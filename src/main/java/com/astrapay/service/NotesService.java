package com.astrapay.service;

import com.astrapay.dto.NoteDto;
import com.astrapay.dto.NoteRequestDto;
import com.astrapay.entity.Note;
import com.astrapay.exception.NoteNotFoundException;
import com.astrapay.repository.NotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        Note note = repository.save(request.getContent());
        return new NoteDto(note.getId(), note.getContent());
    }

    public NoteDto update(Long id, NoteRequestDto request) {
        Note updated = repository.update(id, request.getContent());
        if (updated == null) {
            throw new NoteNotFoundException("Note with id " + id + " not found");
        }
        return new NoteDto(updated.getId(), updated.getContent());
    }


    public void delete(Long id) {
        if (!repository.findById(id).isPresent()) {
            throw new NoteNotFoundException("Note with id " + id + " not found");
        }
        repository.delete(id);
    }
}

