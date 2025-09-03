package com.astrapay.controller;

import com.astrapay.dto.NoteDto;
import com.astrapay.dto.NoteRequestDto;
import com.astrapay.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotesControllerTest {

    @Mock
    private NotesService notesService;

    @InjectMocks
    private NotesController notesController;

    private NoteDto testNote;
    private NoteRequestDto testNoteRequestDto;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testNote = new NoteDto(1L, "Test Note");
        testNoteRequestDto = new NoteRequestDto();
        testNoteRequestDto.setContent("Test Note");
        
        mockMvc = MockMvcBuilders.standaloneSetup(notesController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllNotes_ShouldReturnListOfNotes() throws Exception {
        // Arrange
        List<NoteDto> notes = Arrays.asList(testNote);
        when(notesService.getAll()).thenReturn(notes);

        // Act & Assert
        mockMvc.perform(get("/notes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(testNote.getContent())));
        
        verify(notesService).getAll();
    }


    @Test
    void createNote_WithValidData_ShouldReturnCreatedNote() throws Exception {
        // Arrange
        when(notesService.create(any(NoteRequestDto.class))).thenReturn(testNote);

        // Act & Assert
        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testNoteRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(testNote.getContent())));
        
        verify(notesService).create(any(NoteRequestDto.class));
    }

    @Test
    void updateNote_WithValidData_ShouldReturnUpdatedNote() throws Exception {
        // Arrange
        testNoteRequestDto.setContent("Updated content");
        when(notesService.update(eq(1L), any(NoteRequestDto.class))).thenReturn(testNote);

        // Act & Assert
        mockMvc.perform(put("/notes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testNoteRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(testNote.getContent())));
        
        verify(notesService).update(eq(1L), any(NoteRequestDto.class));
    }

    @Test
    void deleteNote_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(notesService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/notes/1"))
                .andExpect(status().isNoContent());
        
        verify(notesService).delete(1L);
    }
}
