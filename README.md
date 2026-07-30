# Sistema Prestamos Bancarios --- Prueba Técnica CHN

Se gestionan los clientes, solicitudes de préstamoy pagos.

## Tecnologias

El backend está con spring boot y java 21, la arquitectura es monolita modular. Aquí se realiza el proceso para el CRUD de clientes y para el almacenamiento, aprobación y rechazo de solicitud, también la realización de pagos.

En Base de datos se utilizó SQL Server y la lógica de negocio del sistema se realizó dentro de Stored Procedures.
El Frontend se hizo con Angular y Angular Material para el tema de diseños

Usamos Docker para contenirizar.

## Como levantar el proyecto

Al tener los proyectos desarrollados, construímos el contenedor con el siguiente comando.
docker compose up --build

Esto levanta 3 contenedores que tenemos registrados en nuestro docker compose
1 Backend
2 Frontend
3 Base de datos

El puerto de SQL Server es 1433
La url del backend es "http/localhost:8080
la url del frontend es "http://localhost:4200


Para poder obtener las imagenes de Docker hub y levantarlas en un nuevo ambiente hacer esto:

*En la ruta backend/database hay un archivo compose.yaml, ese archivo ya apunta a las imagenes subidas a docker hub, solo necesitas utilizar el siguiente comando dentro de la carpeta donde se encuentre el compose.yaml y ya se ejecutará la creación de los contenedores.

docker compose up -d


Luego de generados los contenedores, es necesario que se pueda acceder a la base de datos y ejecutar manualmente los scripts tablas.sql y procedimientos.sql

