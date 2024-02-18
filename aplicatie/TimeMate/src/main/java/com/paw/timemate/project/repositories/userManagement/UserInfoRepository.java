package com.paw.timemate.project.repositories.userManagement;


import com.paw.timemate.project.entities.userManagement.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for 'UserInfo' entities.
 * Extends JpaRepository to provide standard CRUD operations for UserInfo entities.
 * This interface will be automatically implemented by Spring Data JPA.
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    /**
     * Finds a user by their name.
     * This method returns an Optional, which will be empty if no user is found.
     *
     * @param username The name of the user to find.
     * @return An Optional containing the found UserInfo or empty if no user is found.
     */
    Optional<UserInfo> findByName(String username);
}