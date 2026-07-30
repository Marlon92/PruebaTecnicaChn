import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';

import { SolicitudService } from '../solicitud.service';
import { Pago, Solicitud } from '../solicitud.model';

@Component({
  selector: 'app-solicitud-detalle',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule
  ],
  templateUrl: './solicitud-detalle.component.html',
  styleUrl: './solicitud-detalle.component.scss'
})
export class SolicitudDetalleComponent implements OnInit {

  solicitud!: Solicitud;
  pagos: Pago[] = [];
  columnasPagos = ['fecha', 'monto', 'observacion'];

  formAprobar: FormGroup;
  formPago: FormGroup;

  constructor(
    private solicitudService: SolicitudService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.formAprobar = this.fb.group({
      tasaInteres: [null, [Validators.required, Validators.min(0)]],
      comentario: ['']
    });

    this.formPago = this.fb.group({
      monto: [null, [Validators.required, Validators.min(0.01)]],
      observacion: ['']
    });
  }

  ngOnInit(): void {
    this.cargarSolicitud();
  }

  cargarSolicitud(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.solicitudService.obtener(id).subscribe(solicitud => {
      this.solicitud = solicitud;

      if (solicitud.estado === 'APROBADA') {
        this.cargarPagos(id);
      }
    });
  }

  cargarPagos(solicitudId: number): void {
    this.solicitudService.listarPagos(solicitudId).subscribe(pagos => this.pagos = pagos);
  }

  aprobar(): void {
    if (this.formAprobar.invalid) {
      this.formAprobar.markAllAsTouched();
      return;
    }

    const { tasaInteres, comentario } = this.formAprobar.value;

    this.solicitudService.aprobar(this.solicitud.id!, tasaInteres, comentario).subscribe(() => {
      this.cargarSolicitud();
    });
  }

  rechazar(): void {
    const comentario = this.formAprobar.value.comentario;

    this.solicitudService.rechazar(this.solicitud.id!, comentario).subscribe(() => {
      this.cargarSolicitud();
    });
  }

  registrarPago(): void {
    if (this.formPago.invalid) {
      this.formPago.markAllAsTouched();
      return;
    }

    this.solicitudService.registrarPago(this.solicitud.id!, this.formPago.value).subscribe(() => {
      this.formPago.reset();
      this.cargarSolicitud();
    });
  }
}
