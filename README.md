# User Management API
API REST profesional desarrollada con **Java** y **Spring Boot 3** para la gestión de usuarios. El proyecto implementa funcionalidades de filtrado dinámico, cifrado de contraseñas y empaquetado mediante contenedores.

---

## Funcionalidades

* **Operaciones CRUD Completas:** Creación, lectura, actualización y eliminación de usuarios.
* **Filtrado Dinámico:** Soporte para consultas personalizadas 
* **Seguridad:** Cifrado de contraseñas utilizando el algoritmo **AES-128** antes de su almacenamiento.
* **Documentación:** Integración de **Swagger/OpenAPI** para pruebas interactivas en tiempo real.
* **Contenedores:** Proyecto listo para desplegarse mediante **Docker**.
* **Pruebas Unitarias:** Cobertura de lógica de negocio con **JUnit 5** y **Mockito**.

---

## Stack Tecnológico

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.x
* **Gestor de Dependencias:** Maven
* **Documentación:** Springdoc OpenAPI
* **Virtualización:** Docker
* **Control de Versiones:** Git

---

### Requisitos previos
* Java 21 instalado o Docker Desktop.
* Maven 3.9 o superior.

### Opción 1: Ejecutar con Docker
Esta es la forma más sencilla de empaquetar y ejecutar la aplicación en un entorno aislado.

1. **Compilar el proyecto**
    ```bash
    mvn clean package -DskipTests
    ```
2.  **Construir la imagen:**
    ```bash
    docker build -t user-api .
    ```
3.  **Ejecutar el contenedor:**
    ```bash
    docker run -p 8080:8080 user-api
    ```

### Opción 2: Ejecutar localmente con Maven
    ```bash
    mvn spring-boot:run

    ```
---

# Documentación de la API (Swagger)
Una vez que la aplicación esté corriendo, puedes acceder a la documentación interactiva y probar los endpoints en:
    http://localhost:8080/swagger-ui/index.html

---

# Ejecución de Pruebas
Para validar la lógica y asegurar que no haya regresiones:

    ```bash
    mvn test
    ```

---
# Estructura del Proyecto
user-api/
├── src/main/java/      # Lógica de negocio (Controllers, Services, Models)
├── src/test/java/      # Pruebas unitarias y de integración
├── postman/            # Colección de Postman para pruebas externas
├── Dockerfile          # Configuración para empaquetado de imagen
├── pom.xml             # Configuración de dependencias Maven
└── README.md           # Documentación principal

---

## Enlace de postman collection
Para encontrar la coleccion en postman, tambien proporciono el enlace:
    https://crimson-space-1421582.postman.co/workspace/b06cbbdf-75ec-4113-8167-cb3608974694/collection/54316529-38413582-78d7-402f-8fc8-a8807db7dedb?action=share&source=copy-link&creator=54316529

---

## Repositorio del Proyecto
Puedes encontrar el código fuente y el historial de cambios en:
        https://github.com/GpeBP/user-api

Desarrollado por: **Guadalupe Becerril Padilla**