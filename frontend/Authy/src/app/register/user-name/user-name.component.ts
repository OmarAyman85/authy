import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-name',
  imports: [ReactiveFormsModule],
  templateUrl: './user-name.component.html',
  styleUrl: './user-name.component.css',
})
export class UserNameComponent {
  @Input() userNameForm!: FormGroup;
}
