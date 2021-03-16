package com.bigid.web.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bigid.common.enums.VoteType;
import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.MessageResponse;
import com.bigid.dao.entity.Post;
import com.bigid.dao.entity.PostCommentVote;
import com.bigid.dao.entity.PostReplyCommentVote;
import com.bigid.dao.entity.PostVote;
import com.bigid.dao.entity.User;
import com.bigid.repository.PostRepository;
import com.bigid.repository.PostVoteRepository;
import com.bigid.repository.ReplyRepository;
import com.bigid.services.PostCommentVoteService;
import com.bigid.services.PostService;
import com.bigid.services.PostVoteService;
import com.bigid.services.ReplyService;
import com.bigid.services.UserService;
import com.bigid.web.commands.CommentCommand;
import com.bigid.web.commands.PostListCommand;
import com.bigid.web.commands.PostRequestCommand;
import com.bigid.web.commands.PostResponseCommand;
import com.bigid.web.common.Constants;

//import sun.misc.BASE64Decoder;

/**
 * @author satish
 *
 */
@RestController
@RequestMapping("/post/")
public class PostController {

	private final Logger logger = Logger.getLogger(PostController.class);

	@Autowired
	private PostService postService;

	@Autowired
	private PostVoteService postVoteService;

	@Autowired
	private PostCommentVoteService postCommentVoteService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostVoteRepository postVoteRepository;
	
	@Autowired ReplyRepository replyRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ReplyService replyService;


	/**
	 * This API is used to post the post details data
	 * 
	 * @param postCommand
	 * @throws IOException
	 */
	@RequestMapping(value = "/publish", method = { RequestMethod.POST, RequestMethod.PUT })
	public void create(@RequestBody PostRequestCommand postCommand/*,
			@RequestParam(name = "postImg", required = false) MultipartFile postImg*/) throws IOException {


			postService.create(postCommand);
		logger.info("Post created successfully");
	}

	/**
	 * This API is used to find post response data based on postId
	 * 
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "/{postId}", method = { RequestMethod.GET })
	public ResponseEntity<PostResponseCommand> getPostById(@PathVariable long postId) {
		logger.info("searching post by :" + postId);
		PostResponseCommand postResponseCommand = postService.findPostById(postId);
		logger.info("Found post " + postResponseCommand + "for id :" + postId);
		return new ResponseEntity<PostResponseCommand>(postResponseCommand,
				HttpStatus.OK);
	}
	/**
	 * This API is used to find the post details based on some critera like page
	 * number and size request will be /post/show/latest?page=1&size=1
	 * 
	 * @param showCriteria
	 * @param pageNumber
	 * @param maxPost
	 * @return
	 */
	@RequestMapping(value = "/show/{showCriteria}", method = { RequestMethod.GET })
	public List<PostResponseCommand> getPostsByCriteria(@PathVariable String showCriteria,
			@RequestParam(required = false, name = "page", defaultValue = "1") int pageNumber,
			@RequestParam(required = false, name = "size") Integer maxPost) {

		if (StringUtils.isEmpty(showCriteria)) {
			showCriteria = Constants.POST_DEFAULT_LISTING_CRITERIA;
		}

		return postService.findByCriteria(showCriteria, pageNumber,
				maxPost == null ? Constants.POST_DEFAULT_FETCH_SIZE : maxPost);
	}

	/**
	 * @param postId
	 * @param pageNumber
	 * @param maxComment
	 * @return
	 */
	@RequestMapping(value = { "/{postId}/comments" }, method = { RequestMethod.GET })
	public List<CommentCommand> getAllCommentsForPost(@PathVariable long postId,
			@RequestParam(required = false, name = "page", defaultValue = "1") int pageNumber,
			@RequestParam(required = false, name = "size") Integer maxComment) {
		logger.info("Found the comments based on postId");
		return postService.getCommentsForPost(postId, pageNumber,
				maxComment == null ? Constants.POST_DEFAULT_FETCH_SIZE : maxComment);
	}

	/**
	 * @param userId
	 *            This API used to display complete list of post,comments,reply
	 *            and votedetails
	 */
	@GetMapping(value = "/{userId}/list")
	public ResponseEntity<PostListCommand> getAllPosts(@PathVariable long userId) {

		PostListCommand postListCommand = postService.getAllPosts(userId);

		return new ResponseEntity<PostListCommand>(postListCommand,
				HttpStatus.OK);

	}

