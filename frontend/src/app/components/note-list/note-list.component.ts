import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Note } from '../../models/note.model';
import { NoteService } from '../../services/note.service';
import { NoteFormComponent } from '../note-form/note-form.component';

@Component({
  selector: 'app-note-list',
  standalone: true,
  imports: [CommonModule, DatePipe, NoteFormComponent],
  templateUrl: './note-list.component.html',
  styles: [`
    /* Card styles */
    .card {
      box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
      margin-bottom: 1.5rem;
      border: 1px solid rgba(0, 0, 0, 0.125);
    }

    /* Card header styles */
    .card-header {
      background-color: #f8f9fa;
      border-bottom: 1px solid rgba(0, 0, 0, 0.125);
      padding: 0.75rem 1.25rem;
    }

    /* List group item styles */
    .list-group-item {
      border-left: none;
      border-right: none;
      padding: 1rem 1.25rem;
      cursor: pointer;
      transition: all 0.2s ease-in-out;
    }

    .list-group-item:first-child {
      border-top: none;
    }

    .list-group-item:last-child {
      border-bottom: none;
    }

    /* Note preview styles */
    .note-preview {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      color: #6c757d;
      font-size: 0.9em;
      margin-bottom: 0.25rem;
    }

    /* Timestamp styles */
    .note-timestamp {
      font-size: 0.75rem;
      color: #6c757d;
    }

    /* Loading spinner */
    .spinner-border {
      width: 1.5rem;
      height: 1.5rem;
      border-width: 0.2em;
    }

    /* Empty state */
    .empty-state {
      padding: 2rem;
      text-align: center;
      color: #6c757d;
    }

    .empty-state i {
      font-size: 3rem;
      opacity: 0.5;
      margin-bottom: 1rem;
      display: block;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
      .card {
        margin-bottom: 1rem;
      }
      
      .list-group-item {
        padding: 0.75rem 1rem;
      }
      
      .btn-sm {
        padding: 0.25rem 0.5rem;
        font-size: 0.75rem;
      }
    }
  `]
})
export class NoteListComponent implements OnInit {
  notes: Note[] = [];
  selectedNote: Note | null = null;
  isLoading = false;
  errorMessage = '';
  
  private noteService = inject(NoteService);

  ngOnInit(): void {
    this.loadNotes();
  }

  loadNotes(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.noteService.getNotes()
      .subscribe({
        next: (notes) => {
          this.notes = notes;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = 'Failed to load notes. Please try again later.';
          this.isLoading = false;
          console.error('Error loading notes:', err);
        }
      });
  }

  onSelect(note: Note): void {
    this.selectedNote = { ...note };
  }

  addNote(): void {
    this.selectedNote = { 
      id: 0, 
      content: '', 
      createdAt: new Date().toISOString(), 
      updatedAt: new Date().toISOString() 
    };
  }

  onEditNote(note: Note): void {
    this.selectedNote = { ...note };
  }

  onSaveNote(note: Note): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    const saveOperation = note.id 
      ? this.noteService.updateNote(note.id, note)
      : this.noteService.createNote(note);

    saveOperation.subscribe({
      next: () => {
        this.loadNotes();
        this.selectedNote = null;
      },
      error: (err) => {
        this.errorMessage = err.message || 'An error occurred while saving the note.';
        this.isLoading = false;
      }
    });
  }

  onDelete(id: number): void {
    if (!confirm('Are you sure you want to delete this note?')) {
      return;
    }

    this.noteService.deleteNote(id).subscribe({
      next: () => {
        this.notes = this.notes.filter(note => note.id !== id);
        if (this.selectedNote?.id === id) {
          this.selectedNote = null;
        }
      },
      error: (err) => {
        console.error('Error deleting note:', err);
        alert('Failed to delete the note.');
      }
    });
  }

  onNoteSaved(note: Note): void {
    const index = this.notes.findIndex(n => n.id === note.id);
    if (index >= 0) {
      this.notes = [
        ...this.notes.slice(0, index),
        note,
        ...this.notes.slice(index + 1)
      ];
    } else {
      this.notes = [...this.notes, note];
    }
    this.selectedNote = null;
  }

  onCancel(): void {
    this.selectedNote = null;
  }
}
