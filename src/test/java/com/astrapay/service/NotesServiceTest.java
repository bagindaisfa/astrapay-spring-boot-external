package com.astrapay.service;

import com.astrapay.dto.NoteDto;
import com.astrapay.dto.NoteRequestDto;
import com.astrapay.entity.Note;
import com.astrapay.exception.NoteNotFoundException;
import com.astrapay.repository.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotesService notesService;

    private Note testNote;
    private NoteDto testNoteDto;

    @BeforeEach
    void setUp() {
        // Setup test data
        testNote = new Note();
        testNote.setId(1L);
        testNote.setContent("Test Note");

        testNoteDto = new NoteDto(1L,"Test Note");
        testNoteDto.setId(1L);
        testNoteDto.setContent("Test Note");
    }

    @Test
    void getAllNotes_ShouldReturnListOfNotes() {
        // Arrange
        List<Note> notes = Arrays.asList(testNote);
        when(notesRepository.findAll()).thenReturn(notes);

        // Act
        List<NoteDto> result = notesService.getAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(notesRepository).findAll();
    }

    @Test
    void getNoteById_WithValidId_ShouldReturnNote() {
        // Arrange
        when(notesRepository.findById(1L)).thenReturn(Optional.of(testNote));

        // Act
        NoteDto result = notesService.getNoteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testNoteDto.getContent(), result.getContent());
        verify(notesRepository, times(1)).findById(1L);
    }

    @Test
    void getNoteById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(notesRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoteNotFoundException.class, () -> notesService.getNoteById(999L));
        verify(notesRepository, times(1)).findById(999L);
    }

    @Test
    void createNote_WithValidData_ShouldReturnCreatedNote() {
        // Arrange
        NoteRequestDto requestDto = new NoteRequestDto();
        requestDto.setContent("Test Note");
        
        when(notesRepository.save(anyString())).thenReturn(testNote);

        // Act
        NoteDto result = notesService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(testNote.getContent(), result.getContent());
        verify(notesRepository).save("Test Note");
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedNote() {
        // Arrange
        NoteRequestDto updateRequest = new NoteRequestDto();
        updateRequest.setContent("Updated content");
        
        Note updatedNote = new Note();
        updatedNote.setId(1L);
        updatedNote.setContent("Updated content");
        
        when(notesRepository.findById(1L)).thenReturn(Optional.of(testNote));
        when(notesRepository.update(anyLong(), anyString())).thenReturn(updatedNote);

        // Act
        NoteDto result = notesService.update(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(notesRepository, times(1)).findById(1L);
        verify(notesRepository, times(1)).update(eq(1L), eq("Updated content"));
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Arrange
        NoteRequestDto updateRequest = new NoteRequestDto();
        updateRequest.setContent("Updated content");
        
        when(notesRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoteNotFoundException.class, 
            () -> notesService.update(999L, updateRequest));
        verify(notesRepository, times(1)).findById(999L);
        verify(notesRepository, never()).update(anyLong(), anyString());
    }

    @Test
    void delete_WithValidId_ShouldDeleteNote() {
        // Arrange
        when(notesRepository.findById(1L)).thenReturn(Optional.of(testNote));
        doNothing().when(notesRepository).delete(1L);

        // Act
        notesService.delete(1L);

        // Assert
        verify(notesRepository, times(1)).findById(1L);
        verify(notesRepository, times(1)).delete(1L);
    }

    @Test
    void delete_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(notesRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoteNotFoundException.class, 
            () -> notesService.delete(999L));
        verify(notesRepository, times(1)).findById(999L);
        verify(notesRepository, never()).delete(anyLong());
    }
}
