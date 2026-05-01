# Diseño del Proyecto - URL Shortener

Este documento detalla las especificaciones técnicas, decisiones de diseño y arquitectura del proyecto URL Shortener.

## Formas de levantar el proyecto

### 1. Compilando localmente
Para ejecutar el proyecto de forma local, asegúrate de tener instalado Java 21 y Maven.

1.  **Compilar el proyecto:**
    ```bash
    ./mvnw clean package
    ```
2.  **Ejecutar la aplicación:**
    ```bash
    java -jar target/url-shortener-1.0.0.jar
    ```
    La aplicación estará disponible en: `http://localhost:8080/api/v1/url-shortener`

### 2. Usando Docker
Asegúrate de tener instalado Docker y Docker Compose.

1.  **Levantar con Docker Compose:**
    ```bash
    docker compose up --build
    ```
    Esto compilará la imagen y levantará el contenedor en el puerto `8080`.

---

## Estructura arquitectónica del proyecto
El proyecto sigue una **Arquitectura Hexagonal (Puertos y Adaptadores)**, lo que permite desacoplar la lógica de negocio de las tecnologías externas (bases de datos, APIs, frameworks).

-   **`domain`**: Contiene las entidades de negocio (`ShortUrl`), objetos de valor (`OriginalUrl`, `ShortCode`) y las interfaces de los puertos (`ShortUrlRepository`). Es el núcleo de la aplicación y no tiene dependencias externas.
-   **`application`**: Contiene los casos de uso (`CreateShortUrlUseCase`, `ResolveShortUrlUseCase`) y los DTOs/Comandos/Queries. Orquesta la lógica del dominio.
-   **`infrastructure`**: Contiene las implementaciones de los puertos (adaptadores).
    -   `rest`: Controladores REST y manejo de excepciones.
    -   `persistence`: Implementación del repositorio usando Spring Data JPA y H2.
    -   `config`: Configuraciones de Spring Beans y propiedades.

---

## Decisiones de diseño tomadas
1.  **Arquitectura Hexagonal:** Para facilitar la mantenibilidad y testabilidad, aislando el dominio de cambios en la infraestructura.
2.  **Base de datos H2 (en memoria):** Elegida para simplificar la ejecución inicial y pruebas, evitando la necesidad de configurar una base de datos externa.
3.  **Caché con Caffeine:** Se implementó una capa de caché para las resoluciones de URLs cortas, mejorando el rendimiento en accesos repetitivos a la misma URL.
4.  **MapStruct:** Para el mapeo entre capas (Entidades, DTOs, Modelos REST), manteniendo la separación de preocupaciones y reduciendo código boilerplate.
5.  **Validación de Dominio:** Se utilizan Value Objects para asegurar que las URLs y los códigos cortos cumplen con las reglas de negocio desde su creación.

---

## Trade-offs
-   **H2 vs Base de Datos Persistente:** Se priorizó la rapidez de ejecución local sobre la persistencia de datos a largo plazo. En un entorno productivo, se cambiaría H2 por una base de datos como PostgreSQL o MySQL mediante la configuración del puerto de persistencia.
-   **Sincronismo:** El proceso de creación y resolución es síncrono. Para una escala masiva, se podría considerar una resolución reactiva o el uso de Redis para una caché distribuida.
-   **Rate Limiting :** La solucion propuesta esta basada en las librerias de bucket4j, la cual es muy performante y tiene una gran cantidad de configuraciones. Aun asi, en un entorno real, lo ideal es que la solucion sea a nivel Gateway, para que se pueda aplicar en todas las aplicaciones.
-   **Analitica :** Para el metodo de analitica, decidi no hacer una FK entre la tabla de analitica y la de url, ya que, con esta solucion, la informacion de url se guarda en memoria tambien, y podria generar un problema de escalabilidad.

---

## Cómo y para qué utilicé la IA
La IA (Junie) fue utilizada durante el desarrollo para:
-   **Generación de Estructura:** Definir la estructura de paquetes siguiendo los principios de Clean Architecture / Arquitectura Hexagonal.
-   **Automatización de Docker:** Creación del `Dockerfile` multietapa optimizado y el archivo `docker-compose.yml`.
-   **Documentación:** Redacción de este archivo `DESIGN.md` y KDocs en el código para mejorar la legibilidad.
-   **Refactorización:** Sugerencia de mejoras en el manejo de excepciones globales y validaciones.
