import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-email',
  imports: [ReactiveFormsModule],
  templateUrl: './email.component.html',
  styleUrl: './email.component.css',
})
export class EmailComponent {
  @Input() emailForm!: FormGroup;
}
