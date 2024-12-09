# Kata Banking Account


Claro, aquí tienes el texto mejorado y estructurado como lo pediste para el README:

Descripción del Proyecto
Este proyecto es una aplicación financiera que permite realizar transacciones bancarias entre cuentas, consultar los movimientos y dar de alta cuentas.

## Funcionalidad General

La aplicación ofrece dos formas de ejecución:

* Interactividad en consola (Permite realizar consultas de movimientos directamente desde la consola).
* API REST (Expone servicios para acceder a una funcionalidad más completa).

### 1 - La Interactividad en consola:

* Ingresar el CBU del cliente.
*  Elegir cómo desea visualizar los movimientos (por monto o por fecha).
*  Elegir el orden en el que se mostrarán los movimientos (ascendente o descendente).

Antes de efectuar las consultas, la aplicación precarga algunos datos de ejemplo para realizar pruebas, como cuentas y movimientos ficticios.

Una vez realizada la consulta, la aplicación pregunta al usuario si desea realizar otra búsqueda o salir de la aplicación.

### 2 - Consulta por Api Rest

Las operaciones que se pueden efectuar desde el swagger son las siguientes:
* Crear una nueva cuenta
* Obtener todas las cuentas
* Obtener los movimientos de una cuenta
* Transferir fondos entre cuentas
* Obtener todas las transacciones

## Puesta en marcha del servicio

### 1 - Docker
**Requisito:** Tener instalado `docker` y `docker-compose`

Antes que nada necesitamos crear la imagen:

```bash
docker build -t kata-bank-1.0.0 .
```

###  Para levantar la aplicacion en modo interartivo y hacer consultas desde la consola ejecutar lo siguiente:

```bash
docker run -it --name kata-bank-1.0.0 -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev kata-bank-1.0.0
```

###  Para poder efectuar consultar a traves de API rest, ejecutar lo siguiente:

```bash
docker run --name kata-bank-1.0.0 -p 8080:8080 kata-bank-1.0.0
```
### Nota aclaratoria:
Para alternar entre ejecuciones usando el mismo --name, es importante detener y eliminar el contenedor previamente, ya que Docker no permite crear dos contenedores con el mismo nombre. 
Sigue estos pasos:

docker stop <CONTAINER_ID>.

docker rm <CONTAINER_ID>

Puedes ver la documentación de la API en Swagger-UI accediendo a:

http://localhost:8080/swagger-ui/index.html

###  En modo normal sin docker, hacer las siguientes acciones:

```bash
  mvn clean install
```

- Ejecutar la aplicación

```bash
 mvn spring-boot:run
``` 