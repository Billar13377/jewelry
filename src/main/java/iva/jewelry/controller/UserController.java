package iva.jewelry.controller;

import iva.jewelry.model.User;
import iva.jewelry.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/me")
    public User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.findByEmail(userDetails.getUsername());
}

    @GetMapping("/admin/users")
    public List<User> getAllUsers() {
        return userService.allUsers();
    }

    @GetMapping("/admin/users/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/admin/users/{id}/roles/add")
    public User addRole(@PathVariable Integer id,
                        @RequestParam String roleName) {
        return userService.addRoleToUser(id, roleName);
    }

    @PutMapping("/admin/users/{id}/roles/remove")
    public User removeRole(@PathVariable Integer id,
                           @RequestParam String roleName) {
        return userService.removeRoleFromUser(id, roleName);
    }

    @DeleteMapping("/admin/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}