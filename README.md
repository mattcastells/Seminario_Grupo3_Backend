# SIP3 Backend

Backend para la aplicación móvil de marketplace de oficios. Implementado con Java 21, Spring Boot 3 y MongoDB.

## Requisitos previos

- Java 21
- Maven 3.9+
- MongoDB local o instancia en MongoDB Atlas

## Configuración rápida

1. Clonar o copiar el proyecto.
2. Configurar variables de entorno necesarias:
   - `MONGODB_URI`: cadena de conexión MongoDB (Atlas o local).
   - `JWT_SECRET`: secreto base64 para firmar JWT.
   - `JWT_EXPIRATION_MS` (opcional): tiempo de expiración en milisegundos.
3. Ejecutar:

```bash
mvn spring-boot:run
```

El servidor escucha por defecto en `http://localhost:8080`.

## Perfil Docker

Para ejecutar con Docker Compose (API + MongoDB local):

```bash
docker compose up --build
```

Se expondrán los puertos `8080` (API) y `27017` (MongoDB).

## Variables de entorno

| Variable | Descripción |
| --- | --- |
| `MONGODB_URI` | Cadena de conexión, ej: `mongodb+srv://<user>:<password>@<cluster>/<db>?retryWrites=true&w=majority` |
| `JWT_SECRET` | Secreto base64 usado para firmar JWT |
| `JWT_EXPIRATION_MS` | (Opcional) Tiempo de validez del token. Default `86400000` (1 día) |

## Colecciones principales

- `users`: credenciales, roles y perfiles.
- `professionals`: información pública de profesionales.
- `service_orders`: solicitudes/contrataciones creadas desde el formulario "Contratar".
- `messages`: conversaciones asociadas a un service order.
- `payments`: registros de cobros.
- `reviews`: reseñas de profesionales.

## Endpoints principales

### Autenticación
- `POST /auth/register`
- `POST /auth/login`
- `GET /auth/me`

### Usuarios
- `GET /api/users` (ADMIN)
- `GET /api/users/{id}`
- `PUT /api/users/me`

### Profesionales
- `GET /api/professionals`
- `GET /api/professionals/{id}`
- `GET /api/professionals/by-user/{userId}`
- `POST /api/professionals`
- `PUT /api/professionals/{id}`
- `DELETE /api/professionals/{id}`

### Service Orders (Contrataciones / Mis Trabajos)
- `POST /api/service-orders`
- `GET /api/service-orders/{id}`
- `GET /api/service-orders/me`
- `GET /api/service-orders/professional/{professionalId}`
- `PUT /api/service-orders/{id}`
- `DELETE /api/service-orders/{id}`

### Chat / Mensajes
- `GET /api/service-orders/{serviceOrderId}/messages`
- `POST /api/service-orders/{serviceOrderId}/messages`

### Pagos
- `POST /api/payments`
- `GET /api/payments/{id}`
- `GET /api/payments`
- `GET /api/payments/service-order/{serviceOrderId}`
- `PUT /api/payments/{id}`
- `DELETE /api/payments/{id}`

### Reseñas
- `POST /api/reviews`
- `GET /api/reviews?professionalId=...`
- `DELETE /api/reviews/{id}`

### Uploads
- `POST /api/uploads` (devuelve URL ficticia para archivos)

## Colección de Postman / Pruebas rápidas

### Registro
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "email": "usuario@example.com",
    "phone": "+541112345678",
    "password": "Password123",
    "fullName": "Usuario Demo",
    "registerAsProfessional": true
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"usuario","password":"Password123"}'
```

### Crear contratación
```bash
curl -X POST http://localhost:8080/api/service-orders \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "professionalId": "<id-profesional>",
    "contactName": "Juan Perez",
    "contactPhone": "+5491112345678",
    "contactEmail": "juan@example.com",
    "address": "Av. Corrientes 1234",
    "serviceType": "Destapación",
    "description": "Hay una pérdida en el techo de la cocina",
    "preferredDate": "2024-10-25",
    "budget": 15000,
    "paymentPreference": "card"
  }'
