import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-full-name',
  imports: [ReactiveFormsModule],
  templateUrl: './full-name.component.html',
  styleUrl: './full-name.component.css',
})
export class FullNameComponent {
  @Input() fullNameForm!: FormGroup;
}
