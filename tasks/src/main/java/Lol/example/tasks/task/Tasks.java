package Lol.example.tasks.task;


import Lol.example.tasks.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="Tasks")
@Table (name="tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private boolean completed = false;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false
    , referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_tasks_fk")
    )
    private User user;

}
