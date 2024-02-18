import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/User/user.service';
import { User } from 'src/app/models/User/user.model';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/Auth/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  myForm: FormGroup;
  isRegistrationMode: boolean = true;

  /* Injecting necessary services */
  constructor(private fb: FormBuilder, private userService: UserService, private authService: AuthService, private router: Router) {
    // Initializing the form with default values and validators
    this.myForm = this.fb.group({
      username: ['Username', [Validators.required, Validators.pattern('^[a-zA-Z0-9._]{7,14}\$')]],
      email: ['Email', [Validators.required, Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')]],
      password: ['Password', [Validators.required, Validators.pattern('^(?=[^a-z]*[a-z])(?=\\D*\\d)[^:&~\\s]{8,20}\$')]]
    });
  }

  /* Function to handle form submission */
  onSubmit() {
    // If the form is valid, create a new user or sign in, based on the mode
    if (this.myForm.valid) {
      // If in registration mode, create a new user
      if (this.isRegistrationMode) {
        this.userService.createUser(new User(
          0,
          this.myForm.value.username,
          this.myForm.value.password,
          this.myForm.value.email,
          0
        )).subscribe(
          (responseMessage: string) => {
            console.log(responseMessage);
            if (responseMessage === "") {
              this.toggleMode();
            } else {
              alert(responseMessage);
            }
          },
          (error) => {
            // Handle errors if needed
          }
        );
      }
      // If in login mode, verify user existence and sign in
      else {
        if (this.myForm.value.username.includes("admin")) {
          // Login as admin
          this.authService.login(
            this.myForm.value.username,
            this.myForm.value.password
          ).subscribe(
            (responseMessage: string) => {
              if (responseMessage == "Success") {
                window.localStorage.setItem('admin', 'true');
                this.router.navigate(['/admin']);
              } else {
                alert(responseMessage);
              }
            },
            (error) => {
              // Handle errors if needed
            }
          );
        } else {
          // Login as a regular user
          this.authService.login(
            this.myForm.value.username,
            this.myForm.value.password
          ).subscribe(
            (responseMessage: string) => {
              if (responseMessage == "Success") {
                this.router.navigate(['/profile']);
              } else if (responseMessage == "Not activated") {
                alert("Your account is not activated yet");
              } else {
                alert(responseMessage);
              }
            },
            (error) => {
              // Handle errors if needed
            }
          );
        }
      }
    }
  }

  /* Function to toggle between registration and login mode */
  toggleMode() {
    this.isRegistrationMode = !this.isRegistrationMode;
    if (!this.isRegistrationMode) {
      // Clear validators and reset values for login mode
      this.myForm.get('email')!.clearValidators();
      this.myForm.get('email')!.setValue('');
      this.myForm.get('username')!.setValue('Username');
      this.myForm.get('password')!.setValue('Password1');
    } else {
      // Set validators and reset values for registration mode
      this.myForm.get('email')!.setValidators([Validators.required, Validators.email]);
      this.myForm.get('email')!.setValue('Email');
      this.myForm.get('username')!.setValue('Username');
      this.myForm.get('password')!.setValue('Password1');
    }

    // Update the form controls
    this.myForm.get('email')!.updateValueAndValidity();
    this.myForm.get('username')!.updateValueAndValidity();
    this.myForm.get('password')!.updateValueAndValidity();
  }
}
