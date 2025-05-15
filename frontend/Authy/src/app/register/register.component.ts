import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  registerReq: FormGroup = new FormGroup({
    profilePicture: new FormControl('', []),
    firstName: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[a-zA-Z]{2,}$/),
    ]),
    middleName: new FormControl('', [Validators.pattern(/^[a-zA-Z]*$/)]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[a-zA-Z]{2,}$/),
    ]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*#?&]).+$/),
    ]),
    confirmPassword: new FormControl('', [Validators.required]),
    mobilePhone: new FormControl('', [Validators.pattern(/^\+?[0-9]{10,15}$/)]),
    homePhone: new FormControl('', [Validators.pattern(/^\+?[0-9]{10,15}$/)]),
    dateOfBirth: new FormControl('', [Validators.required]),
    nationalID: new FormControl('', [Validators.pattern(/^[A-Z0-9]{5,20}$/)]),
    gender: new FormControl('', [Validators.required]),
    maritalStatus: new FormControl('', [Validators.required]),
    role: new FormControl('', [Validators.required]),
    apartment: new FormControl('', [Validators.maxLength(10)]),
    floor: new FormControl('', [Validators.pattern(/^[0-9]*$/)]),
    area: new FormControl('', [Validators.required, Validators.minLength(3)]),
    city: new FormControl('', [Validators.required]),
    country: new FormControl('', [Validators.required]),
    postalCode: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[A-Za-z0-9]{4,10}$/),
    ]),
    linkedinUrl: new FormControl('', [
      Validators.pattern(/^(https?:\/\/)?(www\.)?linkedin\.com\/.*$/),
    ]),
    githubUrl: new FormControl('', [
      Validators.pattern(/^(https?:\/\/)?(www\.)?github\.com\/.*$/),
    ]),
    portflioUrl: new FormControl('', [
      Validators.pattern(/^(https?:\/\/)?[\w\-]+(\.[\w\-]+)+[/#?]?.*$/),
    ]),
    facebookUrl: new FormControl('', [
      Validators.pattern(/^(https?:\/\/)?(www\.)?facebook\.com\/.*$/),
    ]),
    instagramUrl: new FormControl('', [
      Validators.pattern(/^(https?:\/\/)?(www\.)?instagram\.com\/.*$/),
    ]),
    xUrl: new FormControl('', [
      Validators.pattern(/^(https?:\/\/)?(www\.)?(twitter|x)\.com\/.*$/),
    ]),
    bio: new FormControl('', [Validators.maxLength(500)]),
    interests: new FormControl('', [Validators.maxLength(200)]),
  });

  // ngOnInit(): void {
  //   this.registerReq.setValue({
  //     profilePicture: '',
  //     firstName: 'John',
  //     middleName: 'K',
  //     lastName: 'Doe',
  //     email: 'john.doe@example.com',
  //     password: 'Passw0rd!',
  //     confirmPassword: 'Passw0rd!',
  //     mobilePhone: '+12345678901',
  //     homePhone: '+10987654321',
  //     dateOfBirth: '1995-05-15',
  //     nationalID: 'A123456789',
  //     gender: 'Male',
  //     maritalStatus: 'Single',
  //     role: 'User',
  //     apartment: '12B',
  //     floor: '3',
  //     area: 'Downtown',
  //     city: 'New York',
  //     country: 'USA',
  //     postalCode: '10001',
  //     linkedinUrl: 'https://www.linkedin.com/in/johndoe',
  //     githubUrl: 'https://github.com/johndoe',
  //     portflioUrl: 'https://johndoe.dev',
  //     facebookUrl: '',
  //     instagramUrl: '',
  //     xUrl: 'https://twitter.com/johndoe',
  //     bio: 'A passionate developer who loves building full-stack applications.',
  //     interests: 'Angular,Spring Boot,Open Source',
  //   });
  // }

  onSubmit(): void {
    const formValue = this.registerReq.value;
    console.log('THIS IS THE REGISTER REQUEST DETAILS : ');
    console.log(formValue);
  }
}
