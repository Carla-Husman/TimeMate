package com.paw.timemate.project.services.userManagement;

import com.paw.timemate.project.entities.userManagement.UserInfo;
import com.paw.timemate.project.repositories.userManagement.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserInfoServiceTest {

    // Declare objects of type UserInfoService, PasswordEncoder and UserInfoRepository
    @Autowired
    private UserInfoService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserInfoRepository userInfoRepository;

    // Test for adding a user with a too short username
    @Test
    void addInvalidUsername1User() {
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("short");
        UserInfo invalidUser = new UserInfo(1, "short", "email@email.com", "password1", 0, new Date(), "", "","Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Username", addUserStatus);
    }

    // Test for adding a user with a username that contains spaces
    @Test
    void addInvalidUsername2User() {
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("sho rt");
        UserInfo invalidUser = new UserInfo(1, "sho rt", "email@email.com", "password1", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Username", addUserStatus);
    }

    // Test for adding a user with a username starting with an invalid character
    @Test
    void addInvalidUsername3User() {
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("@short");
        UserInfo invalidUser = new UserInfo(1, "@short", "email@email.com", "password1", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Username", addUserStatus);
    }

    // Test for adding a duplicate user (with identical username and email)
    @Test
    void addInvalidUsername4User() {
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("testtest");
        UserInfo user1 = new UserInfo(1, "testtest", "email@email.com", "password1", 0, new Date(), "", "", "Starter");
        UserInfo user2 = new UserInfo(1, "testtest", "email@email.com", "password2", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(user1);
        addUserStatus = userService.addUser(user2);
        assertEquals("Already Exists", addUserStatus);
    }

    // Test for adding a user with a too short password
    @Test
    void addInvalidPassword1User(){
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("username");
        UserInfo invalidUser = new UserInfo(1, "username", "email@email.com", "password", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Password", addUserStatus);
    }

    // Test for adding a user with a password that contains spaces
    @Test
    void addInvalidPassword2User(){
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("username");
        UserInfo invalidUser = new UserInfo(1, "username", "email@email.com", "pass word", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Password", addUserStatus);
    }

    // Test for adding a user with a password that does not meet the criteria
    @Test
    void addInvalidPassword3User(){
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("username");
        UserInfo invalidUser = new UserInfo(1, "username", "email@email.com", "p1", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Password", addUserStatus);
    }

    // Test for adding a user with an invalid email address (space before '@')
    @Test
    void addInvalidEmail1User(){
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("username");
        UserInfo invalidUser = new UserInfo(1, "username", "em ail@email.com", "password1", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Email", addUserStatus);
    }

    // Test for adding a user with an invalid email address (no '@' character)
    @Test
    void addInvalidEmail2User(){
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("username");
        UserInfo invalidUser = new UserInfo(1, "username", "emailemail.com", "password1", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Email", addUserStatus);
    }

    // Test for adding a user with an invalid email address (no characters before '@')
    @Test
    void addInvalidEmail3User(){
        userService = new UserInfoService();
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("username");
        UserInfo invalidUser = new UserInfo(1, "username", "@email.com", "password1", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(invalidUser);
        assertEquals("Invalid Email", addUserStatus);
    }

    // Test for adding a valid user
    @Test
    void addValidUser(){
        userService = new UserInfoService();
        // Set the private 'encoder' and 'repository' fields in UserInfoService using ReflectionTestUtils
        ReflectionTestUtils.setField(userService, "repository", userInfoRepository);
        ReflectionTestUtils.setField(userService, "encoder", encoder);
        userService.deleteUserInfoByUsername("newuser");
        UserInfo validUser = new UserInfo(1, "newuser", "email@email.com", "password1", 0, new Date(), "", "", "Starter");
        String addUserStatus = userService.addUser(validUser);
        assertEquals("User Added Successfully", addUserStatus);
    }
}