	/**
	 * @param userId
	 *            This API used to display complete list of post,comments,reply
	 *            and votedetails
	 */
	@GetMapping(value = "/list")
	public ResponseEntity<PostListCommand> getAllPostList() {

		PostListCommand postListCommand = postService.getAllPostList();

		return new ResponseEntity<PostListCommand>(postListCommand,
				HttpStatus.OK);

	}

	/**
	 * @author satish
	 * @param postId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/shared/{userId}/{postId}", method = { RequestMethod.POST })
	public ResponseEntity<MessageResponse> sharedPost(@PathVariable Long postId, @PathVariable Long userId) {
		postService.saveSharedPost(userId, postId);
		return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("success_status", null, null),
				messageSource.getMessage("shared_post", null, null)), HttpStatus.OK);
	}
	
	/**This API is used to Unsave the Post
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "/unsavePost/{postId}", method = { RequestMethod.PUT })
	public ResponseEntity<MessageResponse> unsavePost(@PathVariable Long postId) {
		postService.unsavePost(postId);
		return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("success_status", null, null),
				messageSource.getMessage("unsaved_post", null, null)), HttpStatus.OK);
	}

	/**To get the All shared Post
	 * @author satish
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/shared/{userId}")
	public ResponseEntity<PostListCommand> getAllSharedPost(@PathVariable long userId) {

		PostListCommand postListCommand = postService.getAllSharedPostByUserId(userId);

		return new ResponseEntity<PostListCommand>(postListCommand,
				HttpStatus.OK);

	}

	/**To get the particular post details by particular userId
	 * @param userId
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "/{userId}/{postId}", method = { RequestMethod.GET })
	public ResponseEntity<PostListCommand> getPostDetailsByUserId(@PathVariable long userId,@PathVariable long postId) {

		PostListCommand postListCommand = postService.getPostDetailsByUserId(userId,postId);

		return new ResponseEntity<PostListCommand>(postListCommand,
				HttpStatus.OK);

	}

	/** To get the all post by particulr userId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/{userId}/allpost", method = { RequestMethod.GET })
	public ResponseEntity<Object> getAllPostCreatedUserId(@PathVariable long userId) {
		User user = userService.findByUserId(userId);
		if(user == null){
			return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
					messageSource.getMessage("all_post", null, null)), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(postService.getAllPostCreatedUserId(userId),HttpStatus.OK);

	}

	/**
	 * @param postId
	 * @param tags
	 * @update the post Tags
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{postId}/tags", method = { RequestMethod.PUT })
	public ResponseEntity updateTags(@PathVariable Long postId, @RequestBody String tags) {

		logger.info("Found the downvote based on postId");
		postService.updateTags(postId, tags);

		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	/**
	 * @param postId
	 * @param tags
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{postId}/tags", method = { RequestMethod.DELETE })
	public ResponseEntity deleteTags(@PathVariable Long postId, @RequestBody String tags) {

		logger.info("Found the downvote based on postId");
		postService.deleteTags(postId, tags);

		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}


	/**
	 * @param postId
	 * @param comment
	 * @return
	 */
	@RequestMapping(value = "/{postId}/comments", method = RequestMethod.POST)
	public ResponseEntity<Comment> saveComents(@PathVariable Long postId, @RequestBody Comment comment) {
		Post post = new Post();
		post.setId(postId);
		comment.setPostId(postId);
		comment.setCommentNotification(true);
		comment.setCommentPostedTime(new Date(System.currentTimeMillis()));
		postService.saveCommentOnPost(comment);

		return new ResponseEntity<Comment>(comment, HttpStatus.OK);
	}

	/**
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "/{postId}/commentsreply", method = RequestMethod.GET)
	public ResponseEntity<Object> getCommentsReply(@PathVariable Long postId) {
		Post post = postRepository.findAllById(postId);
		if(post == null){
			return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
					messageSource.getMessage("comment_reply_on_postId", null, null)), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(postService.getCommentsReply(postId),HttpStatus.OK);	
	}

	/**
	 * @param postId
	 * @param comment
	 */

	@RequestMapping(value = "/{postId}/replies", method = RequestMethod.POST, consumes = "APPLICATION/JSON")
	public void saveReplies(@PathVariable Long postId, @RequestBody Comment comment) {
		postService.saveReplyOnComment(postId, comment);
	}

