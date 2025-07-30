package com.example.inovaTest.repositories;

import com.example.inovaTest.models.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventModel, Long> {
}
