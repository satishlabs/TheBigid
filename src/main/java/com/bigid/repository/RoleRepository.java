package com.bigid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bigid.dao.entity.Role;

/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * RoleRepository use to save all the role of user related data in database
 * @author satish
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