	/**
	 * @ To get the upVote details on Post from this API
	 * 
	 * @author satish
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	@RequestMapping(value = "/{postId}/upvote", method = { RequestMethod.POST })
	public ResponseEntity<PostVote> upVote(@PathVariable Long postId,
			@RequestBody com.bigid.web.commands.VoteDetail voteDetail) {
		logger.info("Found the downvote based on postId");
		PostVote postVote = postVoteService.saveUpVote(postId, voteDetail);

		return new ResponseEntity<>(postVote, HttpStatus.OK);
	}

	/**
	 * To get the downVote details on Post from this API
	 * 
	 * @author satish
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	@RequestMapping(value = "/{postId}/downvote", method = { RequestMethod.POST })
	public ResponseEntity<PostVote> downVvote(@PathVariable Long postId,
			@RequestBody com.bigid.web.commands.VoteDetail voteDetail) {
		logger.info("Found the downvote based on postId");
		PostVote postVote = postVoteService.saveDownVote(postId, voteDetail);

		return new ResponseEntity<>(postVote, HttpStatus.OK);
	}

	/**
	 * To get the pushVote details on Post from this API
	 * 
	 * @author satish
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	@RequestMapping(value = "/{postId}/pushedvote", method = { RequestMethod.POST })
	public ResponseEntity<PostVote> pushavailibilty(@PathVariable Long postId,
			@RequestBody com.bigid.web.commands.VoteDetail voteDetail) {
		logger.info("Found the downvote based on postId");
		PostVote postVote = postVoteService.updatePushAvaility(postId, voteDetail);

		return new ResponseEntity<>(postVote, HttpStatus.OK);
	}

	/**
	 * To get the isNSFW
	 * 
	 * @author satish
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	@RequestMapping(value = "/{postId}/isnsfw", method = { RequestMethod.POST })
	public ResponseEntity<PostVote> reportedIsNSFW(@PathVariable Long postId,
			@RequestBody com.bigid.web.commands.VoteDetail voteDetail) {
		logger.info("Found the downvote based on postId");
		PostVote postVote = postVoteService.updateIsNsfw(postId, voteDetail);

		return new ResponseEntity<>(postVote, HttpStatus.OK);
	}

	/**
	 * @author satish
	 * @param postId
	 * @param postVote
	 * @return
	 */
	@RequestMapping(value = "/pushedvote", method = RequestMethod.PUT)
	public String pushedvote(@RequestParam Long postId, @RequestBody PostVote postVote) {
		try {
			if (postVote != null) {

				Post postV = postRepository.findOne(postId);
				if (postV != null) {
					if (postVote.isPushActivate()) {
						if (null != postVote && postVote.getVoteType().equals(VoteType.UP)) {
							postVote.setPushActivatedTimeByMe(new Date(System.currentTimeMillis()));
							postVote.setPushCount(getPushCount(postId));
							postVote.setVoteCount(getPushVoteCount(postId));
							postVote = postVoteRepository.save(postVote);
						}
					}
				}
			}
		} catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return "User succesfully updated!";
	}

	/**
	 * 
	 * @author satish
	 * @param postId
	 * @return
	 */
	private Integer getPushCount(long postId) {
		Post post = postService.findOne(postId);
		List<PostVote> votes = post.getVotes();
		int pushCount = 0;
		for (PostVote dbVote : votes) {
			if (dbVote.isPushActivate() && dbVote.getPushActivatedTimeByMe() != null) {
				pushCount++;
			}
		}
		return Integer.valueOf(pushCount);
	}

	/**
	 * @author satish
	 * @param postId
	 * @return
	 */
	private Integer getPushVoteCount(long postId) {
		Post post = postService.findOne(postId);
		List<PostVote> votes = post.getVotes();
		int pushCount = 0;
		for (PostVote dbVote : votes) {
			if (dbVote.isPushActivate() && dbVote.getPushActivatedTimeByMe() != null) {
				pushCount = pushCount + 2;
			}
		}
		return Integer.valueOf(pushCount);
	}

