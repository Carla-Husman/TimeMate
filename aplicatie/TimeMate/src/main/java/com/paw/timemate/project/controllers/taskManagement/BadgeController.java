package com.paw.timemate.project.controllers.taskManagement;


import com.paw.timemate.project.entities.taskManagement.Badge;
import com.paw.timemate.project.services.taskManagement.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Badge entities.
 * This class handles HTTP requests and delegates business actions to the BadgeService.
 */@RestController
@RequestMapping(value = "/badges")
@CrossOrigin(origins = "http://localhost:4200")
public class BadgeController {

    // Injecting the BadgeService to interact with badge data.
    @Autowired
    private BadgeService badgeService;

    /**
     * Handles GET requests to "/badges" and retrieves all badges.
     *
     * @return A ResponseEntity containing a list of all Badge entities and the HTTP status code.
     */
    @GetMapping
    @PreAuthorize("@userAccessService.hasAccess(authentication.principal.username)")
    public ResponseEntity<List<Badge>> findAllBadges() {
        List<Badge> badges = badgeService.findAll();
        return new ResponseEntity<>(badges, HttpStatus.OK);
    }

    /**
     * Handles GET requests to "/badges/{id}" to retrieve a specific badge by its ID.
     *
     * @param id The ID of the badge to be retrieved.
     * @return A ResponseEntity containing the Badge entity if found, or an HTTP NOT_FOUND status.
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("@userAccessService.hasAccess(authentication.principal.username)")
    public ResponseEntity<Badge> findBadgeById(@PathVariable String id) {
        Badge badge = badgeService.findById(id);
        return badge != null
                ? new ResponseEntity<>(badge, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
