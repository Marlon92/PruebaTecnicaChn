import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { SolicitudService } from '../solicitud.service';

@Component({
  selector: 'app-solicitud-form',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './solicitud-form.component.html',
  styleUrl: './solicitud-form.component.scss'
})
export class SolicitudFormComponent implements OnInit {

  form: FormGroup;
  clienteId!: number;

  constructor(
    private fb: FormBuilder,
    private solicitudService: SolicitudService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      montoSolicitado: [null, [Validators.required, Validators.min(1)]],
      plazoMeses: [null, [Validators.required, Validators.min(1)]],
      motivo: ['']
    });
  }

  ngOnInit(): void {
    this.clienteId = Number(this.route.snapshot.paramMap.get('clienteId'));
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const solicitud = { clienteId: this.clienteId, ...this.form.value };

    this.solicitudService.crear(solicitud).subscribe(() => {
      this.router.navigate(['/clientes', this.clienteId, 'solicitudes']);
    });
  }

  cancelar(): void {
    this.router.navigate(['/clientes', this.clienteId, 'solicitudes']);
  }
}
