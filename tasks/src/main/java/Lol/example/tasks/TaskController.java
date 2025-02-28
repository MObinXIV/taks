package Lol.example.tasks;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private  final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/user/{userId}")
    public Tasks createTaskForUser(@PathVariable UUID userId, @RequestBody Tasks task) {
       return  taskService.CreateTask(userId, task);
    }

}
