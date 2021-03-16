package com.bigid.services.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigid.business.Notification;
import com.bigid.common.enums.VoteType;
import com.bigid.dao.entity.Post;
import com.bigid.dao.entity.PostVote;
import com.bigid.dao.entity.User;
import com.bigid.repository.PostRepository;
import com.bigid.repository.PostVoteRepository;
import com.bigid.repository.UserRepository;
import com.bigid.services.PostVoteService;
import com.bigid.web.commands.VoteDetail;
import com.bigid.web.common.CommonUtil;
import com.bigid.web.common.Constants;

@Service
@Transactional
public class PostVoteServiceImpl implements PostVoteService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostVoteRepository postVoteRepository;

	@Autowired
	private UserNotificationServiceImpl notificationService;



	public PostVote getVote(long userId, long postId) {
		// VoteDetail voteDetail = new VoteDetail();

		Post post = postRepository.findOne(postId);
		List<PostVote> votes = post.getVotes();
		PostVote userDBVote = null;
		for (PostVote dbVote : votes) {
			if (dbVote.getUserId() == userId) {
				userDBVote = dbVote;
				break;
			}
		}
		return userDBVote;
	}

	@Override
	public PostVote saveUpVote(long postId, VoteDetail voteDetail) {
		PostVote userDBVote = null;
		boolean flagForSameVote = true;
		if (voteDetail != null) {
			Post post = postRepository.findOne(postId);
			List<PostVote> votes = post.getVotes();

			for (PostVote dbVote : votes) {
				if (dbVote.getUserId() == voteDetail.getUserId()) {
					userDBVote = dbVote;
					break;
				}
			}
			if (voteDetail.isUpVote()) {
				if (null == userDBVote) {
					userDBVote = new PostVote();
					userDBVote.setPostId(postId);
					userDBVote.setUserId(voteDetail.getUserId());
					userDBVote.setUpVotedTimeByMe(new Date(System.currentTimeMillis()));
					userDBVote.setVoteType(VoteType.UP);
					userDBVote.setUpVote(true);
					userDBVote.setPushActivate(true);
					userDBVote.setReportedNSFW(false);

					votes.add(userDBVote);
				} else if (userDBVote.getVoteType().equals(VoteType.DOWN)) {
					userDBVote.setUpVotedTimeByMe(new Date(System.currentTimeMillis()));
					userDBVote.setPushActivate(true);
					userDBVote.setReportedNSFW(false);
					if (userDBVote.isUpVote() == true) {
						flagForSameVote = false;
					}
					userDBVote.setDownVote(false);
					userDBVote.setUpVote(true);
					userDBVote.setVoteType(VoteType.UP);
				} else if (userDBVote.getVoteType().equals(VoteType.UP)) {
					//userDBVote.setPushActivate(true);
					flagForSameVote = true;
				}
				if (flagForSameVote) {
					int vc = getVoteCounted(userDBVote, postId);
					userDBVote.setVoteCount(vc);
					post.setVoteCount(vc);
					postRepository.save(post);
					for (PostVote dbVote : votes) {
						dbVote.setVoteCount(vc);
						postVoteRepository.save(dbVote);
					}
				}

				if (null != userDBVote) {

					postVoteRepository.save(userDBVote);
					post.setVotes(votes);
					postRepository.save(post);
				Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId,
						"LIKED");
					notificationService.publish(notification);
				}
			}
		}
		return userDBVote;
	}

	@Override
	public PostVote saveDownVote(long postId, VoteDetail voteDetail) {
		PostVote userDBVote = null;
		boolean flagForSameVote = true;
		if (voteDetail != null) {
			Post post = postRepository.findOne(postId);
			List<PostVote> votes = post.getVotes();
			for (PostVote dbVote : votes) {
				if (dbVote.getUserId() == voteDetail.getUserId()) {
					userDBVote = dbVote;
					break;
				}
			}
			if (voteDetail.isDownVote()) {
				if (null == userDBVote) {
					userDBVote = new PostVote();
					userDBVote.setPostId(postId);
					userDBVote.setDownVote(true);
					userDBVote.setUpVote(false);
					userDBVote.setUserId(voteDetail.getUserId());
					userDBVote.setDownVotedTimeByMe(new Date(System.currentTimeMillis()));
					userDBVote.setVoteType(VoteType.DOWN);
					userDBVote.setPushActivate(false);
					userDBVote.setReportedNSFW(true);
					userDBVote.setReportedIsNSFW(false);
					votes.add(userDBVote);

				} else if (userDBVote.getVoteType().equals(VoteType.UP)) {
					userDBVote.setDownVotedTimeByMe(new Date(System.currentTimeMillis()));
					userDBVote.setPushActivate(false);
					userDBVote.setReportedNSFW(true);
					userDBVote.setReportedIsNSFW(false);
					if (userDBVote.isDownVote() == true) {
						flagForSameVote = false;
					}
					userDBVote.setDownVote(true);
					userDBVote.setUpVote(false);
					userDBVote.setVoteType(VoteType.DOWN);
				}else if (userDBVote.getVoteType().equals(VoteType.DOWN)) {
					//userDBVote.setReportedNSFW(true);
					flagForSameVote = true;
				}
			}
			if (flagForSameVote) {
				int vc = getVoteCounted(userDBVote, postId);
				userDBVote.setVoteCount(vc);
				post.setVoteCount(vc);
				postRepository.save(post);
				for (PostVote dbVote : votes) {
					dbVote.setVoteCount(vc);
					postVoteRepository.save(dbVote);
				}
			}
			if (null != userDBVote) {
				postVoteRepository.save(userDBVote);
				post.setVotes(votes);
				postRepository.save(post);
			Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId,
					"DISLIKED");
				notificationService.publish(notification);
			}
		}
		return userDBVote;
	}
	
	private int getVoteCounted(PostVote userDBVote, long postId) {
		int vc = 0;
		Post post = postRepository.getOne(postId);
		List<PostVote> votes = post.getVotes();
		for (PostVote vote : votes) {
			if (0 != vote.getVoteCount() && vc < vote.getVoteCount()) {
				vc = vote.getVoteCount();
			}else if (0 != vote.getVoteCount() && vc > vote.getVoteCount()) {
					vc = vote.getVoteCount();	
			}
		}
		if (userDBVote.getVoteType().equals(VoteType.UP)) {
				vc++;
		} else if (userDBVote.getVoteType().equals(VoteType.DOWN)) {
				vc--;	
		}
		return vc;
	}
	@Override
	public PostVote updatePushAvaility(long postId, VoteDetail voteDetail) {
		User user = null;
		PostVote userDBVote = null;
		if (voteDetail != null) {
			Post post = postRepository.findOne(postId);
			List<PostVote> votes = post.getVotes();
			for (PostVote dbVote : votes) {
				if (dbVote.getUserId() == voteDetail.getUserId()) {
					userDBVote = dbVote;
					break;
				}
			}
			if (voteDetail.isPushActivate()) {
				if (null != userDBVote && userDBVote.getVoteType().equals(VoteType.UP)
						&& post.getCreatedBy() != userDBVote.getUserId()
						&& userDBVote.getPushActivatedTimeByMe() == null) {

					user = userRepository.findOne(userDBVote.getUserId());

					if (user.getPushAvailabilityNo() > 0) {
						if (null != user && null != user.getPushAvailabilityNo()) {
							user.setPushAvailabilityNo(
									user.getPushAvailabilityNo() - Constants.GET_PUSH_AVAILABILITY_NO);
						}
						userDBVote.setPushActivatedTimeByMe(new Date(System.currentTimeMillis()));
						userDBVote.setPushActivated(true);
						int vCount = userDBVote.getVoteCount() + Constants.PUSH_AVAILABILITY_NO;
						userDBVote.setVoteCount(vCount);

						int vc = getVoteCounted(userDBVote, postId);
						userDBVote.setVoteCount(vc);
						int pushCount = getPushCount(userDBVote, postId);
						userDBVote.setPushCount(pushCount);
						post.setPushCount(pushCount);
						post.setVoteCount(vc);
						postRepository.save(post);
						for (PostVote dbVote : votes) {
							dbVote.setVoteCount(vCount);
							dbVote.setPushCount(pushCount);
							postVoteRepository.save(dbVote);
							
						}
						userRepository.save(user);
					}
				}
			}
			if (null != userDBVote) {
				postVoteRepository.save(userDBVote);
				Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId, "PUSH");
				notificationService.publish(notification);
			}
		}
		return userDBVote;
	}

	/**Push count on every post based on postId
	 * @param userDBVote
	 * @param postId
	 * @return
	 */
	private int getPushCount(PostVote userDBVote, long postId) {
		int pc = 0;
		Post post = postRepository.getOne(postId);
		List<PostVote> votes = post.getVotes();
		for (PostVote vote : votes) {
			if (0 != vote.getPushCount() && pc < vote.getPostId()) {
				pc = vote.getPushCount();
			}
		}
		if (userDBVote.isPushActivated()) {
			pc++;
		}
		return pc;
	}
	
	@Override
	public PostVote updateIsNsfw(long postId, VoteDetail voteDetail) {
		//User user = null;
		PostVote userDBVote = null;
		if (voteDetail != null) {
			Post post = postRepository.findOne(postId);
			List<PostVote> votes = post.getVotes();
			for (PostVote dbVote : votes) {
				if (dbVote.getUserId() == voteDetail.getUserId()) {
					userDBVote = dbVote;
					break;
				}
			}
			if (voteDetail.isReportedNSFW()) {
				if (null != userDBVote && userDBVote.getVoteType().equals(VoteType.DOWN)
				/* && post.getCreatedBy() != userDBVote.getUserId() */
						&& userDBVote.getReportedIsNSFWTimeByMe() == null) {
					// user = userManager.findOne(userDBVote.getUserId());
					userDBVote.setReportedIsNSFWTimeByMe(new Date(System.currentTimeMillis()));
					userDBVote.setReasonForNsfw(voteDetail.getReasonForNsfw());
					userDBVote.setReportedIsNSFW(true);

					for (PostVote dbVote : votes) {
						postVoteRepository.save(dbVote);
					}
					// userManager.save(user);
				}
			}
			if (null != userDBVote) {
				postVoteRepository.save(userDBVote);
				Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId, "NSFW");
				notificationService.publish(notification);
			}
		}
		return userDBVote;
	}

	@Override
	public PostVote getVote(Long postId) {
		Integer voteCount = 0;
		Integer pushCount = 0;
		Post post = postRepository.findOne(postId);
		List<PostVote> votes = post.getVotes();
		PostVote userDBVote = new PostVote();
		for (PostVote dbVote : votes) {
			voteCount = dbVote.getVoteCount();
			pushCount = dbVote.getPushCount();
			break;
		}
		userDBVote.setVoteCount(voteCount);
		userDBVote.setPushCount(pushCount);
		return userDBVote;
	}
}
