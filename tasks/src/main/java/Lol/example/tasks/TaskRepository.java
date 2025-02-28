package Lol.example.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, UUID> {
//    List <Tasks> findByUserId(UUID userId);

    @Query("SELECT t FROM Tasks t WHERE t.user.id = :userId")
    List<Tasks> findByUserId(@Param("userId") UUID userId);
}
