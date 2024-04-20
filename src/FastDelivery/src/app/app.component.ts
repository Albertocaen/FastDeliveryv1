import { Component } from '@angular/core';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterLink, RouterModule, RouterOutlet} from '@angular/router'; // Importa el módulo RouterModule
import { AppRoutingModule } from './app-routing.module';
import { ContactoComponent } from './contacto/contacto.component';

@NgModule({
  declarations: [
    ContactoComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([]), // Agrega RouterModule.forRoot([]) aquí
    AppRoutingModule,
  ],
  providers: [],

})

export class AppModule { }
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    RouterLink,
    RouterOutlet
  ],
  standalone: true
})
export class AppComponent {
  title = 'FastDelivery';
}
