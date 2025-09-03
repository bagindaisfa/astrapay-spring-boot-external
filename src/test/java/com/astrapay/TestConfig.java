package com.astrapay;

import com.astrapay.repository.NotesRepository;
import com.astrapay.service.NotesService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public NotesRepository notesRepository() {
        return Mockito.mock(NotesRepository.class);
    }

    @Bean
    @Primary
    public NotesService notesService(NotesRepository notesRepository) {
        return new NotesService(notesRepository);
    }
}