```

## Ejemplos de documentos

```json
// users
{
  "_id": "66f1a8d9e4b0c72c12345678",
  "username": "usuario",
  "email": "usuario@example.com",
  "phone": "+5491112345678",
  "password": "$2a$10$...",
  "roles": ["USER", "PROFESSIONAL"],
  "profile": {
    "fullName": "Usuario Demo",
    "location": "CABA",
    "phone": "+5491112345678",
    "email": "usuario@example.com",
    "preferredPaymentMethods": ["efectivo", "mercadopago"]
  },
  "active": true,
  "createdAt": "2024-09-24T13:45:00Z",
  "updatedAt": "2024-09-24T13:45:00Z"
}

// professionals
{
  "_id": "66f1a8d9e4b0c72c87654321",
  "userId": "66f1a8d9e4b0c72c12345678",
  "displayName": "Manolo Cáceres",
  "profession": "Plomero",
  "summary": "Plomero matriculado especializado en instalaciones y reparaciones",
  "biography": "Más de 15 años realizando trabajos de plomería en CABA.",
  "experienceYears": 15,
  "services": ["Instalación de cañerías", "Destapaciones"],
  "tags": ["Verificado", "Popular"],
  "rating": 4.7,
  "reviewsCount": 12,
  "distanceKm": 4.0,
  "address": "CABA",
  "minRate": 8000,
  "maxRate": 25000,
  "contactEmail": "manolo@example.com",
  "contactPhone": "+5491112345678",
  "paymentMethods": ["mercadopago", "efectivo"],
  "availableJobs": ["Plomería", "Gas"],
  "verificationStatus": {
    "faceVerified": true,
    "dniFrontVerified": true,
    "dniBackVerified": true
  },
  "active": true,
  "featured": true,
  "createdAt": "2024-09-24T14:10:00Z",
  "updatedAt": "2024-09-24T14:10:00Z"
}

// service_orders
{
  "_id": "66f1a8d9e4b0c72cabcd1234",
  "userId": "usuario",
  "professionalId": "66f1a8d9e4b0c72c87654321",
  "contactName": "Juan Perez",
  "contactPhone": "+5491112345678",
  "contactEmail": "juan@example.com",
  "address": "Av. Corrientes 1234",
  "serviceType": "Destapación",
  "description": "Losa del techo con gotera cercana al calefón.",
  "preferredDate": "2024-10-25",
  "budget": 15000,
  "paymentPreference": "card",
  "status": "PENDING",
  "scheduledAt": null,
  "lastMessagePreview": "Perfecto, llevo los materiales que necesitamos",
  "lastMessageAt": "2024-09-24T15:12:00Z",
  "createdAt": "2024-09-24T15:00:00Z",
  "updatedAt": "2024-09-24T15:12:00Z"
}

// messages
{
  "_id": "66f1a8d9e4b0c72cffed4321",
  "serviceOrderId": "66f1a8d9e4b0c72cabcd1234",
  "senderType": "PROFESSIONAL",
  "senderId": "66f1a8d9e4b0c72c87654321",
  "content": "Hola, ¿podés mandarme una foto de la pérdida?",
  "attachmentUrl": null,
  "createdAt": "2024-09-24T15:05:00Z"
}

// payments
{
  "_id": "66f1a8d9e4b0c72c99998888",
  "serviceOrderId": "66f1a8d9e4b0c72cabcd1234",
  "userId": "usuario",
  "professionalId": "66f1a8d9e4b0c72c87654321",
  "amount": 15000,
  "currency": "ARS",
  "method": "CARD",
  "status": "SUCCESS",
  "providerReference": "1f72b8cb-3c2d-4eb5-9f39-99c4f3a45c88",
  "cardLastFour": "4242",
  "notes": null,
  "createdAt": "2024-09-24T15:20:00Z",
  "updatedAt": "2024-09-24T15:20:02Z",
  "processedAt": "2024-09-24T15:20:02Z"
}

// reviews
{
  "_id": "66f1a8d9e4b0c72c55556666",
  "professionalId": "66f1a8d9e4b0c72c87654321",
  "userId": "usuario",
  "userDisplayName": "usuario",
  "rating": 5,
  "comment": "Excelente servicio, muy puntual y prolijo.",
  "createdAt": "2024-09-24T16:00:00Z"
}
```

## Cambiar a MongoDB Atlas

Modificar la variable `MONGODB_URI` con la cadena provista por Atlas, por ejemplo:

```bash
export MONGODB_URI="mongodb+srv://sip3:<password>@cluster0.mongodb.net/sip3?retryWrites=true&w=majority"
```

El backend se conectará automáticamente usando esa URI. Para Compass basta con usar la misma cadena de conexión.
