package com.paw.timemate.project.scheduler;

import com.paw.timemate.project.services.taskManagement.NotificationService;
import com.paw.timemate.project.services.taskManagement.TaskService;
import com.paw.timemate.project.entities.taskManagement.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Scheduler component for sending task notifications.
 * This class is responsible for scheduling and executing notification tasks.
 */
@Component
public class TaskNotificationScheduler {

    // TaskService is used to interact with task-related data
    @Autowired
    private TaskService taskService;

    // NotificationService is used to handle the process of sending notifications
    @Autowired
    private NotificationService notificationService;

    /**
     * Scheduled method to send notifications for due tasks.
     * This method runs daily at 00:00 AM and sends reminders for tasks that are due the next day.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    // @Scheduled(cron = "0 23 2 * * ?")
    public void sendDueDateNotifications() {
        LocalDate now = LocalDate.now();
        System.out.println("Running task notification scheduler at " + now.toString());

        LocalDate dueDate = now.plusDays(1);

        List<Task> tasksDue = taskService.findTasksByDueDate(dueDate);
        System.out.println("The number of tasks due tomorrow is: " + tasksDue.size());

        for (Task task : tasksDue) {
            String userEmail = taskService.getUserEmailByUserId(task.getUserID());
            System.out.println("User email: " + userEmail);
            System.out.println("Tomorrow's task: " + task.toString());
            notificationService.sendDueReminder(userEmail, task, 1);
        }
    }

}
