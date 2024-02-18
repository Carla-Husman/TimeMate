package com.paw.timemate.project.services.userManagement;

import com.paw.timemate.project.entities.userManagement.UserInfo;
import com.paw.timemate.project.exceptions.userManagement.AccessBlockedException;
import com.paw.timemate.project.services.userManagement.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccessService {

    @Autowired
    private UserInfoService userInfoService;

    public boolean hasAccess(String username) {
        UserInfo user = userInfoService.findUserInfoByUsername(username);
        if (user != null && "BLOCKED".equals(user.getAccess())) {
            throw new AccessBlockedException("YOU ARE BLOCKED FROM TIMEMATE! PLEASE CONTACT THE ADMINISTRATOR!");
        }
        return true;
    }
}
