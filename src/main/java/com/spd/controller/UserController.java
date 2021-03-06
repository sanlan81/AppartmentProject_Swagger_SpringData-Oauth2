package com.spd.controller;

import com.spd.bean.ImageBean;
import com.spd.bean.UserInformationBean;
import com.spd.bean.UserRegistrationBean;
import com.spd.entity.User;
import com.spd.exception.AuthenticationUserException;
import com.spd.exception.UserAuthenticationException;
import com.spd.mapper.ObjectMapper;
import com.spd.service.CheckService;
import com.spd.service.RegistrationService;
import com.spd.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
@Api(value = "users")
public class UserController {

    private final UserService userService;
    private final RegistrationService registrationService;
    private final ObjectMapper objectMapper;
    private final CheckService checkService;

    @Autowired
    public UserController(UserService userService, RegistrationService registrationService, ObjectMapper objectMapper, CheckService checkService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.objectMapper = objectMapper;
        this.checkService = checkService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "create update user", httpMethod = "POST")
    public ResponseEntity<String> createUser(Authentication authentication, @RequestBody UserRegistrationBean userRegistrationBean) {
        Optional.ofNullable(authentication)
                .ifPresent(auth -> {throw new AuthenticationUserException("You can not create user!");});
        checkService.checkUserTerm(userRegistrationBean);
        User user = userService.createUser(userRegistrationBean);
        registrationService.registration(user);
        return new ResponseEntity<>("Message send", HttpStatus.OK);
    }

    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    @ApiOperation(value = "change password", httpMethod = "POST")
    public void changePassword(Authentication authentication, @RequestBody String password) {
        userService.changePassword(authentication.getName(), password);
    }

    @RequestMapping(value = "/verify/{token}", method = RequestMethod.POST)
    @ApiOperation(value = "verify user", httpMethod = "POST")
    public void verificationUser(@PathVariable("token") String token) {
        registrationService.verification(token);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ApiOperation(value = "create update user", httpMethod = "PUT")
    public void updateUser(Authentication authentication, @RequestBody UserInformationBean userInformationBean) {
        userService.updateUser(authentication.getName(), userInformationBean);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "get user", httpMethod = "GET")
    public UserInformationBean getUser(Authentication authentication) {
        User user = userService.getByEmail(authentication.getName());
        return objectMapper.map(user, UserInformationBean.class);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ApiOperation(value = "delete user", httpMethod = "DELETE")
    public void deleteUser(Authentication authentication) {
        userService.deleteUser(authentication.getName());
    }

    @RequestMapping(value = "/image", method = RequestMethod.PUT)
    @ApiOperation(value = "set image", httpMethod = "PUT")
    public void setUserImage(Authentication authentication , @RequestBody ImageBean imageBean) {
        userService.setImage(authentication.getName(), imageBean.getId());
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    @ApiOperation(value = "get image", httpMethod = "GET")
    public String getUserImage(Authentication authentication ) {
       return userService.getImageUrl(authentication.getName());
    }
}
