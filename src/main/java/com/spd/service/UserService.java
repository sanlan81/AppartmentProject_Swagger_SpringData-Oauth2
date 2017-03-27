package com.spd.service;

import com.spd.bean.UserInformationBean;
import com.spd.bean.UserRegistrationBean;
import com.spd.entity.Image;
import com.spd.entity.User;
import com.spd.entity.UserToken;
import com.spd.mapper.ObjectMapper;
import com.spd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService /*implements UserDetailsService*/ {

    private final ObjectMapper objectMapper;
    private final UserTokenService userTokenService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Autowired
    public UserService(ObjectMapper objectMapper, UserTokenService userTokenService, EmailService emailService, UserRepository userRepository, ImageService imageService) {
        this.objectMapper = objectMapper;
        this.userTokenService = userTokenService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    /*@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findOneByEmail(email);

        return userOptional.map(user ->
                new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getActive(),
                true,
                true,
                true,
                getAuthorities()
        )).orElseGet(() -> getErrorUser(email));
    }

    private org.springframework.security.core.userdetails.User getErrorUser(String email) {
        return new org.springframework.security.core.userdetails.User(
                email,
                "",
                false,
                false,
                false,
                false,
                getAuthorities()
        );
    }*/

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    public User getById(int id) {
        return userRepository.getOne(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String email) {
        Optional<User> userOptional = userRepository.findOneByEmail(email);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get().getId());
        }
        else {
            // TODO
        }
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public void updateUser(Optional<String> emailOptional, UserInformationBean userInformationBean) {
        User newUser = emailOptional
                .flatMap(userRepository::findOneByEmail)
                .map(user -> updateUserWithUserRegistrationBean(user, userInformationBean))
                .orElseGet(() -> objectMapper.map(userInformationBean, User.class));
        saveUser(newUser);
    }

    public void saveUser(Optional<String> emailOptional, UserRegistrationBean userRegistrationBean) {
        Optional<User> userOptional = emailOptional
                .flatMap(userRepository::findOneByEmail);
        if (userOptional.isPresent()) {
            saveUser(userOptional.get());
        }
        else {
            // TODO
        }
    }

    private User updateUserWithUserRegistrationBean(User user, UserInformationBean userInformationBean) {
        user.setFirstName(userInformationBean.getFirstName());
        user.setLastName(userInformationBean.getLastName());
        user.setPassword(userInformationBean.getPassword());
        return user;
    }

    public User saveUser(UserRegistrationBean userRegistrationBean) {
        User user = objectMapper.map(userRegistrationBean, User.class);
        return saveUser(user);
    }


    public void setImage(int id, int imageId) {
        User user = getById(id);
        Image image = imageService.getImage(imageId);
        user.setImage(image);
        userRepository.save(user);
    }

    public void registration(User user) throws MessagingException {
        String token = userTokenService.createToken();
        userTokenService.saveToken(user, token);

        String link = emailService.createLink(token);
        emailService.sendMail(user.getEmail(), link);
    }

    public void verification(String token) {
        Optional<UserToken> userTokenOptional = userTokenService.getUserTokenByToken(token);
        if (userTokenOptional.isPresent()) {
            User user = userTokenOptional.get().getUser();
            user.setActive(true);
            userRepository.save(user);
        }
    }

}
