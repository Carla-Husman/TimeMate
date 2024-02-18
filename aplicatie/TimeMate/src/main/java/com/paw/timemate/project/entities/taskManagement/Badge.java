package com.paw.timemate.project.entities.taskManagement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

/**
 * Represents the 'Badge' entity for storing in the MongoDB database.
 * Each badge symbolizes a certain achievement or status within the application.
 */
@Document(collection = "badges")
@Data
public class Badge {
    @Id
    private String id;
    private String name;
    private String description;
    private String rank;
    private int necessaryPoints;
}
