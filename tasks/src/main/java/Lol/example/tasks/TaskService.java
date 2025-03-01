package Lol.example.tasks;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private  final TaskRepository taskRepository;
    private  final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

//    public List<Tasks> getTasks() {
//        return taskRepository.findAll();
//    }

    // find the tasks for the user
    public List<Tasks> getTaskForUser(UUID userId)
    {
        return taskRepository.findByUserId(userId);
    }

    public Tasks CreateTask(UUID userId , Tasks task){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // assign the task to the specific user
        task.setUser(user);

        return taskRepository.save(task);
    }

    @Transactional
    public Tasks patchTask(UUID userId, UUID taskId, Tasks task) {
        // Fetch task ensuring it belongs to the user in one query
        Tasks existingTask = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for the user"));

        // Update only if there are changes
        boolean isUpdated = false;

        if (task.getTitle() != null && !task.getTitle().isBlank() && !task.getTitle().equals(existingTask.getTitle())) {
            existingTask.setTitle(task.getTitle());
            isUpdated = true;
        }

        if (task.getDescription() != null && !task.getDescription().isBlank() && !task.getDescription().equals(existingTask.getDescription())) {
            existingTask.setDescription(task.getDescription());
            isUpdated = true;
        }

        // Return without saving if no changes
        return isUpdated ? taskRepository.save(existingTask) : existingTask;
    }


    public void deleteTask(UUID userId, UUID taskId) {
      Tasks existingTask =taskRepository.findByIdAndUserId(taskId, userId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for the user"));
      taskRepository.delete(existingTask);

    }

}
