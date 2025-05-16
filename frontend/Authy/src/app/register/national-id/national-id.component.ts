import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-national-id',
  imports: [ReactiveFormsModule],
  templateUrl: './national-id.component.html',
  styleUrl: './national-id.component.css',
})
export class NationalIdComponent {
  @Input() nationalIdForm!: FormGroup;
}
