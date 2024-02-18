package com.paw.timemate.project.repositories.taskManagement;

import com.paw.timemate.project.entities.taskManagement.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for 'Badge' entities.
 * Extends MongoRepository to provide standard CRUD operations for Badge documents in MongoDB.
 * This interface will be automatically implemented by Spring Data MongoDB.
 */
public interface BadgeRepository extends MongoRepository<Badge, String> {
    List<Badge> findAllByOrderByNecessaryPointsDesc();

    Optional<Badge> findByName(String badgeName);
    // No additional methods needed for basic CRUD operations.
}
