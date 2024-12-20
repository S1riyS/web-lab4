import { AuthService } from './../../services/auth.service';
import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { LocaleComponent } from '../locale-switch/locale-switch.component';
import { TranslateModule } from '@ngx-translate/core';
import { ToolbarModule } from 'primeng/toolbar';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { LayoutService } from '../../services/layout.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    ButtonModule,
    LocaleComponent,
    TranslateModule,
    ToolbarModule,
    CardModule,
    CommonModule,
    RouterModule,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  constructor(private authService: AuthService,
              private router: Router,
              public layout: LayoutService) 
  {}

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logOut(): void {
    this.authService.logOut();
    this.router.navigate(['/login']);
  }

  get username(): string {
    return this.authService.username;
  }
}
