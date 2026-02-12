# Usuarios Service

Microservicio de gestion de usuarios para la plazoleta de comidas.
Implementa HU1 (crear propietario) y HU5 (autenticacion por correo y clave).
Incluye HU6 (crear cuenta empleado).

## Arquitectura

Hexagonal (puertos y adaptadores).

- Dominio: `src/main/java/com/plazoleta/usuarios/domain`
- Aplicacion (use cases): `src/main/java/com/plazoleta/usuarios/application`
- Adaptadores de entrada (web): `src/main/java/com/plazoleta/usuarios/adapters/in`
- Adaptadores de salida (persistencia, seguridad): `src/main/java/com/plazoleta/usuarios/adapters/out`
- Infraestructura (config, seguridad): `src/main/java/com/plazoleta/usuarios/infrastructure`

## Requisitos

- Java 25
- PostgreSQL
- Gradle

## Configuracion

Editar `src/main/resources/application.properties`:

```properties
server.port=8081
spring.datasource.url=jdbc:postgresql://localhost:5432/db_usuarios
spring.datasource.username=postgres
spring.datasource.password=Admin123
security.jwt.secret=plazoleta-local-secret-2026-04-08-01
security.jwt.expiration-seconds=3600
```

El secreto JWT debe tener longitud suficiente para HMAC.

## Seguridad

Se usa JWT en el header `Authorization: Bearer <token>`.
El token incluye:

- `sub` con el correo del usuario
- `roles` con el rol del usuario
- `userId` con el id del usuario

## Endpoints

- `POST /usuarios/login` (publico)
- `POST /usuarios/propietarios` (requiere rol ADMIN)
- `POST /usuarios/empleados` (requiere rol PROPIETARIO)
- `GET /usuarios/{id}/rol` (publico, consumo interno entre microservicios)

### Login

Request body:

```json
{
  "correo": "ana@example.com",
  "clave": "secreto"
}
```

Response:

```json
{
  "id": 1,
  "correo": "ana@example.com",
  "rol": "ADMIN",
  "token": "..."
}
```

### Crear propietario

Request body:

```json
{
  "nombre": "Ana",
  "apellido": "Garcia",
  "documentoIdentidad": "123456",
  "celular": "+573001112233",
  "fechaNacimiento": "1990-01-01",
  "correo": "ana@example.com",
  "clave": "secreto"
}
```

Validaciones:

- Email con formato valido.
- Celular maximo 13 caracteres, permite +.
- Documento de identidad numerico.
- Mayor de edad.
- Clave se almacena con bcrypt.

### Crear empleado

Request body:

```json
{
  "nombre": "Luis",
  "apellido": "Perez",
  "documentoIdentidad": "987654",
  "celular": "+573001112244",
  "correo": "luis@example.com",
  "clave": "secreto",
  "idRol": "EMPLEADO"
}
```

Validaciones:

- Email con formato valido.
- Celular maximo 13 caracteres, permite +.
- Documento de identidad numerico.
- Rol debe ser EMPLEADO.
- Clave se almacena con bcrypt.

## OpenAPI

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Pruebas

```bash
./gradlew test
```

## Ejecutar

```bash
./gradlew bootRun
```
