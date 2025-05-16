import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-date-of-birth',
  imports: [ReactiveFormsModule],
  templateUrl: './date-of-birth.component.html',
  styleUrl: './date-of-birth.component.css',
})
export class DateOfBirthComponent {
  @Input() dateOfBirthForm!: FormGroup;
}
