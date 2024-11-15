package com.elearning.haui.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.haui.domain.dto.ResultPaginationDTO;
import com.elearning.haui.domain.entity.User;
import com.elearning.haui.exception.IdInvalidException;
import com.elearning.haui.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class UserAPI {

    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    // Lấy danh sách người dùng, phân trang
    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @RequestParam(value = "current", defaultValue = "1") String currentOptional,
            @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeOptional) throws IdInvalidException {

        int current = Integer.parseInt(currentOptional);
        int pageSize = Integer.parseInt(pageSizeOptional);

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(pageable));
    }

    // lấy người dùng theo id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.getUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("Người dùng không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    // Cập nhập người dùng
    @PutMapping("/users/{id}")
    public String updateUserById(@PathVariable String id, @RequestBody String entity) {

        return entity;
    }

    // Xoá người dùng theo id
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id)
            throws IdInvalidException {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new IdInvalidException("Người dùng không tồn tại");
        }

        userService.handleDeleteUser(id);
        return ResponseEntity.ok("Successfully deleted user");
    }

}