	/**
	 * To get the commentUpvote.
	 * 
	 * @author satish
	 * @param commentId
	 * @param commentVoteDetail
	 * @return
	 */
	@RequestMapping(value = "/{commentId}/commentupvote", method = { RequestMethod.POST })
	public ResponseEntity<PostCommentVote> commentUpVote(@PathVariable Long commentId,
			@RequestBody com.bigid.web.commands.CommentVoteDetail commentVoteDetail) {
		logger.info("Found the downvote based on postId");
		PostCommentVote postCommentVote = postCommentVoteService.saveCommentUpVote(commentId, commentVoteDetail);

		return new ResponseEntity<>(postCommentVote, HttpStatus.OK);
	}

	/**
	 * To get the commentDownVote
	 * 
	 * @author satish
	 * @param commentId
	 * @param commentVoteDetail
	 * @return
	 */
	@RequestMapping(value = "/{commentId}/commentdownvote", method = { RequestMethod.POST })
	public ResponseEntity<PostCommentVote> downVvote(@PathVariable Long commentId,
			@RequestBody com.bigid.web.commands.CommentVoteDetail commentVoteDetail) {
		logger.info("Found the downvote based on postId");
		PostCommentVote postCommentVote = postCommentVoteService.saveCommentDownVote(commentId, commentVoteDetail);

		return new ResponseEntity<>(postCommentVote, HttpStatus.OK);
	}
	/**
	 * To get the replyUpVote
	 * 
	 * @author satish
	 * @param replyId
	 * @param postReplyCommandVoteDetail
	 * @return
	 */
	@RequestMapping(value = "/{replyId}/replycommentupvote", method = { RequestMethod.POST })
	public ResponseEntity<PostReplyCommentVote> downVvote(@PathVariable Long replyId,
			@RequestBody com.bigid.web.commands.PostReplyCommandVoteDetail postReplyCommandVoteDetail) {
		logger.info("Found the downvote based on postId");
		PostReplyCommentVote postReplyCommentVote = replyService.saveReplyUpVote(replyId, postReplyCommandVoteDetail);

		return new ResponseEntity<>(postReplyCommentVote, HttpStatus.OK);
	}
	/**
	 * 
	 * @author satish
	 * @return
	 */
	@RequestMapping(value = "/currentfeeds", method = { RequestMethod.GET })
	public Object getcurrentfeeds() {
		List<Post> postList = postRepository.findAll();
		if(postList.size()<0 && postList.isEmpty()){
				return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
						messageSource.getMessage("current_feed_map", null, null)), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(postService.getAllCurrentFeeds(),HttpStatus.OK);
	}	
	/**
	 * @author satish
	 * @param postId
	 */
	@RequestMapping(value = "/heatmapfeeds", method = { RequestMethod.GET })
	public Object getHeatMapFeed() {
		List<Post> postList = postRepository.findAll();
		if(postList.size()<0 && postList.isEmpty()){
				return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
						messageSource.getMessage("heat_feed_map", null, null)), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(postService.getHeatMapFeed(),HttpStatus.OK);
	}
	/**
	 * @author satish
	 * @param postId
	 * @return
	 */

	@RequestMapping(value = "/enablenotification/{postId}", method = { RequestMethod.PUT })
	public ResponseEntity<MessageResponse> enableNotification(@PathVariable Long postId) {
		postService.enableNotification(postId);
		return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("success_status", null, null),
				messageSource.getMessage("enablePost_notification", null, null)), HttpStatus.OK);
	}
	/**
	 * @author satish
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "/disablenotification/{postId}", method = { RequestMethod.PUT })
	public ResponseEntity<MessageResponse> disableNotification(@PathVariable Long postId) {
		postService.enableNotification(postId);
		return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("success_status", null, null),
				messageSource.getMessage("disablePost_notification", null, null)), HttpStatus.OK);
	}
	/**
	 * This API is used to save the post follower based on userId and postId
	 * 
	 * @author satish
	 * @param userId
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "/{userId}/{postId}", method = { RequestMethod.POST })
	public ResponseEntity<MessageResponse> followUser(@PathVariable Long userId, @PathVariable Long postId) {
		logger.info("Found the follower based on postId");
		postService.savefollowUsers(userId, postId);
		return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("success_status", null, null),
				messageSource.getMessage("post_follower", null, null)), HttpStatus.OK);
	}
	/**
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/followers/{userId}", method = { RequestMethod.GET })
	public Object getFollowersByPostId(@PathVariable Long userId) {
		User user = userService.findByUserId(userId);
		if(user == null){
			return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
					messageSource.getMessage("user_followed_by", null, null)), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(userService.getFollowersByPostId(userId),HttpStatus.OK);
	}
}
