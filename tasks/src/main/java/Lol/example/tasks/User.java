package Lol.example.tasks;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
            columnDefinition = "TEXT"
    )
    private  String email;
    @Column(name = "password", nullable = false, length = 255)
    private String password;  // Store hashed passwords

}
