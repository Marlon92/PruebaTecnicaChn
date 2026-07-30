import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';

import { SolicitudService } from '../solicitud.service';
import { EstadoSolicitud, Solicitud } from '../solicitud.model';

@Component({
  selector: 'app-solicitud-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule
  ],
  templateUrl: './solicitud-list.component.html',
  styleUrl: './solicitud-list.component.scss'
})
export class SolicitudListComponent implements OnInit {

  solicitudes: Solicitud[] = [];
  columnas = ['cliente', 'monto', 'plazo', 'estado', 'saldo', 'acciones'];
  clienteId: number | null = null;

  constructor(
    private solicitudService: SolicitudService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const clienteIdParam = this.route.snapshot.paramMap.get('clienteId');
    this.clienteId = clienteIdParam ? Number(clienteIdParam) : null;

    this.route.queryParamMap.subscribe(params => {
      const estadoFiltro = params.get('estado') as EstadoSolicitud | null;
      this.cargarSolicitudes(estadoFiltro);
    });
  }

  cargarSolicitudes(estadoFiltro: EstadoSolicitud | null): void {
    if (this.clienteId !== null) {
      this.solicitudService.listarPorCliente(this.clienteId).subscribe(solicitudes => this.solicitudes = solicitudes);
      return;
    }

    this.solicitudService.listar().subscribe(solicitudes => {
      this.solicitudes = estadoFiltro
        ? solicitudes.filter(solicitud => solicitud.estado === estadoFiltro)
        : solicitudes;
    });
  }
}
