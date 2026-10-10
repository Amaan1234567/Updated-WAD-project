package com.users_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.util.Optional;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-29T13:45:58.449049799+05:30[Asia/Kolkata]", comments = "Generator version: 7.21.0")
@Controller
@RequestMapping("${openapi.users.base-path:/v1}")
public class UsersApiController implements UsersApi {

    private final NativeWebRequest request;

    @Autowired
    UsersService usersService;

    @Autowired
    public UsersApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    public ResponseEntity<Void> usersAddPost(@Parameter(name = "NewUserApi", description = "", required = true) @Valid @RequestBody NewUserApi newUserApi) {
        try {
            usersService.createUserApi(newUserApi);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JpaSystemException e) {
            if (e.getRootCause().getMessage().contains("new row violates row-level security policy for table")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }

    public ResponseEntity<Void> usersPut(
            @Parameter(name = "UserUpdate", description = "", required = true) @Valid @RequestBody UserUpdate userUpdate) {
        try {
            usersService.updateUser(userUpdate);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JpaSystemException e) {
            if (e.getRootCause().getMessage().contains("new row violates row-level security policy for table")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public ResponseEntity<Void> usersRegisterPost(
            @Parameter(name = "NewUser", description = "", required = true) @Valid @RequestBody NewUser newUser) {
        try {
            usersService.createUser(newUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JpaSystemException e) {
            if (e.getRootCause().getMessage().contains("new row violates row-level security policy for table")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public ResponseEntity<Void> usersUserIdDelete(@Parameter(name = "user_id", description = "Numeric id of user that needs to be deleted", required = true, in = ParameterIn.PATH) @PathVariable("user_id") Long userId) {
        try {
            usersService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JpaSystemException e) {
            if (e.getRootCause().getMessage().contains("new row violates row-level security policy for table")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public ResponseEntity<User> usersUserIdGet(
            @Parameter(name = "user_id", description = "Numeric id of user that needs to be fetched", required = true, in = ParameterIn.PATH) @PathVariable("user_id") Long userId) {
        try {
            User user = usersService.getUser(userId);
            return ResponseEntity.ok(user);
        } catch (JpaSystemException e) {
            if (e.getRootCause().getMessage().contains("new row violates row-level security policy for table")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
