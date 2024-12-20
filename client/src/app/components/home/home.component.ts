import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { ButtonModule } from 'primeng/button';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SliderModule } from 'primeng/slider';
import { CardModule } from 'primeng/card';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { CommonModule, DatePipe } from '@angular/common';
import { Dot, Shot, ShotsService } from '../../services/shots.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { LayoutService } from '../../services/layout.service';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-graph',
  standalone: true,
  imports: [
    HeaderComponent,
    ButtonModule,
    RouterModule,
    SliderModule,
    CardModule,
    ReactiveFormsModule,
    TableModule,
    CommonModule,
    ToastModule,
    DropdownModule,
    TranslateModule
  ],
  providers: [MessageService, DatePipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  public R_SIZE = 100;
  public GRAPH_SIZE = 300;

  public rText = "R";
  public r2Text = "R/2";

  public form = new FormGroup({
    x: new FormControl(0),
    y: new FormControl(0),
    r: new FormControl(1)
  });

  public radiusValues: number[] = [1, 2, 3, 4, 5];

  public shots: Shot[] = [];

  constructor(private auth: AuthService,
              private shotService: ShotsService,
              private messageService: MessageService,
              public layout: LayoutService,
              private translate: TranslateService) 
  {}

  ngOnInit(): void {
    this.shotService.getShots()
    .subscribe({
      next: (value) => this.shots = value,
      error: (error) => {
        console.log(error);
      }
    })
  }

  onClick(event: MouseEvent): void {
    const element = document.getElementById("svg-figure");
    let mouseX, mouseY, r;

    if (element && this.form.value.r && this.form.value.r > 0) {
      mouseX = event.clientX - element.getBoundingClientRect().left;
      mouseY = event.clientY - element.getBoundingClientRect().top;
      r = this.form.value.r

      mouseX -= 150;
      mouseY -= 150;
      mouseX = mouseX / this.R_SIZE * r;
      mouseY = - mouseY / this.R_SIZE * r;
    }
    else {
      mouseX = 0;
      mouseY = 0;
      r = 1;
    }

    this.sendShot({
      x: mouseX,
      y: mouseY,
      r: r
    });
  }

  newShot(): void {
    this.sendShot({
      x: this.form.value.x ? this.form.value.x : 0,
      y: this.form.value.y ? this.form.value.y : 0,
      r: this.form.value.r ? this.form.value.r : 1
    });
  }

  private sendShot(dot: Dot) {
    if (!this.form.value.r || this.form.value.r <= 0) {
      this.translate.get("HOME.ERRORS.R_SHOULD_BE_POSITIVE")
      .subscribe(
        (value) => {
          this.messageService.add({
            severity: "error",
            detail: value
          });
        }
      );
      return;
    }
    if (!this.auth.isLoggedIn) {
      this.translate.get("HOME.ERRORS.AUTH_ERROR")
      .subscribe(
        (value) => {
          this.messageService.add({
            severity: "error",
            detail: value
          });
        }
      );
      return;
    }

    this.shotService.newShot(dot)
    .subscribe({
      next: (shot) => this.shots = [shot, ...this.shots],
      error: (error) => {
        const details: string[] = error.error.errors
        this.messageService.add({
          severity:'error', 
          summary: 'Error', 
          detail: details.join(", ")
        })
      }
    });
  }

  public clearShots(): void {
    this.shotService.clearShots().subscribe({
      next: () => this.shots = [],
      error: (error) => console.log("Error deleting shots", error)
    });
  }

  public filterShotsByRadius(r: number) {
    return this.shots.filter( shot => shot.r == r);
  }
}
