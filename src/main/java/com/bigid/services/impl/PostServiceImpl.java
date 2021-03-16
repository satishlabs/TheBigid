package com.bigid.services.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.bigid.business.Notification;
import com.bigid.common.enums.PostCategory;
import com.bigid.common.enums.PostType;
import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.Post;
import com.bigid.dao.entity.PostCommentVote;
import com.bigid.dao.entity.PostMisc;
import com.bigid.dao.entity.PostReplyCommentVote;
import com.bigid.dao.entity.PostRequest;
import com.bigid.dao.entity.PostShared;
import com.bigid.dao.entity.PostVote;
import com.bigid.dao.entity.Reply;
import com.bigid.dao.entity.User;
import com.bigid.dao.entity.UserFollowPost;
import com.bigid.exceptions.BusinessException;
import com.bigid.repository.CommentRepository;
import com.bigid.repository.PostRepository;
import com.bigid.repository.PostSharedRepository;
import com.bigid.repository.ReplyRepository;
import com.bigid.repository.UserFollowRepository;
import com.bigid.repository.UserRepository;
import com.bigid.services.CommentService;
import com.bigid.services.PostCommentVoteService;
import com.bigid.services.PostService;
import com.bigid.services.PostVoteService;
import com.bigid.services.ReplyVoteService;
import com.bigid.services.SecurityService;
import com.bigid.services.UserNotificationService;
import com.bigid.web.commands.CommentCommand;
import com.bigid.web.commands.CurrentFeedCommand;
import com.bigid.web.commands.CurrentFeedCommandWrapper;
import com.bigid.web.commands.HeatMapFeedCommand;
import com.bigid.web.commands.PostCommentCommand;
import com.bigid.web.commands.PostListCommand;
import com.bigid.web.commands.PostRequestCommand;
import com.bigid.web.commands.PostResponseCommand;
import com.bigid.web.commands.ReplyCommand;
import com.bigid.web.common.CommonUtil;
import com.bigid.web.common.Constants;

@Service("PostServiceImpl")
@Transactional
public class PostServiceImpl implements PostService {
	private final Logger logger = Logger.getLogger(PostServiceImpl.class);
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	SecurityService securityManager;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostSharedRepository postSharedRepository;

	@Autowired
	private ReplyVoteService replyVoteService;

	@Autowired
	private UserFollowRepository userFollowRepository;

	@Autowired
	private PostService postService;

	@Autowired
	private ReplyRepository replyRepository;

	@Autowired
	private PostCommentVoteService postCommentVoteService;

	@Autowired
	private UserNotificationService notificationService;

	@Autowired
	private PostVoteService postVoteService;

	@Autowired
	private CommentService commentService;

	@Autowired
	Environment environment;

	@Override
	public void create(PostRequestCommand post) throws IOException {
		logger.info("postServiceImpl :" + post.toString());

		Post postEntity = new Post();
		PostRequest postRequest = new PostRequest();
		BeanUtils.copyProperties(post, postEntity);
		postEntity.setStatusType("NEW");
		try {
			postEntity.setType(PostType.valueOf(post.getPostType().toUpperCase()));
		} catch (IllegalArgumentException e) {
			postEntity.setType(PostType.GENERAL);
		}
		try {
			postEntity.setCategory(PostCategory.valueOf(post.getPostCategory()));
		} catch (IllegalArgumentException e) {
			postEntity.setCategory(PostCategory.CASUAL);
		}
		postEntity.setCreatedBy(CommonUtil.getLoggedInUserId());
		postEntity.setLocation(post.getLocation());
		postEntity.setCountry(post.getCountry());
		postEntity.setPopularity(popularityCount(post.getCountry()));

		Set<String> tags = new HashSet<String>();
		if (post.getTags() != null && post.getTags().length()>0) {
			String[] tagArr = post.getTags().split(",");
			if (tagArr.length > 0) {
				for (String str : tagArr) {
					tags.add(str);
				}
			}
		}
		postEntity.setTags(tags);

		postRequest.setId(postEntity.getId());
		postEntity.setPostRequest(postRequest);
		postEntity.setCreationTimestamp(new Date(System.currentTimeMillis()));
		postEntity.setLastModifiedTimestamp(new Date(System.currentTimeMillis()));
		postEntity.setSavedPostCount(post.getSavedPostCount());

		String sourceData = post.getImgPath();

		if (sourceData != null && sourceData.length() > 0) {
			String[] parts = sourceData.split(",");
			String imageString = parts[1];
			BufferedImage image = null;
			byte[] imageByte;
			imageByte = decodeImage(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();

			String str = System.currentTimeMillis() + "";
			File outputfile;
			if (post.getPostType().equalsIgnoreCase("VISUAL")) {
				outputfile = new File(environment.getProperty("image.path.local.diskPost") + str + ".gif");
			} else {
				outputfile = new File(environment.getProperty("image.path.local.diskPost") + str + ".png");
			}
			String imagePathView = environment.getProperty("image.path.viewPost") + outputfile.getName();
			postEntity.setPostImg(str);
			ImageIO.write(image, "gif", outputfile);
			//ImageIO.write(image, "png", outputfile);
			logger.info("outputfile @:" + outputfile);
			postEntity.setImgPath(imagePathView);
		}

		postRepository.save(postEntity);
		if (post.getIsNSWF() != null
				|| (!StringUtils.isEmpty(post.getFromField()) || !StringUtils.isEmpty(post.getToField()))) {
			PostMisc postMisc = new PostMisc();
			postMisc.setId(postEntity.getId());
			postMisc.setNSWF(post.getIsNSWF());
			postMisc.setFromField(post.getFromField());
			postMisc.setToField(post.getToField());
			postEntity.setPostMisc(postMisc);
			postRepository.save(postEntity);

			Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postEntity.getId(), "POST_CREATE");
			notificationService.publish(notification);

		}

	}
	/**
	 * Decodes the base64 string into byte array
	 *
	 * @param imageString
	 *            - a {@link java.lang.String}
	 * @return byte array
	 */
	public static byte[] decodeImage(String imageString) {
		return Base64.decodeBase64(imageString);
	}

