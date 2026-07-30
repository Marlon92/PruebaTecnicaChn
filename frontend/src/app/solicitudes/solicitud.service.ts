import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_URL } from '../api-config';
import { Pago, Solicitud } from './solicitud.model';

@Injectable({ providedIn: 'root' })
export class SolicitudService {

  private readonly baseUrl = `${API_URL}/solicitudes`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(this.baseUrl);
  }

  listarPorCliente(clienteId: number): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(`${this.baseUrl}/cliente/${clienteId}`);
  }

  obtener(id: number): Observable<Solicitud> {
    return this.http.get<Solicitud>(`${this.baseUrl}/${id}`);
  }

  crear(solicitud: Solicitud): Observable<Solicitud> {
    return this.http.post<Solicitud>(this.baseUrl, solicitud);
  }

  aprobar(id: number, tasaInteres: number, comentario: string): Observable<Solicitud> {
    return this.http.put<Solicitud>(`${this.baseUrl}/${id}/aprobar`, { tasaInteres, comentario });
  }

  rechazar(id: number, comentario: string): Observable<Solicitud> {
    return this.http.put<Solicitud>(`${this.baseUrl}/${id}/rechazar`, { comentario });
  }

  listarPagos(solicitudId: number): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.baseUrl}/${solicitudId}/pagos`);
  }

  registrarPago(solicitudId: number, pago: Pago): Observable<Pago> {
    return this.http.post<Pago>(`${this.baseUrl}/${solicitudId}/pagos`, pago);
  }
}
