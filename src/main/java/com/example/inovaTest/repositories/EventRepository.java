package com.example.inovaTest.repositories;

import com.example.inovaTest.models.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventModel, Long> {
    // Retorna todos os eventos ordenados pela data (mais próxima primeiro)
    java.util.List<EventModel> findAllByOrderByDateAsc();
    // Retorna todos os eventos ordenados pela data (mais recente primeiro)
    java.util.List<EventModel> findAllByOrderByDateDesc();
}
