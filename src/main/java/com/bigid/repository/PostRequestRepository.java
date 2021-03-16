package com.bigid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.PostRequest;

/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * PostRequestRepository use to list of post all the user related data in database
 * @author satish
 *
 */
@Repository
public interface PostRequestRepository extends JpaRepository<PostRequest, Long> {
	
	
}
