package com.elearning.haui.api;

import com.elearning.haui.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/users")
public interface UserApi {

    @GetMapping
    List<User> getList();

    @PostMapping
    ResponseEntity<User> createNewUser(@RequestBody User user);

    @DeleteMapping("/{userId}")
    ResponseEntity<?> delUserById(@PathVariable long userId);

    @GetMapping("/{userId}")
    ResponseEntity<User> getUserById(@PathVariable long userId);

    @PutMapping
    ResponseEntity<User> updateUser(@RequestBody User reqUser);
}
