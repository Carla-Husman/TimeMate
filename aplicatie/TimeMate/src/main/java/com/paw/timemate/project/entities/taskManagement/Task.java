package com.paw.timemate.project.entities.taskManagement;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Represents the 'Task' entity for storing in the MongoDB database.
 * Each task is an activity or piece of work to be completed by a user.
 */
@Document(collection = "tasks") // Specifies the collection name in MongoDB.
@Data
public class Task {

    @Id
    private String id;
    private int userID;
    private String title;
    private String description;
    private Date startDate;
    private Date dueDate;
    private Boolean isCompleted = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int idUser) {
        this.userID = idUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }


}
