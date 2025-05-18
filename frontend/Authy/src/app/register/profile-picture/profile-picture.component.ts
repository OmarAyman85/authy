import { Component, EventEmitter, Input, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile-picture',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile-picture.component.html',
  styleUrl: './profile-picture.component.css',
})
export class ProfilePictureComponent {
  @Input() control!: FormControl;
  @Input() previewUrl!: string;
  @Output() profileUrlChanged = new EventEmitter<string>();
  @Output() fileSelected = new EventEmitter<File>();

  selectedFileName: string = '';

  constructor(private http: HttpClient) {}

  onFileChange(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      this.selectedFileName = file.name;
      this.fileSelected.emit(file);
      this.uploadFile(file);
    }
  }

  private uploadFile(file: File) {
    const formData = new FormData();
    formData.append('file', file);

    this.http
      .post('http://localhost:8081/api/upload-profile-picture', formData, {
        responseType: 'text',
      })
      .subscribe({
        next: (url: string) => {
          this.control.setValue(url); // store the returned URL in form control
          this.profileUrlChanged.emit(url); // inform parent of new URL
        },
        error: (err) => {
          console.error('Profile picture upload failed', err);
        },
      });
  }
}
