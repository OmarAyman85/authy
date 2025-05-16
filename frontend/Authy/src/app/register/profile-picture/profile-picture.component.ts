import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile-picture',
  imports: [ReactiveFormsModule],
  templateUrl: './profile-picture.component.html',
  styleUrl: './profile-picture.component.css',
})
export class ProfilePictureComponent {
  @Input() control!: FormControl ;
  @Input() selectedFileName!: string;
  @Input() previewUrl!: string;
  @Output() fileSelected = new EventEmitter<File>();

  onFileChange(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      this.control.setValue(file);
      this.fileSelected.emit(file);
    }
  }
}
