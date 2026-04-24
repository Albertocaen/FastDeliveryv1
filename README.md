<div align="center">

![Logo de FastDelivery](./src/main/resources/static/uploads/cambio-removebg-preview.png)

# FastDelivery · Baco

**Plataforma completa de pedidos y reparto a domicilio construida con Spring Boot,
desplegada en producción sobre Oracle Cloud con HTTPS automático y dominio propio.**

[![Demo en vivo](https://img.shields.io/badge/demo-bacodelivery.com-success?style=for-the-badge&logo=icloud&logoColor=white)](https://bacodelivery.com)
[![Sobre el proyecto](https://img.shields.io/badge/sobre_el_proyecto-grey?style=for-the-badge)](https://bacodelivery.com/sobre-mi)
[![Java](https://img.shields.io/badge/Java-22-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose_v2-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

</div>

---

## Enlaces rápidos

- **Demo en vivo:** [bacodelivery.com](https://bacodelivery.com)
- **Página del proyecto:** [bacodelivery.com/sobre-mi](https://bacodelivery.com/sobre-mi)
- **Autor:** [Alberto Caen](https://github.com/Albertocaen) — [LinkedIn](https://www.linkedin.com/in/albertocaen77)

---

## Qué es este proyecto

**FastDelivery · Baco** es una aplicación web completa que cubre el ciclo de un negocio
de reparto: catálogo de productos, carrito de compra, pagos con PayPal, gestión de
pedidos, asignación dinámica de repartidores a vehículos, control de stock,
notificaciones en tiempo real y panel de administración.

Incluye tres tipos de usuario (cliente, repartidor, admin), base de datos con catorce
tablas, autenticación JWT con revocación de tokens, reverse proxy con TLS y despliegue
productivo en Oracle Cloud.

## Stack técnico

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 22 |
| Framework | Spring Boot 3.2.4 |
| Seguridad | Spring Security 6 · JWT (JJWT 0.11.5, HMAC-SHA512) |
| Persistencia | Spring Data JPA · Hibernate · MySQL 8 |
| Mapeo DTO | MapStruct 1.4 |
| Pagos | PayPal REST SDK 1.14 |
| Tiempo real | Spring WebSocket · STOMP · SockJS |
| Email | Spring Mail (SMTP) |
| Vistas | Thymeleaf 3 · Bootstrap 4/5 · jQuery 3.6 |
| Build | Maven · Dockerfile multi-stage |
| Contenedores | Docker · Compose v2 |
| Reverse proxy | Caddy 2 (TLS, WebSocket upgrade, security headers) |
| Cloud | Oracle Cloud Infrastructure |
| Edge / DNS | Cloudflare (proxy, Origin Cert, WAF, mitigación DDoS) |
| Endurecimiento | Security List restringida a CIDR de Cloudflare (ingress 443) |

## Retos técnicos resueltos

Problemas reales afrontados durante el desarrollo y la puesta en producción:

- **JWT con revocación explícita:** al hacer logout, el token se añade a una blacklist
  en memoria (`TokenRevocationService`). Un token comprometido deja de ser válido al
  instante, sin esperar a que caduque.
- **Cookies host-only detrás de reverse proxy:** las cookies se emiten sin atributo
  `Domain` y con `Secure` derivado de `request.isSecure()`, leyendo `X-Forwarded-Proto`
  inyectado por Caddy gracias a `server.forward-headers-strategy=framework`. Así
  funcionan en cualquier host (local, dominio, IP) y se marcan como seguras solo cuando
  viajan por HTTPS.
- **Asignación dinámica de repartidores:** algoritmo que asigna el pedido al primer
  repartidor con menos de 3 pedidos activos, descontando los entregados. Constraint en
  base de datos + lógica en servicio, con fallback controlado si todos están saturados.
- **Compatibilidad Windows ↔ Linux en MySQL:** el flag `--lower-case-table-names=1` en
  el servicio MySQL garantiza que las tablas se resuelvan igual en Linux que en Windows,
  evitando fallos silenciosos en los scripts de inicialización al desplegar.
- **Encoding JDBC:** la URL de conexión usa `characterEncoding=UTF-8` (nombre Java),
  no `utf8mb4` (nombre MySQL). Sutil pero importante — Spring rechaza el segundo al
  abrir la conexión.
- **TLS con certificado persistente:** Caddy sirve HTTPS con un Origin Certificate de
  15 años emitido por Cloudflare. WebSocket upgrade, compresión zstd/gzip y cabeceras
  de seguridad (HSTS, X-Frame-Options, X-Content-Type-Options) aplicados por defecto.
- **Build Docker multi-stage:** imagen construida en dos etapas con `eclipse-temurin:22`
  (JDK para compilar, JRE para runtime). Imagen final ligera y sin herramientas de build.
  Límites de memoria por servicio en Compose para que el consumo sea predecible.
- **Seed de datos idempotente:** los scripts de `Docker/mysql-init/` se ejecutan solo
  en la primera creación del volumen, dejando la base con 21 productos, 5 clientes, 4
  repartidores y 3 admins de prueba listos para demo.
- **Cloudflare como capa de borde:** Cloudflare actúa como proxy delante de la
  aplicación: termina TLS público con su propio certificado y reencripta hacia el
  origen con un Origin Certificate de 15 años validado por Caddy. La IP real del
  servidor queda oculta, y entran WAF, mitigación DDoS y caché de borde.
- **`trusted_proxies` configurado en Caddy:** los 22 rangos CIDR (IPv4 + IPv6) de
  Cloudflare se declaran como proxies de confianza, para que `{client_ip}` y las
  cabeceras `X-Real-IP` / `X-Forwarded-For` reflejen la IP real del visitante en lugar
  de la IP del nodo de Cloudflare. Fundamental para logs útiles y bloqueos por IP.
- **Security List de Oracle Cloud cerrada a Cloudflare:** el puerto 443 del servidor
  solo acepta tráfico desde los 15 rangos de Cloudflare, configurados como reglas
  ingress a nivel de red. Cualquier intento de acceso directo se bloquea antes incluso
  de llegar al reverse proxy. Defensa en profundidad: Cloudflare en el borde + firewall
  cloud por debajo.
- **Higiene del historial git:** una auditoría con `git log --all -- ...` localizó que
  `application.properties` y un `.env` antiguo habían estado trackeados en commits del
  primer arranque del proyecto. Se reescribió el historial completo con
  `git filter-repo --invert-paths`, seguido de force push a todas las ramas y
  sincronización de clones. Los secretos filtrados se rotaron por protocolo, el
  repositorio quedó limpio y el `.gitignore` se amplió para cubrir `certs/`, `*.pem`
  y `*.key`.

## Seguridad — capas aplicadas

La aplicación está protegida por varias capas independientes que trabajan juntas:

**Borde (Cloudflare)**
- Proxy activo delante de la aplicación. El visitante habla con Cloudflare, la IP
  real del servidor queda oculta.
- Certificado público gestionado por Cloudflare: renovación automática, sin
  intervención manual.
- WAF y mitigación DDoS incluidos: bots conocidos, escaneos y ataques de amplificación
  se filtran antes de alcanzar la aplicación.

**Reverse proxy (Caddy)**
- TLS con Origin Certificate de Cloudflare válido 15 años.
- `trusted_proxies` configurado con los 22 rangos CIDR de Cloudflare para propagar
  correctamente la IP real del visitante.
- Cabeceras de seguridad: HSTS (1 año, `includeSubDomains`), `X-Content-Type-Options`,
  `X-Frame-Options`, `Referrer-Policy`. La cabecera `Server` se elimina.
- Soporte explícito para WebSocket upgrade (STOMP / SockJS).
- Límite de tamaño de request (`20 MB`) para contener uploads abusivos.

**Red (Oracle Cloud)**
- Security List con reglas ingress que solo permiten tráfico al puerto 443 desde los
  15 rangos IP de Cloudflare. El resto se bloquea antes de entrar al servidor.
- Puertos internos (`8080` de la aplicación, `3306` de MySQL) no se exponen al host;
  viven dentro de la red Docker privada `fastdelivery-net`.

**Aplicación**
- JWT con revocación activa a través de `TokenRevocationService`: el logout invalida
  el token de inmediato.
- Cookies host-only y `Secure` derivado dinámicamente de `request.isSecure()`, para
  que funcionen correctamente tanto en local como detrás del reverse proxy.
- Spring Security 6 con control de acceso por rol y protección CSRF en formularios.

**Repositorio**
- `.gitignore` cubre credenciales y certificados (`.env`, `application.properties`,
  `certs/`, `*.pem`, `*.key`).
- Historial de git auditado y limpio: ficheros sensibles que estuvieron trackeados
  en versiones iniciales fueron eliminados del historial con `git filter-repo`.

## Funcionalidades principales

### Para el cliente
- Catálogo de productos con destacados en portada.
- Carrito de compra persistente en sesión.
- Checkout con PayPal (sandbox o live, configurable por entorno).
- Historial de pedidos y estado en tiempo real vía WebSocket.
- Registro, login, edición de perfil y recuperación de contraseña por email.

### Para el repartidor
- Vista dedicada con sus pedidos asignados.
- Notificaciones en tiempo real (STOMP sobre SockJS) cuando hay pedidos nuevos.
- Cambio de estado del pedido (en reparto → entregado) que libera su cola.
- Vehículo asignado (moto o coche) con atributos específicos.

### Para el admin
- Panel de gestión con CRUD de productos, proveedores, clientes, repartidores,
  vehículos y stock.
- Creación de pedidos a proveedores para reponer inventario.
- Visión global del sistema en una única vista (`/gestor`).

### Transversales
- Chatbot integrado con respuestas a FAQ (envíos, horarios, contacto, devoluciones).
- Página pública `/sobre-mi` orientada a portfolio, con stack técnico y retos resueltos.
- Error pages propias (403, 404, genérica) coherentes con la estética.

## Arquitectura del proyecto

```
src/main/java/org/proyecto/fastdeliveryp_v1/
├── classes/          Validadores custom (ImageUrlValidator, NumericValidator)
├── config/           Beans de configuracion (WebConfig, WebSocketConfig, PaypalConfig, CookieInterceptor)
├── controller/       16 controladores Thymeleaf MVC
├── restcontroller/   12 controladores REST para el panel de gestion y APIs internas
├── dto/              DTOs para exponer entidades sin leaks
├── entity/           14 entidades JPA (Persona, Cliente, Repartidor, Vehiculo, Producto...)
├── exceptionHandler/ GlobalExceptionHandler + CustomErrorController
├── mapper/           17 mappers MapStruct (entity <-> DTO)
├── repository/       Repositorios Spring Data JPA
├── security/         JwtTokenUtil, JwtTokenFilter, PasswordEncryptor
└── service/          17 servicios con la logica de negocio
```

La base de datos modela tres jerarquías naturales: **Persona** como raíz de usuario
(Admin / Cliente / Repartidor), **Vehiculo** como padre de Moto y Coche, y los pedidos
desdoblados en **PedidoCliente** (cara al comprador) y **PedidoProveedor** (reposición
de stock), cada uno con su tabla de línea de productos.

## Despliegue en producción

La aplicación se despliega en Oracle Cloud con `docker compose up -d --build`. Caddy
presenta el Origin Certificate de Cloudflare y MySQL persiste en un volumen Docker.
El servidor solo expone los puertos 80 y 443, y la Security List de Oracle Cloud los
restringe a los rangos IP de Cloudflare. La aplicación y MySQL viven en la red interna
`fastdelivery-net`, sin contacto con el host.

## Instalación local

Requisitos:
- Java 22 (el `Dockerfile` usa `eclipse-temurin:22`)
- Docker y Docker Compose v2 (`docker compose`, no `docker-compose`)
- Maven (opcional; el proyecto incluye `./mvnw`)

Arranque:

```bash
git clone https://github.com/Albertocaen/FastDeliveryv1.git
cd FastDeliveryv1

# Variables de entorno a partir de la plantilla
cp .env.production.example .env
# edita .env con tus credenciales de MySQL, JWT, SMTP, PayPal...

# Levantar todo el stack
docker compose up -d --build
docker compose logs -f app
```

Una vez arranque, la app está en `http://localhost:8080`.

## Tests

Cobertura con JUnit 5 y Mockito en los puntos críticos:
- `FastDeliveryPV1ApplicationTests` — carga del contexto de Spring.
- `JwtTokenUtilTest` — creación, validación y expiración de tokens.
- `CarritoControllerTest` — flujos del carrito.
- `ProductoServiceTest` — lógica de negocio de productos.

## Historial de cambios

### Primer despliegue productivo

Fixes relevantes aplicados durante el primer deploy a `bacodelivery.com`:

**Autenticación**
- Cookies JWT host-only (sin `setDomain`), para que funcionen en cualquier host.
- `Secure` derivado de `request.isSecure()`, respetando `X-Forwarded-Proto` de Caddy.
- Enlaces de reset password parametrizados por `app.base.url` (antes apuntaban a `localhost:8080`).

**Despliegue y base de datos**
- `docker-compose.yml`: MySQL y `app` dejan de exponer puertos al host, solo Caddy.
- `docker-compose.yml`: límites de memoria por servicio para controlar el consumo.
- `docker-compose.yml`: `--lower-case-table-names=1` en MySQL para igualar Linux ↔ Windows.
- `docker-compose.yml`: JDBC con `characterEncoding=UTF-8` (Java) en lugar de `utf8mb4` (MySQL).
- `docker-compose.yml`: healthchecks y `restart: unless-stopped` por servicio.
- `Caddyfile`: WebSocket upgrade, HSTS, `X-Frame-Options`, redirect `www` → apex.

**Init SQL**
- Unificado el nombre de la base (`Fastdelivery` → `FastDelivery`) para que el `USE`
  coincida con `MYSQL_DATABASE`. Los scripts de `Docker/mysql-init/` ahora son
  idempotentes en la primera creación del volumen.

**Frontend**
- Rehechas `register.html`, `profile/view.html`, `profile/edit.html` y `perfil.css` con
  estética coherente (cards, avatar, paleta `#333` / `#f8f9fa`, Roboto).
- Página nueva `/sobre-mi` con hero, funcionalidades, stack, retos y proyectos.

### Endurecimiento de seguridad

Mejoras de seguridad y operación aplicadas sobre el despliegue ya estable:

**Cloudflare y red**
- Proxy de Cloudflare activo en modo Full strict, con Origin Certificate de 15 años.
- `Caddyfile`: directiva `tls` apuntando al cert de Cloudflare y bloque global
  `servers { trusted_proxies static ... }` con los 22 rangos CIDR de Cloudflare.
- `docker-compose.yml`: volumen `./certs:/etc/caddy/certs:ro` para montar los certs
  sin incluirlos en la imagen.
- Security List de Oracle Cloud: 15 reglas ingress en el puerto 443 restringidas a
  los rangos IP de Cloudflare. Acceso directo por IP bloqueado.

**Repositorio y secretos**
- Auditoría del historial con `git log --all -- ...` para localizar secretos
  trackeados en versiones iniciales.
- Reescritura del historial completo con `git filter-repo --invert-paths`, eliminando
  `application.properties` y `.env` de cada commit en todas las ramas.
- Force push sincronizado + `git fetch --prune` en cada clone.
- `.gitignore` ampliado con `certs/`, `*.pem` y `*.key`.
- Limpieza de ramas obsoletas.

## Licencia

MIT. Ver [`LICENSE`](./LICENSE).

## Contacto

- **Autor:** Alberto Caen
- **GitHub:** [github.com/Albertocaen](https://github.com/Albertocaen)
- **LinkedIn:** [linkedin.com/in/albertocaen77](https://www.linkedin.com/in/albertocaen77)
- **Email:** alberto.caen.1@gmail.com
- **Proyecto en vivo:** [bacodelivery.com](https://bacodelivery.com) · [Sobre el proyecto](https://bacodelivery.com/sobre-mi)
