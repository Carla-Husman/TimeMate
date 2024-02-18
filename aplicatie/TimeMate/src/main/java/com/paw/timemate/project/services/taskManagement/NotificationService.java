package com.paw.timemate.project.services.taskManagement;

import com.paw.timemate.project.entities.taskManagement.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class for handling notifications related to tasks.
 * This class provides functionalities to send email notifications for tasks.
 */
@Service
public class NotificationService {

    // Injecting the JavaMailSender bean to send email messages
    @Autowired
    private JavaMailSender emailSender;

    /**
     * Sends an email reminder for a specific task.
     *
     * @param to The email address to which the reminder will be sent.
     * @param task The task for which the reminder is sent.
     * @param daysInAdvance The number of days in advance the reminder is for.
     */
    public void sendDueReminder(String to, Task task, int daysInAdvance) {
        String subject = "Reminder: Task '" + task.getTitle() + "' is due in " + daysInAdvance + " day(s)";
        String text = "Don't forget to complete your task '" + task.getTitle() + "' by " + task.getDueDate().toString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

    public void sendNotification(String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("andreeapislariu07@gmail.com");
        message.setSubject("Alert: BLACKLIST Task Detected");
        message.setText(text);
        emailSender.send(message);
    }
}
