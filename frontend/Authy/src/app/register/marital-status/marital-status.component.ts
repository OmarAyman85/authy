import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-marital-status',
  imports: [ReactiveFormsModule],
  templateUrl: './marital-status.component.html',
  styleUrl: './marital-status.component.css',
})
export class MaritalStatusComponent {
  @Input() marritalStatusForm!: FormGroup;
}
