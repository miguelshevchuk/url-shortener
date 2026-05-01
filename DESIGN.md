# Diseño del Proyecto - URL Shortener

Este documento detalla las especificaciones técnicas, decisiones de diseño y arquitectura del proyecto URL Shortener.

## Prerrequisitos

Para levantar y desarrollar en este proyecto, se requiere:

- **Java 21**: El proyecto utiliza características de Java 21 (como Records).
- **Maven 3.8+**: Para la gestión de dependencias y construcción.
- **Docker y Docker Compose** (opcional): Para despliegue mediante contenedores.

## Formas de levantar el proyecto

### 1. Compilando localmente
Asegúrate de tener instalado Java 21 y Maven.

1.  **Compilar el proyecto:**
    ```bash
    ./mvnw clean package
    ```
2.  **Ejecutar la aplicación:**
    ```bash
    java -jar target/url-shortener-1.0.0.jar
    ```
    La aplicación estará disponible en: `http://localhost:8080`

### 2. Usando Docker
Asegúrate de tener instalado Docker y Docker Compose.

1.  **Levantar con Docker Compose:**
    ```bash
    docker compose up --build
    ```
    Esto compilará la imagen y levantará el contenedor en el puerto `8080`.

---

## Documentación de la API (Swagger/OpenAPI)

El proyecto incluye documentación interactiva mediante Swagger UI. Una vez levantada la aplicación, puedes acceder en:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec (JSON)**: `http://localhost:8080/v3/api-docs`

---

## Endpoints

### 1. Crear URL Corta
- **POST** `/api/v1/url`
- **Cuerpo (JSON)**:
    - `originalUrl`: (Obligatorio) URL que se desea acortar. Debe ser válida.
    - `shortCode`: (Opcional) Código personalizado deseado.
- **Descripción**: Genera un código único para una URL y establece una fecha de expiración.

### 2. Redirección
- **GET** `/api/v1/{shortCode}`
- **Descripción**: Redirige (HTTP 302) a la URL original asociada al código corto. Registra estadísticas del click (IP, User-Agent, Referer).

### 3. Analíticas
- **GET** `/api/v1/url/{shortCode}/analytics`
- **Descripción**: Devuelve información detallada sobre la URL corta, incluyendo contador total de clicks y detalles de los últimos accesos.

---

## Calidad y Pruebas

El proyecto cuenta con una cobertura del **100%** de las clases, asegurando la robustez de la lógica de negocio y la infraestructura.

### Estrategia de Testing
- **Pruebas de Dominio**: Validación exhaustiva de Value Objects (URLs válidas, IPv4/IPv6, ShortCodes) y lógica de entidades.
- **Pruebas de Casos de Uso**: Tests unitarios con Mockito para orquestar el flujo de negocio sin dependencias externas.
- **Pruebas de Infraestructura**:
    - **Controladores REST**: Uso de `@WebMvcTest` para verificar contratos de API, códigos de estado y serialización JSON.
    - **Persistencia**: Verificación de mappers y repositorios.
    - **Filtros**: Pruebas específicas para el `RateLimitFilter` simulando consumo de tokens y bloqueos por IP.
- **Manejo de Excepciones**: Cobertura del `GlobalExceptionHandler` para garantizar respuestas consistentes ante errores.

### Reporte de Cobertura
Se puede verificar la ejecución de las pruebas mediante:
```bash
./mvnw test
```

---

## Documentación del Código

Se ha implementado una estrategia de documentación en dos niveles:
1.  **Javadoc**: Documentación técnica detallada en todas las clases de dominio, casos de uso e infraestructura, facilitando el mantenimiento y la comprensión del código para desarrolladores.
2.  **OpenAPI/Swagger**: Documentación funcional y descriptiva de la interfaz REST, accesible en tiempo de ejecución.

---

## Estructura arquitectónica del proyecto
El proyecto sigue una **Arquitectura Hexagonal (Puertos y Adaptadores)**, lo que permite desacoplar la lógica de negocio de las tecnologías externas (bases de datos, APIs, frameworks). Adicionalmente, se aplica el patrón **CQRS (Command Query Responsibility Segregation)** en la capa de aplicación.

-   **`domain`**: Contiene las entidades de negocio (`ShortUrl`), objetos de valor (`OriginalUrl`, `ShortCode`) y las interfaces de los puertos (`ShortUrlRepository`). Es el núcleo de la aplicación y no tiene dependencias externas.
-   **`application`**: Orquesta la lógica del dominio separando las operaciones en:
    -   **Commands**: Operaciones que cambian el estado del sistema (`CreateShortUrlCommand`).
    -   **Queries**: Operaciones de lectura que no modifican el estado (`ResolveShortUrlQuery`, `GetAnalitycsQuery`).
    -   **Use Cases**: Implementaciones que procesan estos comandos y consultas.
