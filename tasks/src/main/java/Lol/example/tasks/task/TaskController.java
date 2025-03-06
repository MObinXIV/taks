package Lol.example.tasks.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/user/{userId}")
    public List<Tasks> getAllTasksForUser(@PathVariable UUID userId) {

        return taskService.getTaskForUser(userId);
    }

    @PatchMapping("/user/{userId}/tasks/{taskId}")
    public Tasks updateTask(
            @PathVariable UUID userId,
            @PathVariable UUID taskId,
            @RequestBody Tasks task) {
        return taskService.patchTask(userId, taskId, task);
    }

    @DeleteMapping("/user/{userId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID userId, @PathVariable UUID taskId) {
        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content (successful deletion)
    }
}
