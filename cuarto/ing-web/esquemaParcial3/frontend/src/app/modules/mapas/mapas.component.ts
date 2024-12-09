import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import * as L from "leaflet";
import { MapasService } from "./mapas.service";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";

@Component({
  selector: "app-mapas",
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: "./mapas.component.html",
})
export class MapasComponent implements OnInit {
  @Input()
  ubicacion!: FormGroup;
  @Input()
  entradaId: string | undefined;
  @Input()
  mostrarBusqueda: boolean = false;
  @Output()
  mapaCreated = new EventEmitter<string>();
  private mapa: L.Map | undefined;

  constructor(
    private mapasService: MapasService,
    private fb: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.mapa = L.map("map").setView([39.3260685, -4.8379791], 5);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "&copy; OpenStreetMap contributors",
    }).addTo(this.mapa);

    const lat = this.ubicacion.get("lat")?.value;
    const lon = this.ubicacion.get("lon")?.value;
    if (lat && lon) {
      this.actualizarMapaEnVista(lat, lon);
    }
  }

  buscar(inputValue: string): void {
    if (this.esCoordenadas(inputValue)) {
      const [lat, lon] = inputValue
        .split(",")
        .map((coord) => parseFloat(coord.trim()));
      this.mapasService.searchByQuery({ lat, lon }).subscribe((response) => {
        const coords = response.data;
        if (coords) {
          this.actualizarMapaEnVista(coords.lat, coords.lon);
        }
      });
    } else {
      this.mapasService
        .searchByQuery({ query: inputValue })
        .subscribe((response) => {
          const coords = response.data;
          if (coords) {
            this.actualizarMapaEnVista(coords.lat, coords.lon);
          }
        });
    }
  }

  private esCoordenadas(value: string): boolean {
    const coordRegex = /^-?\d+(\.\d+)?\s*,\s*-?\d+(\.\d+)?$/;
    return coordRegex.test(value);
  }

  private actualizarMapaEnVista(lat: number, lon: number): void {
    if (this.mapa) {
      const myIcon = L.icon({
        iconUrl: "../../assets/marker-icon.png",
        shadowUrl: "../../assets/marker-shadow.png",
      });

      L.marker([lat, lon], { icon: myIcon }).addTo(this.mapa);
      this.mapa.setView([lat, lon], 13);

      if (this.mostrarBusqueda) {
        this.ubicacion.patchValue({
          lat: lat,
          lon: lon,
        });
      }
    }
  }

  crearMapa() {
    if (this.ubicacion.valid) {
      const mapaData = {
        idEntrada: "674df7b1a1067f53a7b2e294", // TO-DO
        lat: this.ubicacion.get("lat")?.value,
        lon: this.ubicacion.get("lon")?.value,
        zoom: 13,
      };

      this.mapasService.createMapa(mapaData).subscribe({
        next: (response) => {
          console.log("Mapa creado correctamente:", response);
          this.mapaCreated.emit(response.idMapa);
        },
        error: (err) => {
          console.error("Error al crear el mapa:", err);
        },
      });
    } else {
      console.log("Formulario no válido");
    }
  }

  actualizarMapa(id: string, idEntrada: string) {
    const mapaData = {
      idEntrada: idEntrada,
    };

    this.mapasService.updateMapa(id, mapaData).subscribe({
      next: (response) => {
        console.log("Mapa actualizado correctamente:", response);
      },
      error: (err) => {
        console.error("Error al actualizar el mapa:", err);
      },
    });
  }
}
