import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { AuthService } from '../../services/auth.service';
import { TooltipModule } from 'primeng/tooltip';
import { DividerModule } from 'primeng/divider';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { HeaderComponent } from '../header/header.component';

interface ErrorCodes {
  [key: string]: string;
}

const usernameErrorCodes = {
  required: `SIGN_UP.ERRORS.FILL_IN_THE_FIELD` ,
  minlength: `SIGN_UP.ERRORS.USERNAME_SHORT`,
  maxlength: `SIGN_UP.ERRORS.USERNAME_LONG`,
  pattern: `SIGN_UP.ERRORS.USERNAME_ILLEGAL_CHARACTERS`
};

const passwordErrorCodes = {
  required: `SIGN_UP.ERRORS.FILL_IN_THE_FIELD`,
  minlength: `SIGN_UP.ERRORS.PASSWORD_SHORT`,
  maxlength: `SIGN_UP.ERRORS.PASSWORD_LONG`
}

const confirmErrorCodes = {
  required: `SIGN_UP.ERRORS.FILL_IN_THE_FIELD`,
  unequal: `SIGN_UP.ERRORS.PASSWORD_DO_NOT_MATCH`
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CardModule,
    ButtonModule,
    ReactiveFormsModule, 
    PasswordModule,
    InputTextModule,
    RouterLink, 
    RouterLinkActive,
    TooltipModule,
    DividerModule,
    TranslateModule,
    CommonModule,
    HeaderComponent
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  public usernameToolTip: string = "";
  public passwordToolTip: string = "";
  public confirmToolTip: string = "";
  public conflict: boolean = false;
  public error: boolean = false;

  form = new FormGroup(
    {
      username: new FormControl("", [
        Validators.required,
        Validators.pattern("[a-zA-Z0-9_]+"),
        Validators.minLength(6),
        Validators.maxLength(20)
      ]),
      password: new FormControl("", [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(32)
      ]),
      confirmPassword: new FormControl("")
    }
  );

  constructor(private auth: AuthService,
              private router: Router,
              private translate: TranslateService) 
  {}

  ngOnInit() {
    const usernameField = this.form.get('username');
    const passwordField = this.form.get('password');
    const confirmField = this.form.get('confirmPassword');

    confirmField?.addValidators(this.matchPassword(this.form, "password"));
    confirmField?.addValidators(Validators.required);

    usernameField?.valueChanges.subscribe(() => {
      this.conflict = false;
      const errorCode = this.getErrorCode(usernameField, usernameErrorCodes);
      if (errorCode) {
        this.translate.get(errorCode)
        .subscribe(
          (value) => {
            if (value) this.usernameToolTip = value
          }
        );
      }
      else {
        this.usernameToolTip = "";
      }
    });

    passwordField?.valueChanges.subscribe(() => {
      confirmField?.updateValueAndValidity();
      const errorCode = this.getErrorCode(passwordField, passwordErrorCodes);
      if (errorCode) {
        this.translate.get(errorCode)
        .subscribe(
          (value) => {
            if (value) this.passwordToolTip = value
          }
        );
      }
      else {
        this.passwordToolTip = "";
      }
    });

    confirmField?.valueChanges.subscribe(() => {
      const errorCode = this.getErrorCode(confirmField, confirmErrorCodes);
      if (errorCode) {
        this.translate.get(errorCode)
        .subscribe(
          (value) => {
            if (value) this.confirmToolTip = value
          }
        );
      }
      else {
        this.confirmToolTip = "";
      }
    });

    this.translate.onLangChange.subscribe(
      (value) => this.validate()
    );
  }

  validate(): void {
    this.form.get('username')?.updateValueAndValidity();
    this.form.get('password')?.updateValueAndValidity();
    this.form.get('confirmPassword')?.updateValueAndValidity();
  }

  onSubmit() {
    this.validate();

    if (this.usernameToolTip || this.passwordToolTip || this.confirmToolTip) {
      return;
    }

    this.auth.register(<string>this.form.value.username, <string>this.form.value.password)
    .subscribe({
      next: (token) => {
        this.router.navigate(["/"]);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 409) {
          this.error = false;
          this.conflict = true;
          this.form.get("username")?.setErrors({taken: true});
        }
        else { 
          this.conflict = false;
          this.error = true;
        }
        console.log("Registration failed", error);
      }
    });
  }

  matchPassword(form: AbstractControl, matchingFieldName: string) {
    const matchingControl = form.get(matchingFieldName);

    return (control: AbstractControl) => {
      return matchingControl?.value !== control.value ? { unequal: true } : null;
    }
  }

  getErrorCode(field: AbstractControl | null, errorCodes: ErrorCodes): string {
    if (!field || !field.errors) {
      return "";
    }
  
    const error = Object.keys(field.errors)[0];
    return errorCodes[error] || "";
  }
}
