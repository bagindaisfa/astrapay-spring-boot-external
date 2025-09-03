import { Component, EventEmitter, Input, Output } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { Note } from "../../models/note.model";
import { NoteService } from "../../services/note.service";

@Component({
  selector: "app-note-form",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./note-form.component.html",
  styles: [
    `
      /* Form styles */
      form {
        margin-bottom: 0;
      }

      /* Textarea styles */
      textarea {
        min-height: 200px;
        resize: vertical;
      }

      /* Button container */
      .d-flex {
        margin-top: 1.5rem;
      }

      /* Error message styles */
      .invalid-feedback {
        display: block;
        margin-top: 0.25rem;
        font-size: 0.875em;
        color: #dc3545;
      }

      /* Textarea focus state */
      textarea:focus {
        border-color: #86b7fe;
        box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
        outline: 0;
      }

      /* Button styles */
      .btn {
        min-width: 100px;
      }

      /* Responsive adjustments */
      @media (max-width: 768px) {
        .btn {
          width: 100%;
          margin-bottom: 0.5rem;
        }

        .btn:last-child {
          margin-bottom: 0;
        }
      }
    `,
  ],
})
export class NoteFormComponent {
  @Input() note: Note = { content: "" };
  @Output() save = new EventEmitter<Note>();
  @Output() cancel = new EventEmitter<void>();
  isLoading = false;
  errorMessage = "";

  onSubmit(): void {
    if (this.note.content.trim()) {
      this.isLoading = true;
      this.errorMessage = "";
      this.save.emit({ ...this.note });
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
