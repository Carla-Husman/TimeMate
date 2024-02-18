package com.paw.timemate.project.entities.userManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents the user information entity.
 * This class is used for storing and managing user-specific data, such as credentials and roles.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private int streak = 0;
    private Date joinDate;
    private String roles = "ROLE_USER";
    private String access = "ALLOWED";
    private String badges = "Starter";
    @PrePersist
    public void prePersist() {
        if (joinDate == null) {
            joinDate = new Date(); // Setează data curentă
        }
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String newUsername) {
        this.name = newUsername;
    }

    public void AddBadge(String badgeName) {
        badges = badges + "+" + badgeName;
    }
}