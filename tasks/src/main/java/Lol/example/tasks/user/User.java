package Lol.example.tasks.user;


import Lol.example.tasks.task.Tasks;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "AppUser")
@Table(name = "app_user",
uniqueConstraints = {
        @UniqueConstraint(name = "user_unique_email", columnNames = "email")
}
)
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)

    private UUID id = UUID.randomUUID();  // Ensure UUID is generated
//    private UUID id;
    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;
    @Column(

            name="email",
            nullable = false,
            columnDefinition = "TEXT",
            updatable = false
    )
    private  String email;
    @Column(name = "password", nullable = false, length = 255)
    private String password;  // Store hashed passwords

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Tasks> tasks;

}
