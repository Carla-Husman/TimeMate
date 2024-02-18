package com.paw.timemate.project.services.userManagement;
import com.paw.timemate.project.entities.userManagement.UserInfo;

import com.paw.timemate.project.repositories.userManagement.UserInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.*;

/**
 * This class is used to perform CRUD operations on the database.
 */
@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = repository.findByName(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public String addUser(UserInfo userInfo) {

        //Verify that all userInfo is valid
        String regexUsername = "^[a-zA-Z0-9._]{7,14}$";
        String regexEmail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$";
        String regexPassword = "^(?=[^a-z]*[a-z])(?=\\D*\\d)[^:&~\\s]{8,20}$";

        Pattern patternUsername = Pattern.compile(regexUsername);
        Pattern patternEmail = Pattern.compile(regexEmail);
        Pattern patternPassword = Pattern.compile(regexPassword);

        Matcher matcherUsername = patternUsername.matcher(userInfo.getName());
        Matcher matcherEmail = patternEmail.matcher(userInfo.getEmail());
        Matcher matcherPassword = patternPassword.matcher(userInfo.getPassword());

        if(!matcherUsername.matches())
            return "Invalid Username";

        if(!matcherPassword.matches())
            return "Invalid Password";

        if(!matcherEmail.matches())
            return "Invalid Email";

        //Verify if the user isn't already in the database => username and email unique

        List<UserInfo> userInfoList = repository.findAll();
        for(UserInfo uInfo : userInfoList){
            if(uInfo.getName().equals(userInfo.getName()) || uInfo.getEmail().equals(userInfo.getEmail())){
                return "Already Exists";
            }
        }

        //All data is valid, we can create the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));

        repository.save(userInfo);
        return "User Added Successfully";
    }

    public UserInfo findUserInfoByUserId(int userId) {
        return repository.findById(userId).orElse(null);
    }

    public UserInfo findUserInfoByUsername(String username) {
        return repository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public List<UserInfo> findAllUsers() {
        return repository.findAll();
    }

    public void updateUserInfo(UserInfo userInfo) {
        repository.save(userInfo);
    }

    public void deleteUserInfoByUsername(String username){
        if(repository.findByName(username).isPresent())
            repository.delete(findUserInfoByUsername(username));
    }

    public void save(UserInfo user) {
        repository.save(user);
    }
}