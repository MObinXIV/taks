package Lol.example.tasks.user;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById( UUID id) {
        return userRepository.findById(id);
    }
    public User addNewUser( User user) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists");
        }
        return userRepository.save(user);
    }
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(UUID id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }
    @Transactional
    public User patchUser(UUID id, User updatedFields) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

        // Prevent email update
        if (updatedFields.getEmail() != null && !updatedFields.getEmail().equals(existingUser.getEmail())) {
            throw new UnsupportedOperationException("Email cannot be updated after creation");
        }

        if (updatedFields.getFirstName() != null && !updatedFields.getFirstName().isEmpty()) {
            existingUser.setFirstName(updatedFields.getFirstName());
        }

        if (updatedFields.getLastName() != null && !updatedFields.getLastName().isEmpty()) {
            existingUser.setLastName(updatedFields.getLastName());
        }

        if (updatedFields.getPassword() != null && !updatedFields.getPassword().isEmpty()) {
            existingUser.setPassword(updatedFields.getPassword());
        }

        return userRepository.save(existingUser);
    }

//
//    void addTasks(Tasks task)
//    {
//
//    }

}
