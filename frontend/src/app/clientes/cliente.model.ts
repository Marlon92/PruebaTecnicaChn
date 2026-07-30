export interface Cliente {
  id?: number;
  nombre: string;
  apellido: string;
  numeroIdentificacion: string;
  fechaNacimiento: string;
  direccion?: string;
  correo?: string;
  telefono?: string;
  fechaRegistro?: string;
}
