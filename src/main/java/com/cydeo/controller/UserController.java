package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getUsers() {

        return ResponseEntity
                .ok(new ResponseWrapper("All users retrieved",
                        userService.listAllUsers(), HttpStatus.OK));
           // ^^ .ok shows OK Status in Postman tab, ^^ this also adds it in the Response Body
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> getUserByName(@PathVariable("username") String username) {

        return ResponseEntity
                .ok(new ResponseWrapper("User retrieved",
                        userService.findByUserName(username), HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user) {

        userService.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseWrapper("User created", HttpStatus.CREATED));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) {

         userService.update(user);
         return ResponseEntity
                 .ok(new ResponseWrapper("User updated", HttpStatus.OK));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) {

        userService.delete(username);
        return ResponseEntity
                .ok(new ResponseWrapper("User deleted", HttpStatus.OK));
    }
}
