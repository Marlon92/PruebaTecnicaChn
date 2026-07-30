import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const mensaje = error.error?.mensaje || 'Ocurrio un error al comunicarse con el servidor';
      snackBar.open(mensaje, 'Cerrar', { duration: 4000 });
      return throwError(() => error);
    })
  );
};
