package com.paw.timemate.project.services.taskManagement;

import com.paw.timemate.project.entities.taskManagement.Task;
import com.paw.timemate.project.repositories.taskManagement.TaskRepository;
import com.paw.timemate.project.repositories.userManagement.UserInfoRepository;
import com.paw.timemate.project.services.userManagement.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserInfoService userInfoService;

    //INSERT
    // Test inserting a task with a blacklisted word in the title and description
    @Test
    void insertWithBlackListWord1() {
        Task newTask = new Task();
        newTask.setTitle("Dau bomba in AC");
        newTask.setDescription("Dau bomba in AC");
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);


        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "BLACKLIST");
    }

    // Test inserting a task with a blacklisted word in the title and description (case insensitive)
    @Test
    void insertWithBlackListWord2() {
        Task newTask = new Task();
        newTask.setTitle("Dau boMbA in AC");
        newTask.setDescription("Dau boMbA in AC");
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);

        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "BLACKLIST");
    }

    // Test inserting a task with a blacklisted word only in the description
    @Test
    void insertWithBlackListWord3() {
        Task newTask = new Task();
        newTask.setTitle("Sunt cuminte in AC");
        newTask.setDescription("Dau boMbA in AC");
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);

        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "BLACKLIST");
    }

    // Test inserting a task with a blacklisted word only in the title
    @Test
    void insertWithBlackListWord4() {
        Task newTask = new Task();
        newTask.setTitle("Dau bomba in AC");
        newTask.setDescription("Sunt cuminte in AC");
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);

        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "BLACKLIST");
    }

    // Test inserting a task with due date before start date
    @Test
    void insertWithDueDateBeforeStartDate1() {
        Task newTask = new Task();
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-05T07:59:59");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);

        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "DUEDATE");
    }

    // Test inserting a task with due date equal to start date
    @Test
    void insertWithDueDateBeforeStartDate2() {
        Task newTask = new Task();
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(startDate);
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);

        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "DUEDATE");
    }

    // Test inserting a valid task
    @Test
    void insertValidTask1() {
        Task newTask = new Task();
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-05T09:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);

        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "Task valid");
    }

    // Test inserting another valid task
    @Test
    void insertValidTask2() {
        Task newTask = new Task();
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);

        Task resultingTask = taskService.insert(0, newTask);
        assertEquals(resultingTask.getTitle(), "Task valid");
    }

    //UPDATE
    
    //Test updating a task with a blacklisted word in the title and description.
    @Test
    void updateWithBlackListWord1() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        Task newUpdateTask = new Task();
        newUpdateTask.setTitle("Dau bomba in AC");
        newUpdateTask.setDescription("Dau bomba in AC");
        Task resultingUpdateTask = taskService.updateTask("123", newUpdateTask);
        assertEquals(resultingUpdateTask.getTitle(), "BLACKLIST");
    }

    // Test updating a task with a blacklisted word in the title and description (case insensitive)
    @Test
    void updateWithBlackListWord2() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        Task newUpdateTask = new Task();
        newUpdateTask.setTitle("Dau boMbA in AC");
        newUpdateTask.setDescription("Dau boMbA in AC");
        Task resultingUpdateTask = taskService.updateTask("123", newUpdateTask);
        assertEquals(resultingUpdateTask.getTitle(), "BLACKLIST");
    }

    // Test updating a task with a blacklisted word only in the description
    @Test
    void updateWithBlackListWord3() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        Task newUpdateTask = new Task();
        newUpdateTask.setTitle("Dau bomba in AC");
        newUpdateTask.setDescription("Sunt cuminte in AC");
        Task resultingUpdateTask = taskService.updateTask("123", newUpdateTask);
        assertEquals(resultingUpdateTask.getTitle(), "BLACKLIST");
    }

    // Test updating a task with a blacklisted word only in the title
    @Test
    void updateWithBlackListWord4() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        Task newUpdateTask = new Task();
        newUpdateTask.setTitle("Sunt cuminte in AC");
        newUpdateTask.setDescription("Dau bomba in AC");
        Task resultingUpdateTask = taskService.updateTask("123", newUpdateTask);
        assertEquals(resultingUpdateTask.getTitle(), "BLACKLIST");
    }

    // Test updating a task with due date before start date
    @Test
    void updateWithDueDateBeforeStartDate1() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        startDate = Date.from(instant);
        localDueDateTime = LocalDateTime.parse("2023-12-05T07:59:59");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        dueDate = Date.from(instant);

        Task updateTaskInfo = new Task();
        updateTaskInfo.setStartDate(startDate);
        updateTaskInfo.setDueDate(dueDate);
        Task resultingUpdateTask = taskService.updateTask("123", updateTaskInfo);

        assertEquals(resultingUpdateTask.getTitle(), "DUEDATE");
    }

    // Test updating a task with due date equal to start date
    @Test
    void updateWithDueDateBeforeStartDate2() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        LocalDateTime localDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        startDate = Date.from(instant);

        Task updateTaskInfo = new Task();
        updateTaskInfo.setStartDate(startDate);
        updateTaskInfo.setDueDate(startDate);
        Task resultingUpdateTask = taskService.updateTask("123", updateTaskInfo);

        assertEquals(resultingUpdateTask.getTitle(), "DUEDATE");
    }

    // Test updating a task with valid date range
    @Test
    void updateValidTask1() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-05T09:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        startDate = Date.from(instant);
        localDueDateTime = LocalDateTime.parse("2023-12-05T09:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        dueDate = Date.from(instant);

        Task updateTaskInfo = new Task();
        updateTaskInfo.setStartDate(startDate);
        updateTaskInfo.setDueDate(dueDate);
        Task resultingUpdateTask = taskService.updateTask("123", updateTaskInfo);

        assertEquals(resultingUpdateTask.getTitle(), "Task valid");
    }

    // Test updating another task with valid date range
    @Test
    void updateValidTask2() {
        taskService = new TaskService();
        userInfoService = new UserInfoService();
        notificationService = new NotificationService();
        ReflectionTestUtils.setField(userInfoService, "repository", userRepository);
        ReflectionTestUtils.setField(taskService, "userInfoService", userInfoService);
        ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        ReflectionTestUtils.setField(notificationService, "emailSender", javaMailSender);
        ReflectionTestUtils.setField(taskService, "notificationService", notificationService);
        taskService.deleteTask("123");
        Task newTask = new Task();
        newTask.setId("123");
        newTask.setTitle("Task valid");
        newTask.setDescription("Task valid");
        LocalDateTime localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        Instant instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date startDate = Date.from(instant);
        LocalDateTime localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date dueDate = Date.from(instant);
        newTask.setStartDate(startDate);
        newTask.setDueDate(dueDate);
        Task resultingTask = taskService.insert(0, newTask);

        localStartDateTime = LocalDateTime.parse("2023-12-05T08:00:00");
        instant = localStartDateTime.atZone(ZoneId.systemDefault()).toInstant();
        startDate = Date.from(instant);
        localDueDateTime = LocalDateTime.parse("2023-12-06T08:00:00");
        instant = localDueDateTime.atZone(ZoneId.systemDefault()).toInstant();
        dueDate = Date.from(instant);

        Task updateTaskInfo = new Task();
        updateTaskInfo.setStartDate(startDate);
        updateTaskInfo.setDueDate(dueDate);
        Task resultingUpdateTask = taskService.updateTask("123", updateTaskInfo);

        assertEquals(resultingUpdateTask.getTitle(), "Task valid");
    }
}
