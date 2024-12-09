package org.example.testlab5.repositories;

import org.example.testlab5.model.Task;
import org.example.testlab5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findByTitle(String title);
    List<Task> findAllByUser(User user);
}
