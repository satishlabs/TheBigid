package com.bigid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigid.dao.entity.PostReplyCommentVote;

/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * ReplyVoteRepository use to save all the user related data in database
 * @author satish
 *
 */
public interface ReplyVoteRepository extends JpaRepository<PostReplyCommentVote, Long>{

}
