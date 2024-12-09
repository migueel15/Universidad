import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { SubirImagenesComponent } from './modules/imagenes/subir-imagenes.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SubirImagenesComponent],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  title = 'laWiki';
  constructor(private router: Router) {}
  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (!(event instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });
  }
}
