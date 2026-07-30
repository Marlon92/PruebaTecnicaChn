# Sistema Prestamos Bancarios --- Prueba Técnica CHN

Aplicación para poder gestión de clientes y para realizar solicitudes de préstamos y gestión de pagos.

EL objetivo del programa realizado es que un cliente pueda solicitar un préstamo, realizando una solicitud y posterior a esto, esta solicitud entra en una etapa de revisión, donde se aprueba/rechaza el préstamo. En caso se aprueba la solicitud, se podrá visualizar en un módulo de solicitudes aprobadas y se permitirá realizar el pago del prestamo otorgado.
En caso se rechaza, únicamente queda la información de la solicitud con un estado Rechazado.

## Tecnologias

El backend está con spring boot y java 21, la arquitectura es monolitica modular. Aquí se realiza el proceso para el CRUD de clientes y para el almacenamiento, aprobación y rechazo de solicitud, también la realización de pagos.

En Base de datos se utilizó SQL Server y la lógica de negocio del sistema se realizó dentro de Stored Procedures. De esta forma la aplicación solo se encarga de consumir los SPS creados, en dado caso el SP reciba parámetros de entrada, la aplicación se los enviará desde cada clase de repositorio, el SP devolverá una respuesta que ya la aplicación formateará para presentar al usuario.

El Frontend se hizo con Angular y Angular Material para el tema de diseños

Usamos Docker para contenirizar y así mantener cada aplicación en su propio ambiente y aislada.

Por último se utilizó Docker Compose para orquestar nuestros contenedores, de esta forma no tenemos que administrar cada contenedor individualmente.

## Como levantar el proyecto

Al tener los proyectos desarrollados, construímos el contenedor con el siguiente comando:

**docker compose up -d --build**

Esto levanta 3 contenedores que tenemos registrados en nuestro docker compose
1 Backend
2 Frontend
3 Base de datos

El puerto de SQL Server es **1433**
La url del backend es **"http/localhost:8080"**
la url del frontend es **"http://localhost:4200"**

Para hacer la funcionalidad más practica, se subieron las imagenes de los contenedores a **Docker Hub**, de esta forma si alguien quiere utilizarla puede hacer la llamada desde Docker Hub, para este ejemplo se realizó por medio de un compose.yaml que obtiene la imagen del repositorio de docker.

## Pasos para Levantar el proyecto a partir de una imagen

Para poder obtener las imagenes de Docker hub y levantarlas en un nuevo ambiente hacer esto:

*En la ruta backend/database hay un archivo compose.yaml, ese archivo ya apunta a las imagenes subidas a docker hub, solo se necesita utilizar el siguiente comando dentro de la carpeta donde se encuentre el compose.yaml y ya se ejecutará la creación de los contenedores.

docker compose up -d

Luego de generados los contenedores, es necesario que se pueda acceder a la base de datos 
ya sea, por medio de SQL SERVER MANAGMENT o por medio de Visual Studio Code a través de la extension de sql server MSSQL establecer conexión con base a los siguientes parámetros:
user: sa
host: localhost,1433

y ejecutar manualmente los scripts **tablas.sql y procedimientos.sql**

Posterior a ejecutar los scripts ya sea puede abrir la aplicación mediante la url **http://localhost:4200** e interactuar con la aplicación.

**Listado de clientes**
<img width="1501" height="510" alt="Captura de pantalla 2026-07-29 a la(s) 11 13 56 p  m" src="https://github.com/user-attachments/assets/7b2d5423-12b8-47b7-863f-bf6947db3e6a" />

**NUEVO CLIENTE**

<img width="1501" height="510" alt="Captura de pantalla 2026-07-29 a la(s) 11 13 56 p  m" src="https://github.com/user-attachments/assets/7b2d5423-12b8-47b7-863f-bf6947db3e6a" />

Entre los clientes tenemos el botón de ver solicitudes

<img width="1206" height="413" alt="Captura de pantalla 2026-07-29 a la(s) 11 16 28 p  m" src="https://github.com/user-attachments/assets/e53075bd-989e-4b16-8dc2-0953f6a8879a" />

El cual nos muestra las solicitudes que tiene el cliente y nos permite crear una nueva

<img width="1497" height="396" alt="Captura de pantalla 2026-07-29 a la(s) 11 17 10 p  m" src="https://github.com/user-attachments/assets/416daaf3-9566-4295-95f6-d76b4b18c513" />

<img width="880" height="474" alt="Captura de pantalla 2026-07-29 a la(s) 11 18 02 p  m" src="https://github.com/user-attachments/assets/2778c52c-c397-4a7e-8913-838d1dcb1914" />

**Solicitudes de Préstamo**
Se visualizan las solicitudes ya sea pendientes, aprobadas o rechazadas.

<img width="1484" height="360" alt="Captura de pantalla 2026-07-29 a la(s) 11 18 26 p  m" src="https://github.com/user-attachments/assets/99083e8a-b583-46d8-98b7-9097a3736047" />

**Prestamos Aprobados**

Pantalla que muestra las solicitudes aprobadas.

<img width="1414" height="334" alt="Captura de pantalla 2026-07-29 a la(s) 11 20 23 p  m" src="https://github.com/user-attachments/assets/7f27a1c0-bd55-47d4-b2bd-6fa9b1d076e2" />

Al ver las **Acciones** nos da la opción de ver nuestra solicitud, el detalle de la misma y poder realizar pagos si esta está aprobada.

<img width="1056" height="778" alt="Captura de pantalla 2026-07-29 a la(s) 11 21 01 p  m" src="https://github.com/user-attachments/assets/91570b01-a5ef-49f2-80e8-e83ade1ab7e7" />

Si la solicitud fue rechazada, solo muestra información de la misma, más no la opción de realizar pago.

<img width="958" height="467" alt="Captura de pantalla 2026-07-29 a la(s) 11 22 23 p  m" src="https://github.com/user-attachments/assets/ce634dbe-c178-4876-8b36-8d80a693abb7" />








