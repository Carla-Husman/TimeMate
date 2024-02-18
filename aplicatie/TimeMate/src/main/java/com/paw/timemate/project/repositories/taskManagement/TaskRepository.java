package com.paw.timemate.project.repositories.taskManagement;

import com.paw.timemate.project.entities.taskManagement.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Repository interface for 'Task' entities.
 * Extends MongoRepository to provide standard CRUD operations for Task documents in MongoDB.
 * Additional custom query methods are defined for specific task queries.
 */
public interface TaskRepository extends MongoRepository<Task, String> {
    /**
     * Finds tasks by the user ID.
     *
     * @param userID The ID of the user whose tasks are to be retrieved.
     * @return A list of tasks belonging to the specified user.
     */
    List<Task> findByUserID(int userID);

    /**
     * Finds tasks by their due date.
     *
     * @param dueDate The specific due date of the tasks.
     * @return A list of tasks that are due on the specified date.
     */
    List<Task> findByDueDate(LocalDate dueDate);

    /**
     * Finds tasks whose due dates fall within a specified date range.
     *
     * @param dueDateStart The start date of the range.
     * @param dueDateEnd The end date of the range.
     * @return A list of tasks whose due dates are between the specified start and end dates.
     */
    List<Task> findByDueDateBetween(Date dueDateStart, Date dueDateEnd);
}