-   **`infrastructure`**: Contiene las implementaciones de los puertos (adaptadores).
    -   `rest`: Controladores REST, modelos de request/response y manejo de excepciones.
    -   `persistence`: Implementación del repositorio usando Spring Data JPA y H2.
    -   `config`: Configuraciones de Spring Beans y propiedades.

---

## Decisiones de diseño tomadas
1.  **Arquitectura Hexagonal:** Para facilitar la mantenibilidad y testabilidad, aislando el dominio de cambios en la infraestructura.
2.  **CQRS:** Se decidió implementar CQRS para separar las responsabilidades de escritura y lectura. Esto facilita la escalabilidad independiente de las operaciones (por ejemplo, optimizar lecturas con caché sin afectar la lógica de creación) y mejora la claridad del código al definir intenciones claras mediante Comandos y Consultas.
3.  **Base de datos H2 (en memoria):** Elegida para simplificar la ejecución inicial y pruebas, evitando la necesidad de configurar una base de datos externa.
4.  **Caché con Caffeine (Estrategia de Lectura):**
    -   **Qué se cachea:** Se cachea el mapeo entre el `shortCode` y la `originalUrl`.
    -   **Por qué:** La resolución de URLs es la operación más frecuente en el sistema. Al cachear este resultado, evitamos accesos recurrentes a la base de datos, reducimos la latencia de redirección y mejoramos la capacidad de respuesta bajo carga.
5.  **Rate Limiting con Bucket4j:** Implementación de un filtro de seguridad para prevenir abusos y ataques de denegación de servicio (DoS) limitando solicitudes por IP.
6.  **MapStruct:** Para el mapeo entre capas (Entidades, DTOs, Modelos REST), manteniendo la separación de preocupaciones y reduciendo código boilerplate.
7.  **Validación de Dominio:** Se utilizan Value Objects para asegurar que las URLs y los códigos cortos cumplen con las reglas de negocio desde su creación.
8.  **Documentación con OpenAPI:** Integración de `springdoc-openapi` para generar documentación automática y pruebas interactivas de los endpoints.
9.  **Estandarización de Errores**: Uso de `ProblemDetail`  para respuestas de error consistentes y descriptivas.

---

## Trade-offs
-   **H2 vs Base de Datos Persistente:** Se priorizó la rapidez de ejecución local sobre la persistencia de datos a largo plazo. En un entorno productivo, se cambiaría H2 por una base de datos como PostgreSQL o MySQL mediante la configuración del puerto de persistencia.
-   **Sincronismo:** El proceso de creación y resolución es síncrono. Para una escala masiva, se podría considerar una resolución reactiva o el uso de Redis para una caché distribuida.
-   **Rate Limiting :** La solucion propuesta esta basada en las librerias de bucket4j, la cual es muy performante y tiene una gran cantidad de configuraciones. Aun asi, en un entorno real, lo ideal es que la solucion sea a nivel Gateway, para que se pueda aplicar en todas las aplicaciones.
-   **Analitica :** Se evitó una FK estricta para permitir desacoplar el almacenamiento de eventos de analytics del modelo transaccional, facilitando una futura migración a un sistema basado en eventos (Kafka / streaming).
-   **Testing MockitoBean vs MockBean**: Debido a la versión de Spring Boot, se optó por el uso de herramientas de testing estándar para asegurar compatibilidad en diferentes entornos.
-   **Manejo de Excepciones**: Se utiliza un `GlobalExceptionHandler` para mapear excepciones de dominio a códigos de estado HTTP apropiados (404, 409, 410, etc.), manteniendo el dominio limpio de conceptos HTTP.

---

## ESCALABILIDAD FUTURA
En escenarios de alta carga se proponen las siguientes mejoras:
- **Caché distribuida (Redis):** Reemplazar Caffeine por Redis para compartir la caché entre múltiples instancias del servicio.
- **Analytics asincrónico (event streaming):** Desacoplar el registro de clicks usando Kafka o RabbitMQ para que la redirección no se vea penalizada por la escritura en la base de datos de analíticas.
- **Rate limiting en gateway:** Mover la lógica de limitación de tasa a un API Gateway (como Spring Cloud Gateway o Nginx) para proteger la infraestructura antes de que las peticiones lleguen a la aplicación.

---

## Cómo y para qué utilicé la IA
La IA fue utilizada durante el desarrollo para:
-   **Generación de Estructura:** Definir la estructura de paquetes siguiendo los principios de Clean Architecture / Arquitectura Hexagonal.
-   **Automatización de Docker:** Creación del `Dockerfile` multietapa optimizado y el archivo `docker-compose.yml`.
-   **Documentación:** Redacción de este archivo `DESIGN.md`, generación de JavaDocs y anotaciones OpenAPI para mejorar la legibilidad y mantenibilidad.
-   **Refactorización:** Sugerencia de mejoras en el manejo de excepciones globales y validaciones.
-   **Pruebas Unitarias e Integración**: Generación de una suite de pruebas exhaustiva logrando el 100% de cobertura, incluyendo casos de borde y validaciones de infraestructura.
