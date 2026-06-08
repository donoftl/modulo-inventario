# Módulo de Inventario de Activos

Este proyecto es una solución integral para la gestión de inventarios, compuesta por un backend robusto en Spring Boot y un frontend dinámico en Angular.

## 🚀 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:
*   **Java 17** o superior.
*   **Node.js** (versión LTS recomendada) y npm.
*   **Angular CLI** (`npm install -g @angular/cli`).
*   **Docker** y **Docker Compose** (para la base de datos).
*   **Maven** (opcional, se puede usar el wrapper `mvnw`).

---

## 🛠️ Configuración del Backend (Spring Boot)

El backend está configurado para conectar con una base de datos MariaDB y utiliza JWT para la seguridad.

1.  **Levantar la Base de Datos:**
    Navega a la carpeta del backend y utiliza Docker Compose para iniciar el contenedor de MariaDB:
    ```bash
    cd backend-inventario
    docker-compose up -d
    ```
    *Nota: Esto creará una base de datos llamada `inventario_db` en el puerto `3306` con usuario/password: `root/root`.*

2.  **Configuración de Propiedades:**
    Revisa el archivo `src/main/resources/application.yml` para ajustar credenciales si es necesario. Por defecto, ya está configurado para el contenedor Docker.

3.  **Ejecutar la Aplicación:**
    Usa el siguiente comando para iniciar el servidor:
    ```bash
    ./mvnw spring-boot:run
    ```
    El servidor estará disponible en: `http://localhost:8080/inventario`

---

## � Documentación de la API

Para facilitar las pruebas de los endpoints, puedes utilizar cualquiera de las siguientes opciones:

### 1. Swagger UI (OpenAPI)
Si la aplicación está en ejecución, puedes acceder a la interfaz interactiva en:
`http://localhost:8080/inventario/swagger-ui/index.html`

### 2. Colección de Postman
En la raíz del proyecto (o en la carpeta `/docs`) se incluye el archivo `Inventario_Activos.postman_collection.json`.
1. Abre Postman.
2. Haz clic en **Import**.
3. Arrastra el archivo JSON para cargar todas las rutas (Activos, Categorías, Auth) automáticamente.

---

## �💻 Configuración del Frontend (Angular)

El frontend es una aplicación SPA que interactúa con la API REST del backend.

1.  **Instalar Dependencias:**
    Navega a la carpeta del frontend e instala los paquetes necesarios:
    ```bash
    cd frontend-inventario
    npm install
    ```

2.  **Ejecutar en Desarrollo:**
    Inicia el servidor de desarrollo de Angular:
    ```bash
    ng serve
    ```
    La aplicación estará disponible en: `http://localhost:4200`

---

## 📝 Notas de Implementación

*   **Seguridad (JWT):** El sistema utiliza tokens JWT para la autenticación. La clave secreta y el tiempo de expiración (24h) están definidos en el `application.yml` del backend.
*   **Persistencia:** Se utiliza Hibernate con `ddl-auto: update`, por lo que las tablas se crearán automáticamente al iniciar el backend por primera vez.
*   **Context Path:** Ten en cuenta que todos los endpoints del backend comienzan con el prefijo `/inventario/api/`.
*   **Locales:** El frontend incluye configuraciones regionales para múltiples países de habla hispana (es-CO, es-MX, es-PA, etc.) para el manejo de monedas y fechas.

## 📂 Estructura del Proyecto

*   `/backend-inventario`: Código fuente Java, configuración de Spring Boot y Docker Compose.
*   `/frontend-inventario`: Aplicación Angular, componentes, servicios y estilos.

---
Desarrollado como parte del Módulo de Inventario de Activos.