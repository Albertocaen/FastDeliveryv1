<div align="center">

![Logo de FastDelivery](./src/main/resources/static/uploads/cambio-removebg-preview.png)

#  BacoDelivery

**Aplicación web completa de pedidos y reparto — construida, securizada y desplegada
en producción por un solo desarrollador.**

[![Demo en vivo](https://img.shields.io/badge/▶_Demo_en_vivo-bacodelivery.com-success?style=for-the-badge&logo=icloud&logoColor=white)](https://bacodelivery.com)
[![Portfolio](https://img.shields.io/badge/Portfolio-sobre_el_proyecto-grey?style=for-the-badge)](https://bacodelivery.com/sobre-mi)
[![Java](https://img.shields.io/badge/Java_22-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![Docker](https://img.shields.io/badge/Docker_Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Oracle Cloud](https://img.shields.io/badge/Oracle_Cloud-F80000?style=for-the-badge&logo=oracle&logoColor=white)](https://www.oracle.com/cloud/)

</div>

---

## ¿Por qué merece 5 minutos de tu atención?

Este no es un CRUD de tutorial. Es una aplicación real con tres roles de usuario,
pagos con PayPal, notificaciones en tiempo real, autenticación JWT con revocación
de tokens, y todo corriendo detrás de Cloudflare + Caddy en una VM de Oracle Cloud.

Aquí el resumen en 30 segundos:

| Qué | Cómo |
|-----|------|
| **Backend** | Spring Boot 3.2 · Spring Security 6 · JWT en cookie HttpOnly |
| **Persistencia** | JPA · MySQL 8 · 14 entidades · MapStruct para DTOs |
| **Tiempo real** | WebSocket · STOMP · SockJS (notificaciones al repartidor) |
| **Pagos** | Integración PayPal REST (sandbox → live configurable) |
| **Infraestructura** | Docker Compose · Oracle Cloud · Caddy · Cloudflare |
| **Seguridad** | TLS de extremo a extremo · Security List restringida a CF · JWT con blacklist |

> **Demo en vivo:** [bacodelivery.com](https://bacodelivery.com)

---

## Índice

- [Stack técnico](#stack-técnico)
- [Retos técnicos resueltos](#retos-técnicos-resueltos)
- [Funcionalidades principales](#funcionalidades-principales)
- [Arquitectura](#arquitectura-del-proyecto)
- [Seguridad](#seguridad--capas-aplicadas)
- [Instalación local](#instalación-local)
- [Tests](#tests)
- [Changelog](#changelog-reciente)
- [Contacto](#contacto)

---

## Stack técnico

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 22 |
| Framework | Spring Boot 3.2.4 |
| Seguridad | Spring Security 6 · JWT (JJWT 0.11.5, HMAC-SHA512) |
| Persistencia | Spring Data JPA · Hibernate · MySQL 8 |
| Mapeo DTO | MapStruct 1.4 |
| Pagos | PayPal REST SDK |
| Tiempo real | Spring WebSocket · STOMP · SockJS |
| Email | Spring Mail (SMTP) |
| Vistas | Thymeleaf 3 · Bootstrap 4/5 · jQuery 3.6 |
| Build | Maven · Dockerfile multi-stage |
| Contenedores | Docker · Compose v2 |
| Reverse proxy | Caddy 2 (TLS, WebSocket upgrade, security headers) |
| Cloud | Oracle Cloud Infrastructure |
| Edge / DNS | Cloudflare (proxy, Origin Cert, WAF, mitigación DDoS) |
| Endurecimiento | Security List restringida a CIDR de Cloudflare (ingress 443) |

---

## Retos técnicos resueltos

Problemas reales que surgieron durante el desarrollo y la puesta en producción:

**JWT con revocación explícita:** al hacer logout, el token se añade a una blacklist
en memoria (`TokenRevocationService`). Un token comprometido deja de ser válido al
instante, sin esperar a que caduque.

**Cookies host-only detrás de reverse proxy:** las cookies se emiten sin atributo
`Domain` y con `Secure` derivado de `request.isSecure()`, leyendo `X-Forwarded-Proto`
inyectado por Caddy gracias a `server.forward-headers-strategy=framework`. Así
funcionan en cualquier host (local, dominio, IP) y se marcan como seguras solo cuando
viajan por HTTPS.

**Asignación dinámica de repartidores:** algoritmo que asigna el pedido al primer
repartidor con menos de 3 pedidos activos, con fallback controlado si todos están
saturados.

**Compatibilidad Windows ↔ Linux en MySQL:** el flag `--lower-case-table-names=1`
en el servicio MySQL garantiza que las tablas se resuelvan igual en Linux que en
Windows. Sin esto, pasar el proyecto de dev a producción rompía silenciosamente
todas las queries.

**TLS con certificado persistente:** Caddy sirve HTTPS con un Origin Certificate de
15 años emitido por Cloudflare. WebSocket upgrade, compresión zstd/gzip y cabeceras
de seguridad (HSTS, X-Frame-Options, X-Content-Type-Options) aplicados por defecto.

**Build Docker multi-stage:** imagen en dos etapas con `eclipse-temurin:22` (JDK
para compilar, JRE para runtime). Imagen final ligera, sin herramientas de build.

**Seed de datos idempotente:** los scripts de `Docker/mysql-init/` se ejecutan solo
en la primera creación del volumen, dejando la base con 21 productos, 5 clientes,
4 repartidores y 3 admins de prueba listos para demo.

**Cloudflare como capa de borde:** IP del servidor oculta. WAF, mitigación DDoS y
caché de borde incluidos. `trusted_proxies` configurado en Caddy con los 22 rangos
CIDR de Cloudflare para que los logs y bloqueos por IP sean útiles.

**Security List de Oracle Cloud:** el puerto 443 solo acepta tráfico desde los
rangos de Cloudflare. Cualquier acceso directo se bloquea a nivel de red, antes
de llegar al reverse proxy. Defensa en profundidad real.

**Higiene del historial git:** auditoría con `git log --all -- ...` localizó
secretos trackeados en commits iniciales. Reescritura del historial con
`git filter-repo --invert-paths`, force push a todas las ramas y rotación de
secretos comprometidos.

---

## Funcionalidades principales

### Para el cliente
- Catálogo de productos con destacados en portada.
- Carrito de compra persistente en sesión.
- Checkout con PayPal (sandbox o live, configurable por entorno).
- Historial de pedidos con detalle completo: productos, precios unitarios, total y repartidor asignado.
- Notificaciones de estado en tiempo real vía WebSocket.
- Registro, login, edición de perfil y recuperación de contraseña por email.

### Para el repartidor
- Vista dedicada con sus pedidos asignados.
- Notificaciones en tiempo real (STOMP sobre SockJS) cuando hay pedidos nuevos.
- Cambio de estado del pedido (en reparto → entregado) que libera su cola.
- Vehículo asignado (moto o coche) con atributos específicos.

### Para el admin
- Panel de gestión (`/gestor`) con CRUD completo de productos, proveedores, clientes,
  repartidores, vehículos y stock, todo cargado dinámicamente sin recarga de página.
- Creación de pedidos a proveedores para reponer inventario.
- Visión global del sistema en una única vista.

### Transversales
- Chatbot integrado con respuestas a FAQ.
- Página pública `/sobre-mi` orientada a portfolio, con stack técnico y retos resueltos.
- Error pages propias (403, 404, genérica) coherentes con la estética.

---

## Arquitectura del proyecto

```
                        Internet
                            │
                  ┌─────────▼─────────┐
                  │    Cloudflare     │  ← TLS público + WAF + DDoS
                  └─────────┬─────────┘
                            │ Origin TLS (cert 15 años)
                  ┌─────────▼─────────┐
                  │  Oracle Cloud VM  │  ← Security List: solo CIDRs CF
                  │  ┌─────────────┐  │
                  │  │   Caddy 2   │  │  ← reverse proxy, HSTS, headers
                  │  └──────┬──────┘  │
                  │         │ red docker interna
                  │  ┌──────▼──────┐  │
                  │  │ app (Spring)│  │  ← :8080 (no expuesto al host)
                  │  └──────┬──────┘  │
                  │  ┌──────▼──────┐  │
                  │  │  MySQL 8    │  │  ← :3306 (no expuesto al host)
                  │  └─────────────┘  │
                  └───────────────────┘
```

```
src/main/java/org/proyecto/fastdeliveryp_v1/
├── config/           Beans de configuración (WebConfig, WebSocketConfig, PaypalConfig)
├── controller/       16 controladores Thymeleaf MVC + panel admin
├── restcontroller/   12 controladores REST para APIs internas
├── dto/              DTOs para exponer entidades sin leaks de datos
├── entity/           14 entidades JPA (Persona, Cliente, Repartidor, Vehiculo, Producto...)
├── exceptionHandler/ GlobalExceptionHandler + CustomErrorController
├── mapper/           17 mappers MapStruct (entity <-> DTO)
├── repository/       Repositorios Spring Data JPA
├── security/         JwtTokenUtil, JwtTokenFilter, PasswordEncryptor
└── service/          17 servicios con la lógica de negocio
```

El modelo de datos trabaja con tres jerarquías: **Persona** como raíz de usuario
(Admin / Cliente / Repartidor), **Vehiculo** como padre de Moto y Coche, y los pedidos
desdoblados en **PedidoCliente** (cara al comprador) y **PedidoProveedor** (reposición
de stock), cada uno con su tabla de línea de productos.

---

## Seguridad — capas aplicadas

**Borde (Cloudflare):** WAF, DDoS, TLS público, IP del servidor oculta.

**Reverse proxy (Caddy):** TLS con Origin Certificate de Cloudflare (15 años),
`trusted_proxies` con los 22 rangos CIDR de Cloudflare, cabeceras de seguridad
(HSTS, X-Content-Type-Options, X-Frame-Options, Referrer-Policy), WebSocket upgrade,
límite de tamaño de request (20 MB).

**Red (Oracle Cloud):** Security List con reglas ingress que solo permiten tráfico
al puerto 443 desde los rangos de Cloudflare. Puertos internos (8080, 3306) no
expuestos al host; viven en la red Docker privada `fastdelivery-net`.

**Aplicación:** JWT con revocación activa (`TokenRevocationService`), cookies
host-only con `Secure` derivado dinámicamente de `request.isSecure()`, Spring
Security 6 con control de acceso por rol (`@PreAuthorize`).

**Repositorio:** `.gitignore` cubre credenciales y certificados (`.env`,
`certs/`, `*.pem`, `*.key`). Historial auditado y limpio.

---

## Instalación local

**Requisitos:** Docker y Docker Compose v2 (`docker compose`, no `docker-compose`).

```bash
git clone https://github.com/Albertocaen/FastDeliveryv1.git
cd FastDeliveryv1

# Variables de entorno (copia la plantilla y edita)
cp .env.example .env
# → Edita .env: MySQL, JWT_SECRET, SMTP, PayPal, API_BASE_URL=http://localhost:8080

# Levantar app + base de datos (Caddy se excluye en local via override)
docker compose up -d --build app usuario-mysql
docker compose logs -f app
```

Una vez arranque: `http://localhost:8080`

El seed SQL inicializa automáticamente productos, clientes y repartidores de prueba.

---

## Tests

Cobertura con JUnit 5 y Mockito en los puntos críticos:

- `FastDeliveryPV1ApplicationTests` — carga del contexto de Spring.
- `JwtTokenUtilTest` — creación, validación y expiración de tokens.
- `CarritoControllerTest` — flujos del carrito.
- `ProductoServiceTest` — lógica de negocio de productos.

---

## Changelog reciente

> Las entradas más nuevas están arriba.

### 2026-04 — Auditoría de seguridad y código

Pase de auditoría P0/P1 sobre la base ya estable. Principales cambios:

**Seguridad aplicada**
- `WebConfig`: eliminado el resource handler `/templates/**` que exponía cualquier
  fragmento HTML al público (reconnaissance gratis para un atacante; ahora 404).
- `ClienteController`: añadido `@PreAuthorize("hasAuthority('ROLE_ADMIN')")` a nivel
  clase (broken access control previo: cualquier usuario podía ver/editar clientes ajenos).
- `ClienteController.deleteCliente`: cambiado de `@GetMapping` a `@PostMapping`.
  El delete por GET permitía CSRF GET-based.
- `SecurityConfig`: añadido `@EnableMethodSecurity` para que `@PreAuthorize` no se
  ignore silenciosamente.
- `JwtTokenUtil.init()`: validación de longitud mínima del secreto (≥ 64 chars).

**Código**
- `GlobalExceptionHandler`: corregido import roto (`java.nio.file.AccessDeniedException`
  → Spring Security) y redirect genérico corregido a `error/generico`.
- Sustituido `e.printStackTrace()` por SLF4J en filtros y handlers.
- `CookieInterceptor`: guarda contra `request.getCookies() == null` (NPE).
- `pom.xml`: eliminado `spring-boot-starter-webflux` (no se usaba, conflictaba con web).

**Próxima iteración**
- CSRF con `CookieCsrfTokenRepository`.
- Migración Flyway para el `PasswordEncryptor` (CommandLineRunner).
- Rate limiting con Bucket4j en `/login` y `/register`.
- Migrar PayPal SDK (`rest-api-sdk:1.14.0` deprecado desde 2020).

### 2026-04 — Refactor UX y sistema de diseño

- Tokens CSS globales en `styles.css` (paleta dorado/cobre, tipografía, espaciado de 4px).
- `productos/list.html`: CSS Grid responsive (1 → 4 columnas), tarjetas con imagen y precio.
- `carrito/ver.html`: layout 2 columnas (items + resumen sticky) en desktop.
- `pedidos/list.html`: cards con pill de estado coloreado y panel lateral de detalle
  (productos, precios unitarios, total, repartidor asignado) cargado vía AJAX.
- Página `/sobre-mi` rediseñada con Open Graph, Twitter Card y CTA para reclutadores.
- Error pages propias (403, 404, genérica) con branding coherente.

### 2026-03 — Endurecimiento de infraestructura

- Proxy Cloudflare Full strict, Origin Certificate de 15 años.
- `Caddyfile` con `trusted_proxies` declarando los 22 CIDR de Cloudflare.
- Security List de Oracle Cloud: ingress 443 restringido a rangos IP de Cloudflare.
- Auditoría del historial git con `git filter-repo`, eliminando secretos trackeados
  en versiones iniciales. Force push y rotación de secretos comprometidos.

### 2026-01 — Primer despliegue productivo

- Cookies JWT host-only con `Secure` dinámico via `request.isSecure()`.
- `docker-compose.yml`: solo Caddy expone puertos al host. Healthchecks y restart.
- `--lower-case-table-names=1` en MySQL para igualar Linux ↔ Windows.
- Seed SQL idempotente: 21 productos, 5 clientes, 4 repartidores, 3 admins.

---

## Contacto

- **Autor:** Alberto Caen
- **GitHub:** [github.com/Albertocaen](https://github.com/Albertocaen)
- **LinkedIn:** [linkedin.com/in/albertocaen77](https://www.linkedin.com/in/albertocaen77)
- **Email:** alberto.caen.1@gmail.com
- **Demo:** [bacodelivery.com](https://bacodelivery.com) · [Sobre el proyecto](https://bacodelivery.com/sobre-mi)

---

## Licencia

MIT — ver [`LICENSE`](./LICENSE).
