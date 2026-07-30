export type EstadoSolicitud = 'PENDIENTE' | 'APROBADA' | 'RECHAZADA';

export interface Solicitud {
  id?: number;
  clienteId: number;
  clienteNombreCompleto?: string;
  montoSolicitado: number;
  plazoMeses: number;
  motivo?: string;
  estado?: EstadoSolicitud;
  fechaSolicitud?: string;
  tasaInteres?: number;
  montoTotalPagar?: number;
  totalPagado?: number;
  saldoPendiente?: number;
  fechaResolucion?: string;
  comentarioResolucion?: string;
}

export interface Pago {
  id?: number;
  solicitudId?: number;
  monto: number;
  fechaPago?: string;
  observacion?: string;
}
