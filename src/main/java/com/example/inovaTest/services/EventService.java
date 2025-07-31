package com.example.inovaTest.services;

import com.example.inovaTest.models.EventModel;
import com.example.inovaTest.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<EventModel> findAll() {
        return eventRepository.findAllByOrderByDateDesc();
    }

    public Optional<EventModel> findById(Long id) {
        return eventRepository.findById(id);
    }

    public EventModel save(EventModel event) {
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    // Salva imagem do evento, gera nome único e atualiza photoPath
    public String saveEventPhoto(Long eventId, MultipartFile file) throws IOException {
        Optional<EventModel> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Evento não encontrado");
        }
        String uploadDir = System.getProperty("user.dir") + java.io.File.separator + "uploads" + java.io.File.separator + "event-photos";
        Files.createDirectories(Paths.get(uploadDir));
        String ext = "";
        String originalName = file.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf('.'));
        }
        String uniqueName = UUID.randomUUID().toString() + ext;
        Path filePath = Paths.get(uploadDir, uniqueName);
        file.transferTo(filePath.toFile());
        EventModel event = eventOpt.get();
        // Salva apenas o caminho relativo para facilitar servir a imagem depois
        String relativePath = "uploads/event-photos/" + uniqueName;
        event.setPhotoPath(relativePath);
        eventRepository.save(event);
        return relativePath;
    }
}
