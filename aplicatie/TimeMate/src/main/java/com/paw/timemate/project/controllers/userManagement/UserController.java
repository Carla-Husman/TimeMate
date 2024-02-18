package com.paw.timemate.project.controllers.userManagement;


import com.paw.timemate.project.entities.taskManagement.Task;
import com.paw.timemate.project.entities.userManagement.AuthRequest;
import com.paw.timemate.project.entities.userManagement.UserInfo;
import com.paw.timemate.project.entities.userManagement.UserProfileUpdateRequest;
import com.paw.timemate.project.services.userManagement.UserAccessService;
import com.paw.timemate.project.services.taskManagement.BadgeService;
import com.paw.timemate.project.services.taskManagement.TaskService;
import com.paw.timemate.project.services.userManagement.JwtService;
import com.paw.timemate.project.services.userManagement.UserInfoService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * This class is responsible for handling all the requests related to user authentication and authorization.
 */
@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    // Injecting the service for managing user information
    @Autowired
    private UserInfoService service;

    // Injecting the service for managing tasks
    @Autowired
    private TaskService taskService;

    // Injecting the service for managing badges
    @Autowired
    private BadgeService badgeService;

    // Injecting the JWT service
    @Autowired
    private JwtService jwtService;

    // Injecting the authentication manager
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAccessService userAccessService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Endpoint for starting the application.
     *
     * @return A simple welcome message.
     */
    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        return new ResponseEntity<>("Welcome this endpoint is not secure", HttpStatus.OK);
    }


    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody UserInfo userInfo) {
        String addUserStatus = service.addUser(userInfo);

        if(addUserStatus.equals("Invalid Username")){
            return new ResponseEntity<>(addUserStatus + ". Please use only letters, digits, dots, or underscores, and ensure the length is between 7 and 14 characters.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(addUserStatus.equals("Invalid Password")){
            return new ResponseEntity<>(addUserStatus + ". Please ensure it is between 8 and 20 characters long, contains at least one lowercase letter, and at least one digit.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(addUserStatus.equals("Invalid Email")){
            return new ResponseEntity<>(addUserStatus + ". Please use a valid email format.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(addUserStatus.equals("Already Exists")){
            return new ResponseEntity<>(addUserStatus, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(addUserStatus, HttpStatus.CREATED);
    }

    // Endpoint for the user profile, accessible only by users with the 'ROLE_USER' role
    // Endpoint for the user profile, accessible only by users with the 'ROLE_USER' role
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER') && @userAccessService.hasAccess(authentication.principal.username)")
    public Map<String, Object> userProfile(Authentication authentication) {
        String username = authentication.getName();
        UserInfo user = service.findUserInfoByUsername(username);
        user.setPassword(null);
        user.setRoles(null);

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        List<Task> tasks = taskService.findTasksByUserIdAndMonth(username, currentMonth);

        //String currentBadgeName = badgeService.getCurrentBadgeName(user.getStreak());

        double progress = taskService.calculateProgressValue(tasks);
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedProgress = df.format(progress);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        //response.put("currentBadgeName", currentBadgeName);
        response.put("progress", formattedProgress);

        return response;
    }

    @PutMapping("/user/updateProfile")
    @PreAuthorize("hasAuthority('ROLE_USER') && @userAccessService.hasAccess(authentication.principal.username)")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody UserProfileUpdateRequest updateRequest) {
        System.out.println(updateRequest.getNewUsername());
        System.out.println(updateRequest.getNewEmail());
        System.out.println(updateRequest.getCurrentPassword());
        String currentUsername = authentication.getName();
        UserInfo currentUser = service.findUserInfoByUsername(currentUsername);

        boolean isPasswordCorrect = passwordEncoder.matches(updateRequest.getCurrentPassword(), currentUser.getPassword());
        if (!isPasswordCorrect) {
            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        if (updateRequest.getNewUsername() != null && !updateRequest.getNewUsername().isEmpty()) {
            currentUser.setName(updateRequest.getNewUsername());
        }

        if (updateRequest.getNewEmail() != null && !updateRequest.getNewEmail().isEmpty()) {
            currentUser.setEmail(updateRequest.getNewEmail());
        }

        service.save(currentUser);
        return new ResponseEntity<>("Profile updated successfully", HttpStatus.OK);
    }


    // Endpoint for the admin profile, accessible only by admins with the 'ROLE_ADMIN' role
    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> adminProfile() {
        List<UserInfo> users = service.findAllUsers();

        users.removeIf(user -> user.getName().contains("admin"));

        users.forEach(user -> {
            user.setPassword(null);
            user.setRoles(null);
        });
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/admin/blockUser/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> blockUser(@PathVariable int userId) {
        try {
            UserInfo user = service.findUserInfoByUserId(userId);
            if (user != null) {
                user.setAccess("BLOCKED");
                service.save(user);
                return new ResponseEntity<>("User successfully blocked", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error blocking user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint for generating the JWT token after authentication
    @PostMapping("/generateToken")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        // Authenticating the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (!userAccessService.hasAccess(authRequest.getUsername())) {
            return new ResponseEntity<>("YOU ARE BLOCKED FROM TIMEMATE! PLEASE CONTACT THE ADMINISTRATOR!", HttpStatus.FORBIDDEN);
        }

        // Generating and returning the JWT token if authentication is successful
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(authRequest.getUsername()), HttpStatus.OK);
        } else {
            // Throws an exception if user information is invalid
            return new ResponseEntity<>("Invalid User Request", HttpStatus.BAD_REQUEST);
            //throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @GetMapping("/validateToken/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        try {
            if (jwtService.isTokenExpired(token)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}