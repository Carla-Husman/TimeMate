package com.paw.timemate.project.services.taskManagement;

import com.paw.timemate.project.entities.taskManagement.Task;
import com.paw.timemate.project.entities.userManagement.UserInfo;
import com.paw.timemate.project.repositories.taskManagement.TaskRepository;
import com.paw.timemate.project.services.userManagement.UserInfoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Service class for handling operations related to 'Task' entities.
 * This class provides business logic and interacts with the repository layer to manage tasks.
 */
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BadgeService badgeService;

    private List<String> blackList = Arrays.asList("crima", "crime", "asasinat", "asasinate", "bombardament", "bombardamente",
            "omor", "omoruri", "sinucidere", "ranire", "injunghiere", "injunghieri", "atentat", "atentate",
            "criminal", "criminali", "drog", "droguri", "supradoza", "atac", "atacuri", "mutilare", "mutilari",
            "automutilare", "automutilari", "cox", "bomba", "bombe", "explozibil", "explozie", "tnt",
            "dinamita", "robie", "rob", "sclav", "sclavi", "sclavie", "xxx", "porno", "pornografie");
    
    @Autowired
    private NotificationService notificationService;

    /**
     * Retrieves all tasks associated with a specific user ID.
     *
     * @param userId The ID of the user whose tasks are to be retrieved.
     * @return A list of Task entities for the specified user.
     */
    public List<Task> findTasksByUserId(int userId) {
        System.out.println(taskRepository.findByUserID(userId));
        return taskRepository.findByUserID(userId);
    }

    /**
     * Inserts a new task for a specific user.
     *
     * @param userId The ID of the user for whom the task is being added.
     * @param task   The Task entity to be inserted.
     * @return The inserted Task entity.
     */
    public Task insert(int userId, Task task) {
        String title = task.getTitle().toLowerCase();
        String description = task.getDescription().toLowerCase();
        for (String blackListWord : blackList) {
            if (title.contains(blackListWord.toLowerCase()) || description.contains(blackListWord.toLowerCase())) {
                Task _task = new Task();
                _task.setTitle("BLACKLIST");

                UserInfo user = userInfoService.findUserInfoByUserId(userId);
                String text = "A task with title or description containing a blacklisted word has been detected.\n" +
                        "Task Title: " + task.getTitle() + "\n" +
                        "Task Description: " + task.getDescription() + "\n" +
                        "Detected Word: " + blackListWord + "\n" +
                        "User Name: " + (user != null ? user.getName() : "Unknown") + "\n" +
                        "User Email: " + (user != null ? user.getEmail() : "Unknown");

                notificationService.sendNotification(text);

                return _task;
            }
        }

        if(!task.getDueDate().after(task.getStartDate())){
            Task _task = new Task();
            _task.setTitle("DUEDATE");
            return _task;
        }

        task.setUserID(userId);
        return taskRepository.insert(task);
    }

    /**
     * Updates a specific task identified by its ID.
     *
     * @param taskId     The ID of the task to be updated.
     * @param updatedTask The updated Task entity.
     * @return The updated Task entity.
     * @throws EntityNotFoundException if the task with the specified ID is not found.
     */
    public Task updateTask(String taskId, Task updatedTask) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String formattedDate = sdf.format(now);

        return taskRepository.findById(taskId).map(task -> {
            if (updatedTask.getTitle() != null) {
                for (String blackListWord : blackList) {
                    if (updatedTask.getTitle().toLowerCase().contains(blackListWord.toLowerCase())) {
                        task.setTitle("BLACKLIST");
                    }
                }
                if(!task.getTitle().equals("BLACKLIST"))
                    task.setTitle(updatedTask.getTitle());
            }
            if (updatedTask.getDescription() != null) {
                for (String blackListWord : blackList) {
                    if (updatedTask.getDescription().toLowerCase().contains(blackListWord.toLowerCase())) {
                        task.setTitle("BLACKLIST");
                    }
                }
                if(!task.getTitle().equals("BLACKLIST"))
                    task.setDescription(updatedTask.getDescription());
            }
            if (updatedTask.getStartDate() != null) {
                task.setStartDate(updatedTask.getStartDate());
            }
            if (updatedTask.getDueDate() != null) {
                task.setDueDate(updatedTask.getDueDate());
            }

            if((task.getStartDate() != null && task.getDueDate() != null)){
                if(!task.getStartDate().before(task.getDueDate()))
                    task.setTitle("DUEDATE");
            }

            if (updatedTask.getIsCompleted() != null && updatedTask.getIsCompleted()) {
                task.setIsCompleted(updatedTask.getIsCompleted());
                System.out.println("Task-ul a fost completat");
                System.out.println("formattedDate: " + formattedDate);
                badgeService.checkAwarding(task, formattedDate);

            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new EntityNotFoundException("Task not found with id " + taskId));
    }

    /**
     * Deletes a task identified by its ID.
     *
     * @param taskId The ID of the task to be deleted.
     */
    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }

    /**
     * Finds tasks that are due on a specific date.
     *
     * @param dueDate The date for which to find due tasks.
     * @return A list of tasks due on the specified date.
     */
    public List<Task> findTasksByDueDate(LocalDate dueDate) {
        Date dueDateStart = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dueDateEnd = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1).toInstant());

        return taskRepository.findByDueDateBetween(dueDateStart, dueDateEnd);
    }

    /**
     * Retrieves the email of a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return The email of the user.
     */
    public String getUserEmailByUserId(int userId) {
        UserInfo userInfo = userInfoService.findUserInfoByUserId(userId);
        return userInfo.getEmail();
    }



    public List<Task> findTasksByUserIdAndDate(int userId, LocalDate date) {
        List<Task> tasks = findTasksByUserId(userId);
        List<Task> tasksByDate = new ArrayList<>();

        for (Task task : tasks) {
            LocalDate taskStartDate = task.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (taskStartDate.equals(date)) {
                tasksByDate.add(task);
            }
        }

        return tasksByDate;
    }


    public List<Task> findTasksByUserIdAndMonth(String username, int currentMonth) {
        List<Task> tasks = findTasksByUserId(userInfoService.findUserInfoByUsername(username).getId());
        List<Task> tasksByMonth = new ArrayList<>();

        for (Task task : tasks) {
            LocalDate taskStartDate = task.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (taskStartDate.getMonthValue() == currentMonth) {
                tasksByMonth.add(task);
            }
        }

        return tasksByMonth;
    }


    public double calculateProgressValue(List<Task> tasks) {
        // Number of finished tasks in the current month
        long finishedTasksInCurrentMonth = tasks.stream()
                .filter(Task::getIsCompleted)
                .count();

        // Number of total tasks in the current month
        long totalTasksInCurrentMonth = tasks.size();

        if (totalTasksInCurrentMonth > 0) {
            return (double) (finishedTasksInCurrentMonth * 100) / totalTasksInCurrentMonth;
        } else {
            return 0.0;
        }
    }
}
