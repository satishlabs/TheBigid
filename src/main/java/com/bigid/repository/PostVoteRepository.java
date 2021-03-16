package com.bigid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.PostVote;

/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * PostVoteRepository use to save all the of post vote deetails of all the user related data in database
 * @author satish
 *
 */
@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

	/**This API is used to count the postVote, how many vote count is on particular post.
	 * @param userId
	 * @return
	 */
	@Query("SELECT count(*) FROM PostVote t where t.userId = :userId and date(t.pushActivatedTimeByMe) = date(current_date)") 
    int findUserVoteCount(@Param("userId") long userId);

	/**This API is used to get the PostVote based on postId
	 * @param postId
	 * @return
	 */
	List<PostVote> getPostVoteByPostId(Long postId);

	/**This API is used to save the comment on Post
	 * @param post
	 */
	public void save(Comment post);

}
