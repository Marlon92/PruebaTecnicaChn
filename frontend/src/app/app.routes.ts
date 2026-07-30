import { Routes } from '@angular/router';

import { ClienteListComponent } from './clientes/cliente-list/cliente-list.component';
import { ClienteFormComponent } from './clientes/cliente-form/cliente-form.component';
import { SolicitudListComponent } from './solicitudes/solicitud-list/solicitud-list.component';
import { SolicitudFormComponent } from './solicitudes/solicitud-form/solicitud-form.component';
import { SolicitudDetalleComponent } from './solicitudes/solicitud-detalle/solicitud-detalle.component';

export const routes: Routes = [
  { path: '', redirectTo: 'clientes', pathMatch: 'full' },

  { path: 'clientes', component: ClienteListComponent },
  { path: 'clientes/nuevo', component: ClienteFormComponent },
  { path: 'clientes/:id/editar', component: ClienteFormComponent },
  { path: 'clientes/:clienteId/solicitudes', component: SolicitudListComponent },
  { path: 'clientes/:clienteId/solicitudes/nueva', component: SolicitudFormComponent },

  { path: 'solicitudes', component: SolicitudListComponent },
  { path: 'solicitudes/:id', component: SolicitudDetalleComponent },

  { path: '**', redirectTo: 'clientes' }
];
