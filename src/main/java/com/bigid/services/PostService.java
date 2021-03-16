package com.bigid.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.Post;
import com.bigid.dao.entity.PostRequest;
import com.bigid.dao.entity.PostShared;
import com.bigid.dao.entity.User;
import com.bigid.web.commands.CommentCommand;
import com.bigid.web.commands.PostCommentCommand;
import com.bigid.web.commands.PostListCommand;
import com.bigid.web.commands.PostRequestCommand;
import com.bigid.web.commands.PostResponseCommand;

/**
 * *This is the PostService interface The service layer is there to provide
 * logic to operate on the data sent to and from the Repository and the client.
 * 
 * @author satish
 *
 */
@Service
public interface PostService {

	/**
	 * This API is going to save the all complete post details .
	 * 
	 * @param post
	 * @throws IOException 
	 */
	void create(PostRequestCommand post) throws IOException;

	/**
	 * This API is used to find the post based on some criteria like pageNumber
	 * and page size
	 * 
	 * @param showCriteria
	 * @param pageNumber
	 * @param size
	 * @return
	 */
	List<PostResponseCommand> findByCriteria(String showCriteria, int pageNumber, int size);

	/**
	 * This API is used to find the post comment based on postId and some
	 * criteria like pageNumber and page size
	 * 
	 * @param postId
	 * @param pageNumber
	 * @param postDefaultFetchSize
	 * @return
	 */
	List<CommentCommand> getCommentsForPost(long postId, int pageNumber, int postDefaultFetchSize);

	/**
	 * This API is going to save the all user comments on based on postId
	 * 
	 * @param postId
	 * @param comment
	 */
	void saveComment(long postId, PostCommentCommand comment);

	/**
	 * This API used to find the all post , posted by particular Id
	 * 
	 * @param postId
	 * @return
	 */
	PostResponseCommand findPostById(long postId);

	/**To find the post based on postId
	 * @author satish
	 * @param postId
	 * @return
	 */
	PostRequest findPostRequestById(long postId);

	/**To get the all post request
	 * @author satish
	 * @return
	 */
	List<Post> getAllPostRequests();

	/**To get the all saved request post based on userId
	 * @author satish
	 * @param userId
	 * @return
	 */
	List<Post> getAllSavedPostRequests(Long userId);

	/**User can disabale the Notification with userId
	 * @param postid
	 * @return
	 */
	public Post disableNotification(Long postid);

	/**User can enable the Notification with userId
	 * @param postid
	 * @return
	 */
	public Post enableNotification(Long postid);

	/**To save the comment on post
	 * @param comment
	 */
	void saveCommentOnPost(Comment comment);

	/**Save the reply commented on post
	 * @param comment
	 */
	void saveReplyOnComment(Long postId,Comment comment);

	/**Update the Tags
	 * @param postId
	 * @param tags
	 */
	void updateTags(long postId, String tags);

	/**Find the list of post based on postId
	 * @param postId
	 * @return
	 */
	Post findOne(Long postId);

	/**To Save the post
	 * @param post
	 */
	void save(Post post);

	/**To Save the Shared Post
	 * @param postId
	 * @param userId
	 * @return
	 */
	public Post saveSharedPost(Long postId, Long userId);

	/**To Save the shared post based on postId and userId
	 * @param userId
	 * @param postId
	 * @return
	 */
	public Post savefollowUsers(Long userId, Long postId);

	/**To Save the followers based on userId and postId
	 * @param postId
	 * @return
	 */
	List<Post> getAllSavedPost(Long postId);

	/**To get the all followers of post
	 * @param id
	 * @return
	 */
	List<User> getFollowers(Long id);


	/**To get the all shared Post using postId
	 * @param postId
	 * @return
	 */
	List<PostShared> getAllSharedPost(Long postId);

	/**To get the all post details by userId
	 * @param userId
	 * @return
	 */
	PostListCommand getAllPosts(long userId);
	/**To get the list of all post details
	 * @param userId
	 * @return
	 */
	PostListCommand getAllPostList();

	/**To get the list of shared post details
	 * @param userId
	 * @return
	 */

	PostListCommand getAllSharedPostByUserId(long userId);
	/**To get the one post details by userd Id
	 * @param userId
	 * @return
	 */
	PostListCommand getPostDetailsByUserId(long userId, long postId);

	/**To get the all post created by particular userId
	 * @param userId
	 * @return
	 */
	PostListCommand getAllPostCreatedUserId(long userId);

	/**Te delete the tags based on postId
	 * @param postId
	 * @param tags
	 */
	public void deleteTags(Long postId, String tags);

	/**This API is used to get all the cureent Feeds Map.
	 * @return
	 */
	Object getAllCurrentFeeds();

	/**This API is used to get the all heat map feeds.
	 * @return
	 */
	Object getHeatMapFeed();

	/**This API is used to get the comment-reply on particular post.
	 * @param postId
	 * @return
	 */
	Object getCommentsReply(Long postId);

	/**This API is used to unsave the post.
	 * @param postId
	 * @return
	 */
	Post unsavePost(Long postId);

}