	private Integer popularityCount(String country) {
		int popularity = 1;
		List<Post> postList = postRepository.findAll();
		if (postList.size() > 0) {
			for (Post post : postList) {
				if (post.getCountry().equalsIgnoreCase(country)) {
					popularity++;
				}
			}
		}
		if (postList.size() > 0) {
			for (Post post : postList) {
				if (post.getCountry().equalsIgnoreCase(country)) {
					post.setPopularity(popularity);
				}
				postRepository.save(post);
			}
		}
		return popularity;
	}

	@Override
	public List<PostResponseCommand> findByCriteria(String showCriteria, int pageNumber, int size) {

		List<PostResponseCommand> postDetailCmdLst = Collections.emptyList();
		if (showCriteria.equals(Constants.POST_DEFAULT_LISTING_CRITERIA)) {

			List<Post> postLst = postRepository.findAllByTypeNot(PostType.COMMENT,
					new PageRequest(pageNumber - 1, size, Direction.DESC, "id"));
			if (CollectionUtils.isEmpty(postLst) == false) {
				postDetailCmdLst = new ArrayList<PostResponseCommand>();
				for (Post post : postLst) {
					long userId = post.getCreatedBy();
					PostResponseCommand postDetailCmd = new PostResponseCommand();
					BeanUtils.copyProperties(post, postDetailCmd);

					postDetailCmd.setPostCategory(post.getCategory() == null ? null
							: post.getCategory().name());
					postDetailCmd.setPostType(post.getType() == null ? null : post
							.getType().name());
					postDetailCmd.setUserId(post.getUserId());

					postDetailCmd.setIsNSWF(null == post.getPostMisc() ? null : post
							.getPostMisc().isNSWF());
					postDetailCmd.setToField(null == post.getPostMisc() ? null : post.getPostMisc().getToField());
					postDetailCmd.setFromField(null == post.getPostMisc() ? null : post.getPostMisc().getFromField());

					PostVote voteDetail = postVoteService.getVote(userId,post.getId());
					if (voteDetail != null) {
						postDetailCmd.setVoteDetails(voteDetail);
					} else {
						voteDetail = postVoteService.getVote(post.getId());
						postDetailCmd.setVoteDetails(voteDetail);
					}

					List<CommentCommand> cmCommandLst = new ArrayList<>();
					List<Comment> commentLst = commentRepository
							.getCommentsByPostId(post.getId());

					for (Comment cm : commentLst) {
						CommentCommand cmCommand = new CommentCommand();
						BeanUtils.copyProperties(cm, cmCommand);

						List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm
								.getCommentId());
						List<ReplyCommand> replyCommandLst = new ArrayList<>();
						for (Reply reply : replyLst) {
							ReplyCommand replyCommand = new ReplyCommand();
							BeanUtils.copyProperties(reply, replyCommand);


							PostReplyCommentVote postReplyCommentVote = replyVoteService.replyVote(userId,reply.getReplyId());
							if(postReplyCommentVote != null){
								replyCommand.setPostReplyCommentVote(postReplyCommentVote);
							}else{
								postReplyCommentVote = replyVoteService.replyVote(reply.getReplyId());
								replyCommand.setPostReplyCommentVote(postReplyCommentVote);
							}

							replyCommandLst.add(replyCommand);
						}

						PostCommentVote postCommentVote = postCommentVoteService
								.getCommentVote(userId,cm.getCommentId());
						if(postCommentVote != null){
							cmCommand.setCommentVoteDetails(postCommentVote);
						}else{
							postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
							cmCommand.setCommentVoteDetails(postCommentVote);
						}

						cmCommand.setReplies(replyCommandLst);
						cmCommandLst.add(cmCommand);
					}

					postDetailCmd.setComments(cmCommandLst);

					postDetailCmdLst.add(postDetailCmd);
				}

				PostListCommand postListCommand = new PostListCommand();
				postListCommand.setPosts(postDetailCmdLst);

			}
		}
		return postDetailCmdLst;
	}

	public List<CommentCommand> getCommentsForPost(long postId, int pageNumber, int size) {
		List<CommentCommand> postCommentList = Collections.emptyList();
		Post post = postRepository.findOne(postId);
		long userId = post.getCreatedBy();
		if (post != null) {
			List<Comment> commentLst = postRepository.findAllPostByParentPostIdAndType(post.getId(), PostType.COMMENT,
					new PageRequest(pageNumber - 1, size, Direction.DESC, "creationTimestamp"));
			if (CollectionUtils.isEmpty(commentLst) == false) {
				postCommentList = new ArrayList<CommentCommand>();
				for (Comment cm : commentLst) {
					CommentCommand cmCommand = new CommentCommand();
					BeanUtils.copyProperties(cm, cmCommand);
					List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm.getCommentId());
					List<ReplyCommand> replyCommandLst = new ArrayList<>();
					for (Reply reply : replyLst) {
						ReplyCommand replyCommand = new ReplyCommand();
						BeanUtils.copyProperties(reply, replyCommand);
						PostReplyCommentVote postReplyCommentVote = replyVoteService.replyVote(userId, reply.getReplyId());
						if (postReplyCommentVote != null) {
							replyCommand.setPostReplyCommentVote(postReplyCommentVote);
						} else {
							postReplyCommentVote = replyVoteService.replyVote(reply.getReplyId());
							replyCommand.setPostReplyCommentVote(postReplyCommentVote);
						}
						replyCommandLst.add(replyCommand);
					}
					PostCommentVote postCommentVote = postCommentVoteService.getCommentVote(userId, cm.getCommentId());
					if (postCommentVote != null) {
						cmCommand.setCommentVoteDetails(postCommentVote);
					} else {
						postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
						cmCommand.setCommentVoteDetails(postCommentVote);
					}
					cmCommand.setReplies(replyCommandLst);
					postCommentList.add(cmCommand);
				}
			}
		}
		return postCommentList;
	}


	@Override
	public void saveComment(long postId, PostCommentCommand comment) {
		Post post = postRepository.findOne(postId);
		boolean f = false;
		if (post != null) {
			Post commentEntity = new Post();
			commentEntity.setBody(comment.getBody());
			Set<String> tags = new HashSet<String>();
			String[] tagsStr = comment.getTags().split(",");
			for (String str : tagsStr) {
				tags.add(str);
			}
			commentEntity.setTags(tags);
			commentEntity.setCreatedBy(CommonUtil.getLoggedInUserId());
			commentEntity.setParentPost(post);
			commentEntity.setAnonymous((f = comment.getIsAnonymous()) ? false : f);
			commentEntity.setCommentsCount(comment.getCommentsCount());
			commentEntity.setVoteCount(comment.getVoteCount());
			commentEntity.setType(PostType.COMMENT);
			commentEntity.setCategory(comment.getCategory());
			postRepository.save(commentEntity);
		} else {
			throw new BusinessException("Unable to post comment, no post found with id " + postId);
		}

	}

	@Override
	public PostResponseCommand findPostById(long postId) {
		Post post =postRepository.findAllById(postId);
		PostResponseCommand postResponseCommand = new PostResponseCommand();
		BeanUtils.copyProperties(post, postResponseCommand);
		return postResponseCommand;
	}

	/**
	 * convert to convert Post entity to command object.
	 * 
	 * @param post
	 * @param postCmd
	 * @param isPostMiscNeeded
	 */
	private void convertPostEntity2Command(Post post, PostResponseCommand postCmd, boolean isPostMiscNeeded) {
		BeanUtils.copyProperties(post, postCmd);
		postCmd.setPostCategory(post.getCategory() == null ? null : post.getCategory().name());
		postCmd.setPostType(post.getType() == null ? null : post.getType().name());

		if (!post.isAnonymous())
			postCmd.setUsername(post.getCreator().getUsername());

		if (isPostMiscNeeded && post.getPostMisc() != null)
			BeanUtils.copyProperties(post.getPostMisc(), postCmd);
	}

	@Override
	public List<Post> getAllPostRequests() {

		List<Post> postList = postRepository.findAll();
		System.out.println(postList);
		if (postList != null && postList.size() > 0) {
			postList.get(0).getTags();
		}
		return postList;
	}

	@Override
	public void saveCommentOnPost(Comment comment) {
		long postId = comment.getPostId();
		Post post = postRepository.findOne(postId);
		if (post != null) {
			List<Comment> comments = new ArrayList<Comment>();
			comments.add(comment);
			post.setComment(comments);

			int commentCount = getCommentCounted(postId);
			post.setCommentsCount(commentCount);
			postRepository.save(post);
			Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId, "COMMENT");
			notificationService.publish(notification);
		} else {
			throw new BusinessException("No post found with id " + postId);
		}
	}

	private int getCommentCounted(long postId) {
		int cc = 0;
		//Post post = postRepository.getOne(postId);
		List<Comment> commentsList = commentService.getCommentedByPostId();
		if(commentsList != null && commentsList.size()>0){
			for(Comment c : commentsList){
				if(postId == c.getPostId()){
					cc++;
				}
			}
		}
		return cc;
	}
	public void saveReplyOnComment(Long postId,Comment comment) {
		Post post = postRepository.findOne(postId);
		comment.setPostId(postId);
		List<Comment> comments = post.getComment();
		Comment csave = null;
		for (Comment c : comments) {
			if (c.getCommentId() == comment.getCommentId()) {
				csave = c;
			}
		}
		List<Reply> replies = comment.getReplies();
		if (replies != null && replies.size() > 0) {
			List<Reply> repliesSave = new ArrayList<Reply>();
			for (Reply r : replies) {
				Reply rsave = new Reply();
				rsave.setCommentId(comment.getCommentId());
				rsave.setReplyCommentContent(r.getReplyCommentContent());
				rsave.setReplyCommentBy(r.getReplyCommentBy());
				rsave.setPostId(comment.getPostId());
				rsave.setReplyNotification(true);
				rsave.setReplyCommentTime(new Date(System.currentTimeMillis()));
				repliesSave.add(rsave);
			}
			csave.setReplies(repliesSave);
		}
		commentRepository.save(csave);
		Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId, "REPLIED");
		notificationService.publish(notification);
	}

	@Override
	public void updateTags(long postId, String tags) {
		Post postEntity = postRepository.findOne(postId);

		Set<String> tagss = postEntity.getTags();

		Set<String> dbtags = new HashSet<String>();
		String[] tagArr = tags.split(",");
		for (String str : tagArr) {
			dbtags.add(str);
			dbtags.addAll(tagss);
		}
		System.out.println("dbtags :" + dbtags);
		postEntity.setTags(dbtags);
		postRepository.save(postEntity);

	}


	@Override
	public void deleteTags(Long postId, String tags) {
		Post postEntity = postRepository.findOne(postId);

		Set<String> tagss = postEntity.getTags();
		String[] tagArr = tags.split(",");
		for (String str : tagArr) {
			if(tagss.contains(str)){
				tagss.remove(str);
			}

		}
		//tags after deletion
		System.out.println("dbtags :tags after deletion" + tagss);
		postEntity.setTags(tagss);
		postRepository.save(postEntity);
	}


	@Override
	public Post findOne(Long postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Post post) {
		// TODO Auto-generated method stub

	}

	@Override
	public Post saveSharedPost(Long userId,Long postId) {
		boolean flagForSameVote = true;
		Post post = postRepository.findOne(postId);
		User user = userRepository.findById(userId);
		Post postList = new Post();
		PostShared postShared = new PostShared();

		postShared.setUserId(userId);
		postShared.setSharedBy(user.getUsername());
		postShared.setPostId(postId);
		postShared.setStatusType("SHARED");
		postShared.setSharedTime(new Date(System.currentTimeMillis()));

		postSharedRepository.save(postShared);
		
		if(flagForSameVote){
			int saveCount=savedCount(postId);
			post.setStatusType("SAVED");
			post.setLastModifiedTimestamp(new Date(System.currentTimeMillis()));
			post.setSavedPostCount(saveCount);
			postRepository.save(post);

		}
		postRepository.save(post);

		Notification notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId, "SHARED");
		notificationService.publish(notification);
		return postList;
	}

	public int savedCount(Long postId) {
		int count =0;
		Post post = postRepository.getOne(postId);
		List<Post> postLst = postService.getAllSavedPost(postId);
		if(postLst != null && postLst.size()>0){
			for(Post i:postLst){
				count++;	
			}
		}
		return count;
	}

	@Override
	public List<Post> getAllSavedPost(Long postId) {
		List<Post> postList = new ArrayList<Post>();
		Post post = postRepository.findOne(postId);
		if (post != null) {
			List<PostShared> list = postSharedRepository.findPostSavedByPostId(post.getId());
			for (PostShared postShared : list) {
				Post savedPost = postRepository.findOne(postShared.getPostId());
				postList.add(savedPost);
			}
		}
		return postList;
	}

	@Override
	public Post savefollowUsers(Long userId, Long postId) {

		Post post = postRepository.findOne(postId);
		Post postList = new Post();
		Notification notification;
		UserFollowPost userFollowPost = new UserFollowPost();

		if(userId == post.getCreatedBy()){

		}else{	
			userFollowPost.setFollower(userId);
			userFollowPost.setFollowing(post.getCreatedBy());
			userFollowPost.setFollowNotification(true);
			userFollowPost.setPostId(postId);
			userFollowPost.setSinceFollowing(new Date(System.currentTimeMillis()));
			userFollowRepository.save(userFollowPost);
		}

		post.setFollowUser(true);
		postRepository.save(post);

		notification = Notification.newInstance(CommonUtil.getLoggedInUserId(), postId, "FOLLOW");
		notificationService.publish(notification);

		return postList;

	}

	@Override
	public List<Post> getAllSavedPostRequests(Long userId) {

		List<Post> postList = new ArrayList<Post>();
		User user = userRepository.findOne(userId);
		if (user != null) {
			List<PostShared> list = postSharedRepository.findPostSharedByUserId(user.getId());
			for (PostShared postShared : list) {
				Post post = postRepository.findOne(postShared.getPostId());
				postList.add(post);
			}
		} // else block will throw the exception
		return postList;
	}

	@Override
	public List<PostShared> getAllSharedPost(Long postId) {
		List<PostShared> postList = new ArrayList<PostShared>();
		PostShared postShared = postSharedRepository.findOne(postId);
		if (postShared != null) {
			List<PostShared> list = postSharedRepository.findPostSavedByPostId(postShared.getId());
			for (PostShared postShare : list) {
				PostShared savedPost = postSharedRepository.findOne(postShare.getPostId());
				postList.add(savedPost);
			}
		}
		return postList;
	}

	public Post enableNotification(Long postid) {
		Post post = postRepository.findOne(postid);
		if (post == null) {
			return null;
			// we will throw exeption
		} else {
			post.setPostNotification(true);
			post.setLastModifiedTimestamp(new Date(System.currentTimeMillis()));
			postRepository.save(post);

			return post;
		}

	}

	public Post disableNotification(Long postid) {
		Post post = postRepository.findOne(postid);
		if (post == null) {
			return null;
			// we will throw exeption
		} else {
			post.setPostNotification(false);
			post.setLastModifiedTimestamp(new Date(System.currentTimeMillis()));
			postRepository.save(post);
			return post;
		}
	}

	@Override
	public List<User> getFollowers(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PostRequest findPostRequestById(long postId) {
		return null;
	}


	@Override
	public PostListCommand getAllPosts(long userId) {
		List<PostResponseCommand> postDetailCmdLst = new ArrayList<>();
		List<Post> postLst = postService.getAllPostRequests();
		for (Post post : postLst) {
			// long userIdd = post.getCreatedBy();
			PostResponseCommand postDetailCmd = new PostResponseCommand();
			BeanUtils.copyProperties(post, postDetailCmd);

			postDetailCmd.setPostCategory(post.getCategory() == null ? null : post.getCategory().name());
			postDetailCmd.setPostType(post.getType() == null ? null : post.getType().name());
			postDetailCmd.setUserId(post.getUserId());

			postDetailCmd.setIsNSWF(null == post.getPostMisc() ? null : post.getPostMisc().isNSWF());
			postDetailCmd.setToField(null == post.getPostMisc() ? null : post.getPostMisc().getToField());
			postDetailCmd.setFromField(null == post.getPostMisc() ? null : post.getPostMisc().getFromField());

			PostVote voteDetail = postVoteService.getVote(userId, post.getId());
			if (voteDetail != null) {
				postDetailCmd.setVoteDetails(voteDetail);
			} else {
				voteDetail = postVoteService.getVote(post.getId());
				postDetailCmd.setVoteDetails(voteDetail);
			}
			List<CommentCommand> cmCommandLst = new ArrayList<>();
			List<Comment> commentLst = commentRepository.getCommentsByPostId(post.getId());
			for (Comment cm : commentLst) {
				CommentCommand cmCommand = new CommentCommand();
				BeanUtils.copyProperties(cm, cmCommand);
				List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm.getCommentId());
				List<ReplyCommand> replyCommandLst = new ArrayList<>();
				for (Reply reply : replyLst) {
					ReplyCommand replyCommand = new ReplyCommand();
					BeanUtils.copyProperties(reply, replyCommand);

					PostReplyCommentVote postReplyCommentVote = replyVoteService.replyVote(userId, reply.getReplyId());
					if (postReplyCommentVote != null) {
						replyCommand.setPostReplyCommentVote(postReplyCommentVote);
					} else {
						postReplyCommentVote = replyVoteService.replyVote(reply.getReplyId());
						replyCommand.setPostReplyCommentVote(postReplyCommentVote);
					}
					replyCommandLst.add(replyCommand);
				}
				PostCommentVote postCommentVote = postCommentVoteService.getCommentVote(userId, cm.getCommentId());
				if (postCommentVote != null) {
					cmCommand.setCommentVoteDetails(postCommentVote);
				} else {
					postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
					cmCommand.setCommentVoteDetails(postCommentVote);
				}
				cmCommand.setReplies(replyCommandLst);
				cmCommandLst.add(cmCommand);
			}
			postDetailCmd.setComments(cmCommandLst);
			postDetailCmdLst.add(postDetailCmd);
		}

		PostListCommand postListCommand = new PostListCommand();
		postListCommand.setPosts(postDetailCmdLst);
		return postListCommand;

	}


	@Override
	public PostListCommand getAllPostList() {

		List<PostResponseCommand> postDetailCmdLst = new ArrayList<>();
		List<Post> postLst = postService.getAllPostRequests();
		for (Post post : postLst) {

			PostResponseCommand postDetailCmd = new PostResponseCommand();
			BeanUtils.copyProperties(post, postDetailCmd);

			postDetailCmd.setPostCategory(post.getCategory() == null ? null : post.getCategory().name());
			postDetailCmd.setPostType(post.getType() == null ? null : post.getType().name());
			postDetailCmd.setUserId(post.getUserId());

			postDetailCmd.setIsNSWF(null == post.getPostMisc() ? null : post.getPostMisc().isNSWF());
			postDetailCmd.setToField(null == post.getPostMisc() ? null : post.getPostMisc().getToField());
			postDetailCmd.setFromField(null == post.getPostMisc() ? null : post.getPostMisc().getFromField());

			PostVote voteDetail = postVoteService.getVote(post.getId());
			if (voteDetail != null) {
				postDetailCmd.setVoteDetails(voteDetail);
			} else {
				voteDetail = postVoteService.getVote(post.getId());
				postDetailCmd.setVoteDetails(voteDetail);
			}

			List<CommentCommand> cmCommandLst = new ArrayList<>();
			List<Comment> commentLst = commentRepository.getCommentsByPostId(post.getId());

			for (Comment cm : commentLst) {
				CommentCommand cmCommand = new CommentCommand();
				BeanUtils.copyProperties(cm, cmCommand);

				List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm.getCommentId());
				List<ReplyCommand> replyCommandLst = new ArrayList<>();
				for (Reply reply : replyLst) {
					ReplyCommand replyCommand = new ReplyCommand();
					BeanUtils.copyProperties(reply, replyCommand);
					replyCommandLst.add(replyCommand);
				}
				PostCommentVote postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
				if (postCommentVote != null) {
					cmCommand.setCommentVoteDetails(postCommentVote);
				} else {
					postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
					cmCommand.setCommentVoteDetails(postCommentVote);
				}

				cmCommand.setReplies(replyCommandLst);
				cmCommandLst.add(cmCommand);
			}

			postDetailCmd.setComments(cmCommandLst);

			postDetailCmdLst.add(postDetailCmd);
		}

		PostListCommand postListCommand = new PostListCommand();
		postListCommand.setPosts(postDetailCmdLst);
		return postListCommand;
	}


	@Override
	public PostListCommand getAllSharedPostByUserId(long userId) {
		List<PostResponseCommand> postDetailCmdLst = new ArrayList<>();

		List<Post> postLst = postService.getAllSavedPostRequests(userId);

		for (Post post : postLst) {

			System.out.println("post  ===>" + post);
			if (post.getStatusType().equalsIgnoreCase("SAVED")) {
				PostResponseCommand postDetailCmd = new PostResponseCommand();
				BeanUtils.copyProperties(post, postDetailCmd);

				postDetailCmd.setPostCategory(post.getCategory() == null ? null : post.getCategory().name());
				postDetailCmd.setPostType(post.getType() == null ? null : post.getType().name());
				postDetailCmd.setUserId(post.getUserId());

				postDetailCmd.setIsNSWF(null == post.getPostMisc() ? null : post.getPostMisc().isNSWF());
				postDetailCmd.setToField(null == post.getPostMisc() ? null : post.getPostMisc().getToField());
				postDetailCmd.setFromField(null == post.getPostMisc() ? null : post.getPostMisc().getFromField());

				PostVote voteDetail = postVoteService.getVote(userId, post.getId());
				if (voteDetail != null) {
					postDetailCmd.setVoteDetails(voteDetail);
				} else {
					voteDetail = postVoteService.getVote(post.getId());
					postDetailCmd.setVoteDetails(voteDetail);
				}

				List<CommentCommand> cmCommandLst = new ArrayList<>();
				List<Comment> commentLst = commentRepository.getCommentsByPostId(post.getId());

				for (Comment cm : commentLst) {
					CommentCommand cmCommand = new CommentCommand();
					BeanUtils.copyProperties(cm, cmCommand);

					List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm.getCommentId());
					List<ReplyCommand> replyCommandLst = new ArrayList<>();
					for (Reply reply : replyLst) {
						ReplyCommand replyCommand = new ReplyCommand();
						BeanUtils.copyProperties(reply, replyCommand);

						PostReplyCommentVote postReplyCommentVote = replyVoteService.replyVote(userId,
								reply.getReplyId());
						if (postReplyCommentVote != null) {
							replyCommand.setPostReplyCommentVote(postReplyCommentVote);
						} else {
							postReplyCommentVote = replyVoteService.replyVote(reply.getReplyId());
							replyCommand.setPostReplyCommentVote(postReplyCommentVote);
						}

						replyCommandLst.add(replyCommand);
					}

					PostCommentVote postCommentVote = postCommentVoteService.getCommentVote(userId, cm.getCommentId());
					if (postCommentVote != null) {
						cmCommand.setCommentVoteDetails(postCommentVote);
					} else {
						postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
						cmCommand.setCommentVoteDetails(postCommentVote);
					}

					cmCommand.setReplies(replyCommandLst);
					cmCommandLst.add(cmCommand);
				}

				postDetailCmd.setComments(cmCommandLst);

				postDetailCmdLst.add(postDetailCmd);

			}
		}
		PostListCommand postListCommand = new PostListCommand();
		postListCommand.setPosts(postDetailCmdLst);

		return postListCommand;
	}


	@Override
	public PostListCommand getPostDetailsByUserId(long userId, long postId) {
		List<PostResponseCommand> postDetailCmdLst = new ArrayList<>();

		Post post = postRepository.findOne(postId);

		if(post != null) {

			System.out.println("post  ===>" + post);

			PostResponseCommand postDetailCmd = new PostResponseCommand();
			BeanUtils.copyProperties(post, postDetailCmd);

			postDetailCmd.setPostCategory(post.getCategory() == null ? null : post.getCategory().name());
			postDetailCmd.setPostType(post.getType() == null ? null : post.getType().name());
			postDetailCmd.setUserId(post.getUserId());

			postDetailCmd.setIsNSWF(null == post.getPostMisc() ? null : post.getPostMisc().isNSWF());
			postDetailCmd.setToField(null == post.getPostMisc() ? null : post.getPostMisc().getToField());
			postDetailCmd.setFromField(null == post.getPostMisc() ? null : post.getPostMisc().getFromField());

			PostVote voteDetail = postVoteService.getVote(userId, post.getId());
			if (voteDetail != null) {
				postDetailCmd.setVoteDetails(voteDetail);
			} else {
				voteDetail = postVoteService.getVote(post.getId());
				postDetailCmd.setVoteDetails(voteDetail);
			}

			List<CommentCommand> cmCommandLst = new ArrayList<>();
			List<Comment> commentLst = commentRepository.getCommentsByPostId(post.getId());

			for (Comment cm : commentLst) {
				CommentCommand cmCommand = new CommentCommand();
				BeanUtils.copyProperties(cm, cmCommand);

				List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm.getCommentId());
				List<ReplyCommand> replyCommandLst = new ArrayList<>();
				for (Reply reply : replyLst) {
					ReplyCommand replyCommand = new ReplyCommand();
					BeanUtils.copyProperties(reply, replyCommand);

					PostReplyCommentVote postReplyCommentVote = replyVoteService.replyVote(userId,
							reply.getReplyId());
					if (postReplyCommentVote != null) {
						replyCommand.setPostReplyCommentVote(postReplyCommentVote);
					} else {
						postReplyCommentVote = replyVoteService.replyVote(reply.getReplyId());
						replyCommand.setPostReplyCommentVote(postReplyCommentVote);
					}

					replyCommandLst.add(replyCommand);
				}

				PostCommentVote postCommentVote = postCommentVoteService.getCommentVote(userId, cm.getCommentId());
				if (postCommentVote != null) {
					cmCommand.setCommentVoteDetails(postCommentVote);
				} else {
					postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
					cmCommand.setCommentVoteDetails(postCommentVote);
				}

				cmCommand.setReplies(replyCommandLst);
				cmCommandLst.add(cmCommand);
			}

			postDetailCmd.setComments(cmCommandLst);

			postDetailCmdLst.add(postDetailCmd);
		}

		PostListCommand postListCommand = new PostListCommand();
		postListCommand.setPosts(postDetailCmdLst);

		return postListCommand;
	}

	@Override
	public PostListCommand getAllPostCreatedUserId(long userId) {

		List<PostResponseCommand> postDetailCmdLst = new ArrayList<>();
		List<Post> postLst = postService.getAllPostRequests();
		for (Post post : postLst) {
			if(post.getCreatedBy() == userId){
				PostResponseCommand postDetailCmd = new PostResponseCommand();
				BeanUtils.copyProperties(post, postDetailCmd);

				postDetailCmd.setPostCategory(post.getCategory() == null ? null : post.getCategory().name());
				postDetailCmd.setPostType(post.getType() == null ? null : post.getType().name());
				postDetailCmd.setUserId(post.getUserId());

				postDetailCmd.setIsNSWF(null == post.getPostMisc() ? null : post.getPostMisc().isNSWF());
				postDetailCmd.setToField(null == post.getPostMisc() ? null : post.getPostMisc().getToField());
				postDetailCmd.setFromField(null == post.getPostMisc() ? null : post.getPostMisc().getFromField());

				PostVote voteDetail = postVoteService.getVote(userId, post.getId());
				if (voteDetail != null) {
					postDetailCmd.setVoteDetails(voteDetail);
				} else {
					voteDetail = postVoteService.getVote(post.getId());
					postDetailCmd.setVoteDetails(voteDetail);
				}

				List<CommentCommand> cmCommandLst = new ArrayList<>();
				List<Comment> commentLst = commentRepository.getCommentsByPostId(post.getId());

				for (Comment cm : commentLst) {
					CommentCommand cmCommand = new CommentCommand();
					BeanUtils.copyProperties(cm, cmCommand);

					List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm.getCommentId());
					List<ReplyCommand> replyCommandLst = new ArrayList<>();
					for (Reply reply : replyLst) {
						ReplyCommand replyCommand = new ReplyCommand();
						BeanUtils.copyProperties(reply, replyCommand);

						PostReplyCommentVote postReplyCommentVote = replyVoteService.replyVote(userId,
								reply.getReplyId());
						if (postReplyCommentVote != null) {
							replyCommand.setPostReplyCommentVote(postReplyCommentVote);
						} else {
							postReplyCommentVote = replyVoteService.replyVote(reply.getReplyId());
							replyCommand.setPostReplyCommentVote(postReplyCommentVote);
						}

						replyCommandLst.add(replyCommand);
					}

					PostCommentVote postCommentVote = postCommentVoteService.getCommentVote(userId, cm.getCommentId());
					if (postCommentVote != null) {
						cmCommand.setCommentVoteDetails(postCommentVote);
					} else {
						postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
						cmCommand.setCommentVoteDetails(postCommentVote);
					}

					cmCommand.setReplies(replyCommandLst);
					cmCommandLst.add(cmCommand);
				}

				postDetailCmd.setComments(cmCommandLst);

				postDetailCmdLst.add(postDetailCmd);
			}
		}
		PostListCommand postListCommand = new PostListCommand();
		postListCommand.setPosts(postDetailCmdLst);
		return postListCommand;
	}

	@Override
	public Object getAllCurrentFeeds() {
		CurrentFeedCommandWrapper currFeedComwrap = new CurrentFeedCommandWrapper();
		List<Post> postList = postRepository.findAll();
		ArrayList<CurrentFeedCommand> currentFeedCommandList = new ArrayList<CurrentFeedCommand>();
		if (postList != null && postList.size() > 0) {
			for (Post cf : postList) {
				CurrentFeedCommand currentFeedCommand = new CurrentFeedCommand();
				BeanUtils.copyProperties(cf, currentFeedCommand);
				currentFeedCommand.getCoordinates().add(currentFeedCommand.getLongitude());
				currentFeedCommand.getCoordinates().add(currentFeedCommand.getLatitude());
				currentFeedCommandList.add(currentFeedCommand);
			}
		}
		currFeedComwrap.setFeedlocations(currentFeedCommandList);
		return currFeedComwrap;
	}

	@Override
	public Object getHeatMapFeed() {
		List<HeatMapFeedCommand> postResp = new ArrayList<HeatMapFeedCommand>();
		int count = 0;
		Set<String> countrySet = new HashSet<>();
		List<Post> postList = postRepository.findAll();
		if (postList != null && postList.size() > 0) {
			for (Post hmf : postList) {
				if (!countrySet.contains(hmf.getCountry())) {
					countrySet.add(hmf.getCountry());
					HeatMapFeedCommand temp = new HeatMapFeedCommand();
					temp.setCountry(hmf.getCountry());
					temp.setPopularity(hmf.getPopularity());
					postResp.add(count, temp);
					count++;
				}
			}
		}
		return postResp;
	}

	@Override
	public Object getCommentsReply(Long postId) {
		Post post = postRepository.findOne(postId);

		long userId = post.getCreatedBy();

		List<CommentCommand> cmCommandLst = new ArrayList<>();
		List<Comment> commentLst = commentRepository.getCommentsByPostId(post.getId());

		for (Comment cm : commentLst) {
			CommentCommand cmCommand = new CommentCommand();
			BeanUtils.copyProperties(cm, cmCommand);

			List<Reply> replyLst = replyRepository.getRepliesByCommentId(cm.getCommentId());
			List<ReplyCommand> replyCommandLst = new ArrayList<>();
			for (Reply reply : replyLst) {
				ReplyCommand replyCommand = new ReplyCommand();
				BeanUtils.copyProperties(reply, replyCommand);

				PostReplyCommentVote postReplyCommentVote = replyVoteService.replyVote(userId, reply.getReplyId());
				if (postReplyCommentVote != null) {
					replyCommand.setPostReplyCommentVote(postReplyCommentVote);
				} else {
					postReplyCommentVote = replyVoteService.replyVote(reply.getReplyId());
					replyCommand.setPostReplyCommentVote(postReplyCommentVote);
				}

				replyCommandLst.add(replyCommand);
			}

			PostCommentVote postCommentVote = postCommentVoteService.getCommentVote(userId, cm.getCommentId());
			if (postCommentVote != null) {
				cmCommand.setCommentVoteDetails(postCommentVote);
			} else {
				postCommentVote = postCommentVoteService.getCommentVote(cm.getCommentId());
				cmCommand.setCommentVoteDetails(postCommentVote);
			}

			cmCommand.setReplies(replyCommandLst);
			cmCommandLst.add(cmCommand);
		}
		return cmCommandLst;
	}
	
	@Override
	public Post unsavePost(Long postId) {
		Post post = postRepository.findOne(postId);
		if (post == null) {
			return null;
			// we will throw exeption
		} else {
			post.setStatusType("NEW");
			post.setLastModifiedTimestamp(new Date(System.currentTimeMillis()));
			postRepository.save(post);
			return post;
		}
		
	}

}