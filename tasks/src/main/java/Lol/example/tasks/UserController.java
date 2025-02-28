package Lol.example.tasks;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public User addUser(@RequestBody User user) {
       return userService.addNewUser(user);
    }

    @GetMapping("/")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") UUID id)
    {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") UUID id,@RequestBody User user)
    {
        return userService.patchUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") UUID id)
    {
        userService.deleteUser(id);
    }
}
