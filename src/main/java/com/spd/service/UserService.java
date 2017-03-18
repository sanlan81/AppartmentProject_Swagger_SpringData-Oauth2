package com.spd.service;

import com.spd.entity.User;
import com.spd.entity.UserEmail;
import com.spd.entity.UserTelephone;
import com.spd.repository.UserEmailRepository;
import com.spd.repository.UserRepository;
import com.spd.repository.UserTelephoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEmailRepository userEmailRepository;

    @Autowired
    private UserTelephoneRepository userTelephoneRepository;

    public User getById(int id) {
        return userRepository.getOne(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public boolean deleteUser(int id) {
        try {
            userRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    public void addExtraEmailByUser(int userId, String email) {
        User user = userRepository.findOne(userId);
        UserEmail userEmail = new UserEmail();
        userEmail.setEmail(email);
        userEmail.setUser(user);
        userEmailRepository.save(userEmail);
    }

    public void deleteExtraEmailByUser(int userId, String email) {
        UserEmail userEmail = userEmailRepository.findByUserIdAndEmail(userId, email);
        userEmailRepository.delete(userEmail.getId());
    }

    public void addExtraTelephoneByUser(int userId, String telephone) {
        User user = userRepository.findOne(userId);
        UserTelephone userTelephone = new UserTelephone();
        userTelephone.setTelephone(telephone);
        userTelephone.setUser(user);
        userTelephoneRepository.save(userTelephone);
    }

    public void deleteExtraTelephoneByUser(int userId, String telephone) {
        UserTelephone userTelephone = userTelephoneRepository.findByUserIdAndTelephone(userId, telephone);
        userTelephoneRepository.delete(userTelephone.getId());
    }

    public User findByEmail(String email) {

        List<User> users = (List<User>) userRepository.findByEmail(email);

        if(users.size() !=  1){
            return null;
        }
        return users.get(0);
    }
}
