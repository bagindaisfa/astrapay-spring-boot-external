package com.astrapay.repository;

import com.astrapay.entity.Note;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class NotesRepository {
    private final Map<Long, Note> store = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Note> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Note save(String content) {
        Long id = counter.getAndIncrement();
        Note note = new Note(id, content);
        store.put(id, note);
        return note;
    }

    public Note update(Long id, String content) {
        Note existing = store.get(id);
        if (existing == null) {
            return null;
        }
        existing.setContent(content);
        store.put(id, existing);
        return existing;
    }


    public void delete(Long id) {
        store.remove(id);
    }
}
