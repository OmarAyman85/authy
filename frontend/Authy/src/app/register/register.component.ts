import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterLink } from '@angular/router';
import { passwordMatchValidator } from '../validators/custom-validators';
import { ProfilePictureComponent } from './profile-picture/profile-picture.component';
import { FullNameComponent } from './full-name/full-name.component';
import { UserNameComponent } from './user-name/user-name.component';
import { EmailComponent } from './email/email.component';
import { PasswordComponent } from './password/password.component';
import { PhoneComponent } from './phone/phone.component';
import { NationalIdComponent } from "./national-id/national-id.component";
import { DateOfBirthComponent } from "./date-of-birth/date-of-birth.component";
import { GenderComponent } from "./gender/gender.component";
import { MaritalStatusComponent } from "./marital-status/marital-status.component";
import { RoleComponent } from "./role/role.component";
import { AddressComponent } from "./address/address.component";
import { SocialLinksComponent } from "./social-links/social-links.component";

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    ProfilePictureComponent,
    FullNameComponent,
    UserNameComponent,
    EmailComponent,
    PasswordComponent,
    PhoneComponent,
    NationalIdComponent,
    DateOfBirthComponent,
    GenderComponent,
    MaritalStatusComponent,
    RoleComponent,
    AddressComponent,
    SocialLinksComponent
],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  registerReq: FormGroup = new FormGroup(
    {
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
      userName: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(20),
        Validators.pattern('^[a-zA-Z0-9_]+$'),
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(
          /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*#?&_]).+$/
        ),
      ]),
      confirmPassword: new FormControl('', [Validators.required]),
      mobilePhone: new FormControl('', [
        Validators.required,
        Validators.pattern(/^\+?[0-9]{10,15}$/),
      ]),
      homePhone: new FormControl('', [Validators.pattern(/^\+?[0-9]{8,12}$/)]),
      dateOfBirth: new FormControl('', [Validators.required]),
      nationalID: new FormControl('', [Validators.pattern(/^[A-Z0-9]{5,20}$/)]),
      gender: new FormControl('', [Validators.required]),
      maritalStatus: new FormControl('', [Validators.required]),
      role: new FormControl('', [Validators.required]),
      apartment: new FormControl('', [Validators.maxLength(10)]),
      floor: new FormControl('', [Validators.pattern(/^[0-9]*$/)]),
      street: new FormControl('', [Validators.required]),
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
        Validators.pattern(/^(https?:\/\/)?(www\.)?(x)\.com\/.*$/),
      ]),
      bio: new FormControl('', [Validators.maxLength(500)]),
      interests: new FormControl('', [Validators.maxLength(200)]),
    },
    { validators: passwordMatchValidator }
  );

  onSubmit(): void {
    const formValue = this.registerReq.value;
    console.log('THIS IS THE REGISTER REQUEST DETAILS : ');
    console.log(formValue);
  }

  selectedFileName = '';
  previewUrl = '';

  get profilePictureControl(): FormControl {
    return this.registerReq.get('profilePicture') as FormControl;
  }

  onFileSelected(file: File) {
    this.selectedFileName = file.name;

    const reader = new FileReader();
    reader.onload = () => {
      this.previewUrl = reader.result as string;
    };
    reader.readAsDataURL(file);
  }
}
