# Guía de Uso y Configuración - ServiceFlow

Bienvenido a la guía detallada de ServiceFlow. Este documento está pensado como manual técnico para levantar, configurar y utilizar el sistema localmente o en un servidor.

---

## 1. Ejecución con Docker (Método Recomendado)

Si tienes **Docker** y **Docker Compose** instalados, el proyecto incluye una configuración multi-stage que construye y ejecuta la aplicación de forma automática, además de levantar la base de datos necesaria.

### Comandos principales:

1. **Construir y levantar todo en segundo plano:**
   ```bash
   docker-compose up -d --build
   ```
2. **Ver los logs de la aplicación:**
   ```bash
   docker-compose logs -f backend
   ```
3. **Apagar y destruir contenedores (sin borrar datos):**
   ```bash
   docker-compose down
   ```
4. **Apagar y borrar base de datos (Reiniciar desde cero):**
   ```bash
   docker-compose down -v
   ```

---

## 2. Ejecución Local (Desarrollo)

Si prefieres ejecutar la aplicación sin Docker, necesitarás lo siguiente:

### Prerrequisitos
- **Java 21** o superior.
- **PostgreSQL 16** o superior ejecutándose en tu puerto `5432`.

### Pasos
1. **Crear la Base de Datos:**
   ```sql
   CREATE DATABASE serviceflow;
   ```
   *(La contraseña por defecto configurada en el proyecto es `12345` para el usuario `postgres`).*

2. **Ejecutar la API:**
   Usa el wrapper de Maven incluido en el proyecto:
   ```bash
   # En Windows
   .\mvnw.cmd spring-boot:run
   
   # En Linux / macOS
   ./mvnw spring-boot:run
   ```

---

## 3. Credenciales y Semilla (Seed)

El sistema utiliza **Flyway** para gestionar la base de datos. La primera vez que el sistema se conecta a la base de datos, creará las tablas e insertará un **usuario administrador por defecto**:

- **Email de acceso:** `admin@serviceflow.com`
- **Contraseña:** `admin123`

---

## 4. Cómo probar la API

Una vez iniciada la aplicación (ya sea por Docker o localmente), el servidor estará escuchando en el puerto `8080`.

### A) Interfaz Visual (Swagger UI)
La forma más sencilla de ver e interactuar con los endpoints es usar la interfaz visual generada automáticamente:

**http://localhost:8080/swagger-ui.html**

**Cómo autenticarte en Swagger:**
1. Ve al endpoint `POST /api/auth/login`.
2. Haz clic en "Try it out" y envía el JSON con el email y contraseña del admin.
3. Copia el valor de la propiedad `token` de la respuesta.
4. Sube al inicio de Swagger, haz clic en el botón verde **Authorize**.
5. Escribe `Bearer ` seguido de un espacio y pega tu token. ¡Listo! Ahora puedes usar los endpoints restringidos.

---

## 5. Variables de Entorno y Seguridad

Para evitar hardcodear contraseñas en el código o en Docker, el proyecto utiliza un archivo `.env`.

Al clonar el proyecto, encontrarás un archivo plantilla llamado `.env.example`. 
**Debes renombrarlo o copiarlo como `.env`** y colocar allí tus verdaderas contraseñas:

```env
DB_USER=postgres
DB_PASSWORD=tu_password_aqui
DB_NAME=serviceflow
JWT_SECRET=tu_clave_secreta_en_produccion_aqui
JWT_EXPIRATION=86400000
```

> **Nota:** El archivo `.env` está ignorado en Git (`.gitignore`) por motivos de seguridad, asegurando que tus secretos de producción nunca se suban al repositorio público.
