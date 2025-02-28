package Lol.example.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
