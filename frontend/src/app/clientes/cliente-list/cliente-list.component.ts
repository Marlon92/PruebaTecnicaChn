import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { ClienteService } from '../cliente.service';
import { Cliente } from '../cliente.model';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, RouterLink, MatTableModule, MatButtonModule, MatIconModule],
  templateUrl: './cliente-list.component.html',
  styleUrl: './cliente-list.component.scss'
})
export class ClienteListComponent implements OnInit {

  clientes: Cliente[] = [];
  columnas = ['nombre', 'identificacion', 'contacto', 'acciones'];

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clienteService.listar().subscribe(clientes => this.clientes = clientes);
  }

  eliminar(cliente: Cliente): void {
    const confirmado = confirm(
      `Eliminar a ${cliente.nombre} ${cliente.apellido} tambien eliminará sus solicitudes de prestamo. Desea continuar?`
    );

    if (!confirmado || cliente.id === undefined) {
      return;
    }

    this.clienteService.eliminar(cliente.id).subscribe(() => this.cargarClientes());
  }
}
