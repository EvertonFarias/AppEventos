package com.example.inovaTest.controllers;

import com.example.inovaTest.models.EventModel;
import com.example.inovaTest.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventModel> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable Long id) {
        Optional<EventModel> event = eventService.findById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventModel> createEvent(@RequestBody EventModel event) {
        EventModel savedEvent = eventService.save(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventModel> updateEvent(@PathVariable Long id, @RequestBody EventModel event) {
        if (!eventService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        event.setId(id);
        EventModel updatedEvent = eventService.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint separado para upload de imagem do evento
    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadEventPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String path = eventService.saveEventPhoto(id, file);
            return ResponseEntity.ok("Imagem enviada com sucesso! Caminho: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar imagem: " + e.getMessage());
        }
    }
}
