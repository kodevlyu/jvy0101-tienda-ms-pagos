# ms-pagos

Microservicio REST de la tienda para registrar y consultar los **pagos** asociados a los pedidos. Forma parte de la Evaluación Parcial N°2 de JVY0101. Cada pago guarda el `pedidoId` como referencia: los pedidos viven en otro microservicio (ms-pedidos) con su propia base de datos y se comunican por REST.

## Tecnologías
- Java 17
- Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA, Validation)
- PostgreSQL 16 (Docker)
- Maven (Maven Wrapper `mvnw`)

## Requisitos
- JDK 17
- Docker Desktop
- Git
- Postman

## Clonar el repositorio
```bash
git clone https://github.com/kodevlyu/jvy0101-tienda-ms-pagos.git
cd jvy0101-tienda-ms-pagos
```

## Configurar la base de datos
```bash
docker run --name pg-ms -e POSTGRES_USER=ms_user -e POSTGRES_PASSWORD=ms_pass123 -e POSTGRES_DB=pagos_db -p 5432:5432 -d postgres:16
```
Si el contenedor `pg-ms` ya existe, basta con `docker start pg-ms`. La tabla `pagos` la crea Hibernate al iniciar.

## Configuración (`src/main/resources/application.properties`)
```properties
spring.application.name=ms-pagos
server.port=8084
spring.datasource.url=jdbc:postgresql://localhost:5432/pagos_db
spring.datasource.username=ms_user
spring.datasource.password=ms_pass123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

## Compilar y ejecutar
```powershell
.\mvnw clean
.\mvnw install
.\mvnw package
java -jar target\ms-pagos-0.0.1-SNAPSHOT.jar
```
El servicio queda en `http://localhost:8084`. La base debe estar corriendo antes de compilar.

## Endpoints (`/api/pagos`)
| Método | Ruta | Descripción | Respuesta |
|--------|------|-------------|-----------|
| POST | `/api/pagos` | Registrar un pago (estado PENDIENTE por defecto) | 201, 400 |
| GET | `/api/pagos` | Listar pagos | 200 |
| GET | `/api/pagos/{id}` | Buscar pago por id | 200, 404 |
| GET | `/api/pagos/pedido/{pedidoId}` | Listar pagos de un pedido | 200 |
| PUT | `/api/pagos/{id}` | Actualizar un pago | 200, 400, 404 |
| DELETE | `/api/pagos/{id}` | Eliminar un pago | 204, 404 |

Ejemplo `POST /api/pagos`:
```json
{
  "pedidoId": 1,
  "monto": 15990,
  "metodo": "TARJETA"
}
```

Ejemplo de error 400:
```json
{
  "mensaje": "Error de validación",
  "errores": [
    "metodo: El metodo de pago es obligatorio",
    "monto: El monto debe ser mayor a 0"
  ],
  "status": 400
}
```

## Estructura
```
cl.duoc.mspagos
├── controller   (PagoController)
├── service      (PagoService)
├── repository   (PagoRepository)
├── model        (Pago)
└── exception    (RecursoNoEncontradoException, GlobalExceptionHandler)
```

## Autora
kodevlyu