package com.paw.timemate.project.controllers.taskManagement;

import com.paw.timemate.project.entities.taskManagement.Task;
import com.paw.timemate.project.services.taskManagement.TaskService;
import com.paw.timemate.project.services.userManagement.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


/**
 * REST controller for managing Task entities.
 * This class handles HTTP requests related to tasks, providing endpoints for CRUD operations.
 */
@RestController
@RequestMapping(value = "/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    // Service for task-related operations
    @Autowired
    private TaskService taskService;

    // Service for user-related operations
    @Autowired
    private UserInfoService userService;

    /**
     * Retrieves all tasks associated with a specific user ID.
     *
     * @param userId The ID of the user whose tasks are to be retrieved.
     * @return A ResponseEntity containing a list of tasks and the appropriate HTTP status code.
     */

    @GetMapping("/{userId}/tasks")
    @PreAuthorize("@userAccessService.hasAccess(authentication.principal.username)")
    public ResponseEntity<List<Task>> getAllTasksByUserIdAndDate(
            @PathVariable int userId,
            @RequestParam(name = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<Task> tasks;
        tasks = taskService.findTasksByUserIdAndDate(userId, date);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Creates and adds a new task for a specific user.
     *
     * @param userId The ID of the user for whom the task is being added.
     * @param task The task object to be added.
     * @return A ResponseEntity containing the created task and the HTTP status code.
     */
    @PostMapping("/{userId}/addTask")
    @PreAuthorize("@userAccessService.hasAccess(authentication.principal.username)")
    public ResponseEntity<?> addTask(@PathVariable int userId, @RequestBody Task task) {
        Task _task = taskService.insert(userId, task);
        //if(_task.getTitle().equals("BLACKLIST"))
        //    return new ResponseEntity<>("Title contains blacklisted words! The admin will be notified.", HttpStatus.OK);
        if(_task.getTitle().equals("DUEDATE"))
            return new ResponseEntity<>("Due date can't be before start date!", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(_task, HttpStatus.CREATED);
    }

    /**
     * Updates a specific task identified by its ID.
     *
     * @param taskId The ID of the task to be updated.
     * @param task The task object with updated information.
     * @return A ResponseEntity containing the updated task and the appropriate HTTP status code.
     */
    @PutMapping("/tasks/{taskId}")
    @PreAuthorize("@userAccessService.hasAccess(authentication.principal.username)")
    public ResponseEntity<?> updateTask(@PathVariable String taskId, @RequestBody Task task) {
        try {
            Task updatedTask = taskService.updateTask(taskId, task);
            //if(updatedTask.getTitle().equals("BLACKLIST"))
            //    return new ResponseEntity<>("Title contains blacklisted words! The admin will be notified.", HttpStatus.OK);
            if(updatedTask.getTitle().equals("DUEDATE"))
                return new ResponseEntity<>("Due date can't be before start date!", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a task identified by its ID.
     *
     * @param taskId The ID of the task to be deleted.
     * @return A ResponseEntity indicating the result of the deletion operation.
     */
    @DeleteMapping("/tasks/{taskId}")
    @PreAuthorize("@userAccessService.hasAccess(authentication.principal.username)")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable String taskId) {
        try {
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
