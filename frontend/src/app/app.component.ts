import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NoteListComponent } from './components/note-list/note-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NoteListComponent],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
      <div class="container">
        <a class="navbar-brand" href="#">Notes App</a>
      </div>
    </nav>

    <main class="container">
      <router-outlet></router-outlet>
      <app-note-list></app-note-list>
    </main>

    <footer class="mt-5 py-3 text-center text-muted">
      <div class="container">
        <p class="mb-0">© {{ currentYear }} Notes App. All rights reserved.</p>
      </div>
    </footer>
  `,
  styles: [`
    :host {
      display: block;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }
    
    main {
      flex: 1;
    }
  `]
})
export class AppComponent {
  currentYear = new Date().getFullYear();
}
