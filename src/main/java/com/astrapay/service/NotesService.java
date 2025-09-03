package com.astrapay.service;

import com.astrapay.dto.NoteDto;
import com.astrapay.dto.NoteRequestDto;
import com.astrapay.entity.Note;
import com.astrapay.exception.NoteNotFoundException;
import com.astrapay.repository.NotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotesService {
    private final NotesRepository repository;

    public NotesService(NotesRepository repository) {
        this.repository = repository;
    }

    public List<NoteDto> getAll() {
        return repository.findAll()
                .stream()
                .map(n -> new NoteDto(n.getId(), n.getContent()))
                .collect(Collectors.toList());
    }

    public NoteDto create(NoteRequestDto request) {
        log.info("Creating new note");
        
        if (request == null || request.getContent() == null) {
            String errorMsg = "Note content cannot be null";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        
        String content = request.getContent().trim();
        if (content.isEmpty()) {
            String errorMsg = "Note content cannot be empty";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        
        try {
            log.debug("Saving new note with content: {}", content);
            Note note = repository.save(content);
            
            if (note == null || note.getId() == null) {
                String errorMsg = "Failed to create note";
                log.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
            
            log.info("Successfully created note with id: {}", note.getId());
            return new NoteDto(note.getId(), note.getContent());
            
        } catch (DataIntegrityViolationException dive) {
            String errorMsg = "Data integrity violation while creating note: " + dive.getMessage();
            log.error(errorMsg, dive);
            throw new IllegalStateException(errorMsg, dive);
        } catch (Exception e) {
            String errorMsg = "Unexpected error creating note: " + e.getMessage();
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public NoteDto update(Long id, NoteRequestDto request) {
        log.info("Updating note with id: {}", id);
        
        if (id == null) {
            String errorMsg = "Note ID cannot be null";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        
        if (request == null || request.getContent() == null) {
            String errorMsg = "Note content cannot be null";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        
        log.debug("Looking for note with id: {}", id);
        Optional<Note> existingNote = repository.findById(id);
        if (existingNote.isEmpty()) {
            String errorMsg = "Note with id " + id + " not found";
            log.error(errorMsg);
            throw new NoteNotFoundException(errorMsg);
        }
        
        try {
            log.debug("Updating note content for id: {}", id);
            String content = request.getContent().trim();
            if (content.isEmpty()) {
                String errorMsg = "Note content cannot be empty";
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            
            Note updated = repository.update(id, content);
            if (updated == null) {
                String errorMsg = "Failed to update note with id: " + id;
                log.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
            
            log.info("Successfully updated note with id: {}", id);
            return new NoteDto(updated.getId(), updated.getContent());
            
        } catch (DataIntegrityViolationException dive) {
            String errorMsg = "Data integrity violation while updating note: " + dive.getMessage();
            log.error(errorMsg, dive);
            throw new IllegalStateException(errorMsg, dive);
        } catch (Exception e) {
            String errorMsg = "Unexpected error updating note: " + e.getMessage();
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }


    public void delete(Long id) {
        if (!repository.findById(id).isPresent()) {
            throw new NoteNotFoundException("Note with id " + id + " not found");
        }
        repository.delete(id);
    }
}

