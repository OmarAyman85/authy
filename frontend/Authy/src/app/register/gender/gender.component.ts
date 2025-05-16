import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-gender',
  imports: [ReactiveFormsModule],
  templateUrl: './gender.component.html',
  styleUrl: './gender.component.css',
})
export class GenderComponent {
  @Input() genderForm!: FormGroup;
}
