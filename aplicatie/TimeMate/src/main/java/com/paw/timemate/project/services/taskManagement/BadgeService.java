package com.paw.timemate.project.services.taskManagement;

import com.paw.timemate.project.entities.taskManagement.Badge;
import com.paw.timemate.project.entities.taskManagement.Task;
import com.paw.timemate.project.entities.userManagement.UserInfo;
import com.paw.timemate.project.repositories.taskManagement.BadgeRepository;
import com.paw.timemate.project.repositories.userManagement.UserInfoRepository;
import com.paw.timemate.project.services.userManagement.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling operations related to 'Badge' entities.
 * This class acts as an intermediary between the repository layer and the controller,
 * providing higher-level business logic and database interactions.
 */
@Service
public class BadgeService {
    // Injects the BadgeRepository automatically by Spring.

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserInfoService userInfoService;


    /**
     * Retrieves all badges stored in the database.
     *
     * @return A list of all Badge entities.
     */
    public List<Badge> findAll() {
        return badgeRepository.findAll();
    }


    /**
     * Retrieves a badge by its ID.
     *
     * @param id The ID of the badge to be retrieved.
     * @return The Badge entity if found, or null if no badge is found with the given ID.
     */
    public Badge findById(String id) {
        Optional<Badge> badge = badgeRepository.findById(id);
        return badge.orElse(null);
    }

    public Badge findByBadgeName(String badgeName){
        Optional<Badge> badge = badgeRepository.findByName(badgeName);
        return badge.orElse(null);
    }

    public String getCurrentBadgeName(int streak) {
        List<Badge> badges = badgeRepository.findAllByOrderByNecessaryPointsDesc();
        for (Badge badge : badges) {
            if (streak >= badge.getNecessaryPoints()) {
                return badge.getName();
            }
        }
        return "Starter";
    }

    public void checkAwarding(Task task, String formattedDate) {
        checkAndAwardDeadlineDuelistBadge(task, formattedDate);
        checkAndAwardFlashFinisherBadge(task);
        checkAndAwardWeekendWarriorBadge(task);
        checkAndAwardMarathonerBadge(task);
    }

    public void checkAndAwardDeadlineDuelistBadge(Task task, String formattedDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String taskDueDateFormatted = sdf.format(task.getDueDate());

        if (taskDueDateFormatted.equals(formattedDate)) {
            UserInfo userInfo = userInfoService.findUserInfoByUserId(task.getUserID());
            System.out.println(userInfo);
            if(!userInfo.getBadges().contains("DeadlineDuelist")) {
                userInfo.AddBadge("DeadlineDuelist");
                userInfo.setStreak(userInfo.getStreak() + 85);
                userInfoService.updateUserInfo(userInfo);
            }
        }
    }
    public void checkAndAwardFlashFinisherBadge(Task task) {
        if (task.getStartDate() != null && task.getDueDate() != null) {
            long duration = task.getDueDate().getTime() - task.getStartDate().getTime();
            long oneHourInMillis = 3600000;

            if (duration <= oneHourInMillis) {
                UserInfo userInfo = userInfoService.findUserInfoByUserId(task.getUserID());
                if (userInfo != null && !userInfo.getBadges().contains("FlashFinisher")) {
                    userInfo.AddBadge("FlashFinisher");
                    userInfo.setStreak(userInfo.getStreak() + 85); 
                    userInfoService.updateUserInfo(userInfo);
                }
            }
        }
    }

    public void checkAndAwardWeekendWarriorBadge(Task task) {
        if (task.getStartDate() != null && task.getDueDate() != null) {
            // Verifică dacă ambele date sunt în weekend
            if (isDateInWeekend(task.getStartDate()) && isDateInWeekend(task.getDueDate())) {
                UserInfo userInfo = userInfoService.findUserInfoByUserId(task.getUserID());
                if (userInfo != null && !userInfo.getBadges().contains("Weekend Warrior")) {
                    userInfo.AddBadge("Weekend Warrior");
                    userInfo.setStreak(userInfo.getStreak() + 80);
                    userInfoService.updateUserInfo(userInfo);
                }
            }
        }
    }

    public void checkAndAwardMarathonerBadge(Task task) {
        if (task.getStartDate() != null && task.getDueDate() != null) {
            long duration = task.getDueDate().getTime() - task.getStartDate().getTime();
            long sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000; // 7 zile în milisecunde

            if (duration > sevenDaysInMillis) {
                UserInfo userInfo = userInfoService.findUserInfoByUserId(task.getUserID());
                if (userInfo != null && !userInfo.getBadges().contains("Marathoner")) {
                    userInfo.AddBadge("Marathoner");
                    userInfo.setStreak(userInfo.getStreak() + 90); // Puncte pentru "Marathoner"
                    userInfoService.updateUserInfo(userInfo);
                }
            }
        }
    }

    private boolean isDateInWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

}
