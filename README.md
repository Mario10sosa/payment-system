# 💳 PaymentSystem
 
Aplicación web de procesamiento seguro de pagos en línea, desarrollada con **Spring Boot** y **React**. Permite a usuarios registrados realizar transacciones a través de múltiples métodos de pago, consultar su historial y repetir pagos previos de forma ágil.
 
El proyecto aplica de forma práctica cinco **patrones de diseño creacionales y de comportamiento**: Abstract Factory, Factory Method, Template Method, Builder y Prototype.
 
---
 
## 🚀 Tecnologías utilizadas
 
### Backend
| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 4.0.3 | Framework backend |
| Spring Security | — | Autenticación y autorización |
| Spring Data JPA | — | Persistencia y repositorios |
| MySQL | — | Base de datos relacional |
| JWT (jjwt) | 0.11.5 | Tokens de autenticación stateless |
| Lombok | — | Reducción de código boilerplate |
| Maven | — | Gestión de dependencias |
 
### Frontend
| Tecnología | Versión | Uso |
|---|---|---|
| React | 18 | Framework de interfaz de usuario |
| Vite | — | Bundler y servidor de desarrollo |
| Axios | — | Cliente HTTP |
| Bootstrap 5 | — | Estilos y componentes UI |
 
---
 
## ⚙️ Requisitos previos
 
Antes de ejecutar el proyecto asegúrate de tener instalado:
 
- [Java 17+](https://adoptium.net/)
- [Node.js 18+](https://nodejs.org/)
- [MySQL 8+](https://www.mysql.com/)
- [Maven 3.8+](https://maven.apache.org/)
 
---
 
## 🗄️ Configuración de la base de datos
 
1. Crea la base de datos en MySQL:
 
```sql
CREATE DATABASE PAGOS_DB;
```
 
2. Verifica que las credenciales en `backend/src/main/resources/application.properties` coincidan con tu entorno:
 
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/PAGOS_DB
spring.datasource.username=root
spring.datasource.password=12345
spring.jpa.hibernate.ddl-auto=update
```
 
> Las tablas `users` y `payments` se crean automáticamente al levantar el backend por primera vez gracias a `ddl-auto=update`.
 
---
 
## 🔧 Instalación y ejecución
 
### 1. Clonar el repositorio
 
```bash
git clone https://github.com/Mario10sosa/payment-system.git
cd payment-system
```
 
### 2. Levantar el Backend
 
```bash
cd backend
./mvnw spring-boot:run
```
 
El servidor quedará disponible en: `http://localhost:8080`
 
### 3. Levantar el Frontend
 
```bash
cd frontend
npm install
npm run dev
```
 
La aplicación quedará disponible en: `http://localhost:5173`
 
---
 
## 🔐 Autenticación
 
El sistema utiliza **JWT (JSON Web Token)** para la autenticación stateless. Al registrarse o iniciar sesión, el servidor retorna un token que debe enviarse en el header de cada petición protegida:
 
```
Authorization: Bearer <token>
```
 
Los tokens tienen una validez de **24 horas**.
 
---
 
## 📡 Endpoints principales
 
### Autenticación — `/api/auth`
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/register` | Registro de nuevo usuario | ❌ |
| POST | `/login` | Inicio de sesión | ❌ |
| GET | `/profile` | Perfil del usuario logueado | ✅ |
 
### Pagos — `/api/payments`
| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/pay` | Procesar un pago | ✅ |
| POST | `/clone?newAmount=` | Clonar pago con nuevo monto | ✅ |
| POST | `/clone-method?newMethod=` | Clonar pago con nuevo método | ✅ |
| GET | `/list` | Listar todos los pagos | ✅ |
| GET | `/my-payments` | Pagos del usuario autenticado | ✅ |
| GET | `/methods` | Métodos de pago disponibles | ✅ |
 
---
 
## 🎨 Métodos de pago soportados
 
| ID | Método | Datos requeridos |
|---|---|---|
| `CARD` | Tarjeta crédito/débito | Número (16 dígitos), CVV (3 dígitos), vencimiento, titular |
| `PAYPAL` | PayPal | Email válido |
| `NEQUI` | Nequi | Teléfono (10 dígitos) |
| `DAVIPLATA` | Daviplata | Teléfono (10 dígitos) |
| `CRYPTO` | Criptomonedas | Wallet (≥26 caracteres), moneda (BTC, ETH, USDT) |
 
---
 
## 🧩 Patrones de diseño implementados
 
| Patrón | Clase(s) principal(es) | Descripción |
|---|---|---|
| **Abstract Factory** | `PaymentAbstractFactory`, `CardAbstractFactory`, ... | Crea familias de objetos por método de pago |
| **Factory Method** | `PaymentCreator`, `CreditCardCreator`, ... | Delega la instanciación del `Payment` a cada subclase |
| **Template Method** | `Payment.processPayment()` | Define el esqueleto del flujo de pago |
| **Builder** | `PaymentResponse.Builder` | Construye la respuesta de pago paso a paso |
| **Prototype** | `PaymentRequest.clone()` | Clona solicitudes para reutilizar datos de pago |
 
---
 
## 📁 Estructura del proyecto
 
```
payment-system/
├── backend/
│   └── src/main/java/com/pagos/
│       ├── config/          # Configuración de base de datos
│       ├── controller/      # PaymentController
│       ├── model/           # PaymentRequest, PaymentResponse (Builder, Prototype)
│       ├── payment/         # Patrones: Abstract Factory, Factory Method, Template Method
│       ├── repository/      # PaymentRepository, UserRepository
│       ├── security/        # JWT: JwtUtil, JwtFilter, SecurityConfig
│       ├── service/         # PaymentService, UserService
│       └── user/            # User, UserController, UserService
└── frontend/
    └── src/
        ├── components/      # LoginForm, RegisterForm, PaymentForm
        │   └── methods/     # CardForm, OtherForms
        ├── context/         # AuthContext
        └── services/        # paymentService.js, userService.js
```
 
---
 
## 👤 Autor
 
**Mario Sosa**
- GitHub: [@Mario10sosa](https://github.com/Mario10sosa)
- Github: [@CamiloCodex]
 
---
 
> Proyecto desarrollado como ejercicio académico de aplicación práctica de patrones de diseño de software.
