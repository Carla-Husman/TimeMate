import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ModalService } from 'src/app/services/Modal/modal.service';
import { UserService } from 'src/app/services/User/user.service';

@Component({
  selector: 'app-edit-modal',
  templateUrl: './edit-modal.component.html',
  styleUrls: ['./edit-modal.component.css']
})

/* this class is useful for managing the edit modal */
export class EditModalComponent implements OnInit {
  display$!: Observable<'open' | 'close'>;
  newUsername!: string;
  newEmail!: string;
  introducedPassword!: string;
  isFormValid!: boolean;
  errorMessage!: string;

  /* this function is used to inject the services */
  constructor(private modalService: ModalService, private userService: UserService) { }

  /* this function is used to initialize the variables */
  ngOnInit() {
    this.newUsername = "";
    this.newEmail = "";
    this.introducedPassword = "";
    this.errorMessage = "";
    this.display$ = this.modalService.watch();
  }

  /* this function is used to close the modal and reset the inputs */
  close() {
    this.newUsername = "";
    this.newEmail = "";
    this.introducedPassword = "";
    this.errorMessage = "";
    this.modalService.close();
  }

  /* this function is used to update the user
    and display an error message if the email or username
    have an incorrect format */
  submitForm(): boolean {
    this.userService.updateUser(this.newUsername, this.newEmail, this.introducedPassword).
      subscribe((response) => {
        this.errorMessage = response;
        if (this.errorMessage == "") {
          this.newUsername = "";
          this.newEmail = "";
          this.close();
        }
      });
    return true
  }
}
