import { LocaleService } from '../../services/locale.service';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { DropdownModule } from 'primeng/dropdown';

interface Locale {
  code: string,
  name: string
}

@Component({
  selector: 'app-locale-switch',
  standalone: true,
  imports: [
    CommonModule,
    DropdownModule,
    FormsModule
  ],
  templateUrl: './locale-switch.component.html',
  styleUrl: './locale-switch.component.css'
})
export class LocaleComponent {
  localeList: Locale[] = [
    { code: "en", name: "English" },
    { code: "ru", name: "Русский" }
  ];
  selectedLocale: Locale = this.localeList[0];

  constructor(private translate: TranslateService,
              private localeService: LocaleService) 
  {
    const localeCode = this.localeService.restoreLocale();
    const locale = this.localeList.find((language) => language.code == localeCode);

    if (locale) {
      this.selectedLocale = locale;
    }
    else {
      this.selectedLocale = this.localeList[0];
    }
  }

  setLocale(): void {
    this.translate.use(this.selectedLocale.code);
    this.localeService.saveLocale(this.selectedLocale.code);
  }
}
