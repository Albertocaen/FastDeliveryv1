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
- **Guía de despliegue (OCI):** [`DEPLOYMENT_OCI.md`](./DEPLOYMENT_OCI.md)
- **Autor:** [Alberto Caen](https://github.com/Albertocaen) — [LinkedIn](https://www.linkedin.com/in/albertocaen77)

---

## Qué es este proyecto

**FastDelivery · Baco** es una aplicación web integral que cubre todo el ciclo de un
negocio de reparto: catálogo de productos, carrito de compra, pagos con PayPal, gestión
de pedidos, asignación dinámica de repartidores a vehículos, control de stock,
notificaciones en tiempo real y un panel de administración.

No es una demo de Spring Boot: es un sistema real, con tres tipos de usuario (cliente,
repartidor, admin), base de datos con catorce tablas, autenticación JWT con revocación
de tokens, reverse proxy con TLS automático y un despliegue productivo en una VM ARM64
de Oracle Cloud.

## Qué he resuelto (y por qué está bien)

Esto no es un CRUD. Son los puntos del proyecto que diferencian "funciona en mi portátil"
de "corre en producción":

- **JWT con revocación explícita:** al hacer logout el token se añade a una blacklist en
  memoria (`TokenRevocationService`), de forma que un token robado deja de ser válido
  aunque no haya caducado. Muy poco común en implementaciones JWT básicas.
- **Cookies host-only detrás de reverse proxy:** tras depurar un bug de login en producción
  me di cuenta de que un `setDomain("localhost")` hardcodeado hacía que el navegador
  descartase la cookie. La solución fue emitir cookies sin `Domain` y derivar `Secure`
  de `request.isSecure()`, que respeta los `X-Forwarded-Proto` que inyecta Caddy gracias a
  `server.forward-headers-strategy=framework`.
- **Asignación dinámica de repartidores:** algoritmo que busca el primer repartidor con
  menos de 3 pedidos activos, descontando los ya entregados. Constraint en BBDD + lógica
  en servicio, con fallback si todos están saturados.
- **Diferencias Windows → Linux en MySQL:** durante el primer despliegue, los inserts de
  datos iniciales fallaban por `Table 'FastDelivery.producto' doesn't exist`. El problema
  era que `CREATE TABLE PRODUCTO` e `INSERT INTO producto` apuntan a tablas distintas en
  Linux si no fuerzas `--lower-case-table-names=1`. Ese flag ahora está en el Compose.
- **Encoding JDBC:** `characterEncoding=UTF-8` (nombre Java), no `utf8mb4` (nombre MySQL).
  Spring rechaza el segundo al abrir la conexión — otro bug resuelto en producción.
- **TLS automático sin Let's Encrypt a mano:** Caddy gestiona los certificados contra
  Let's Encrypt, con soporte para WebSocket upgrade, compresión zstd/gzip y cabeceras
  de seguridad (HSTS, X-Frame-Options, X-Content-Type-Options).
- **Build en ARM64 de bajos recursos:** Dockerfile multi-stage con `eclipse-temurin:22`
  (JDK para compilar, JRE para runtime), límites de memoria por servicio en Compose y
  swap en la VM para sobrevivir a la compilación de Maven con 6 GB de RAM.
- **Seed script idempotente:** los `Docker/mysql-init/*.sql` se ejecutan solo la primera
  vez que se crea el volumen, dejando el sistema listo con 21 productos, 5 clientes, 4
  repartidores y 3 admins de prueba sin intervención manual.
- **Cloudflare con Origin Certificate (15 años):** la app vive detrás de Cloudflare,
  que termina TLS público con su propio cert y reencripta hacia el origin con un Origin
  Cert emitido por Cloudflare y validado por Caddy. El visitante final nunca conoce la
  IP real de la VM y, de regalo, entran WAF, mitigación DDoS y caché de borde.
- **`trusted_proxies` con rangos CIDR de Cloudflare en Caddy:** sin esto `{client_ip}`
  devolvería la IP del nodo de Cloudflare en todos los logs (inútil para auditar o
  banear). Configurando los 22 rangos (IPv4 + IPv6) que publica Cloudflare como proxies
  de confianza, Caddy lee `CF-Connecting-IP` y reenvía la IP real del visitante en
  `X-Real-IP` y `X-Forwarded-For`.
- **OCI Security List cerrada a Cloudflare:** el puerto 443 de la VM solo acepta
  tráfico desde los 15 rangos de Cloudflare configurados como reglas ingress en la
  Security List de Oracle Cloud. Cualquier intento de hablar directamente con la IP
  pública de la VM se dropea a nivel de red, no llega ni a Caddy. Defensa en
  profundidad: Cloudflare en el borde + firewall de Oracle por debajo.
- **Limpieza de secretos en el historial git:** una auditoría con
  `git log --all -- src/main/resources/application.properties` reveló que el fichero
  estuvo trackeado durante 11 commits del primer arranque del proyecto, y un `.env` con
  credenciales de correo en otra rama. Reescribí todo el historial con `git filter-repo
  --invert-paths`, force push a todas las ramas, y `git fetch --prune` en cada clone
  (Windows + VM de producción). Los commits viejos en GitHub ya no exponen esos
  ficheros. Las credenciales filtradas pasan al protocolo estándar "asumir
  comprometidas, rotar y olvidar".

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
| Cloud | Oracle Cloud — VM Ampere A1 (ARM64, 6 GB RAM) |
| Edge / DNS | Cloudflare (proxy, Origin Cert, WAF, mitigación DDoS) |
| Endurecimiento | OCI Security List restringida a CIDR de Cloudflare (ingress 443) |

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

## Despliegue en producción

La guía completa de despliegue en Oracle Cloud (VM Ampere A1 ARM64, Caddy con HTTPS
automático, DNS en Cloudflare, swap para la build, backups) está en
[`DEPLOYMENT_OCI.md`](./DEPLOYMENT_OCI.md) para no ensuciar este README.

El resumen es: `docker compose up -d --build` en la VM, Caddy presenta el Origin
Certificate de Cloudflare, y MySQL persiste en un volumen Docker. La VM solo expone
80 y 443, y la Security List de Oracle Cloud los restringe a los rangos IP de
Cloudflare. La app y MySQL viven en la red interna `fastdelivery-net`, sin contacto
con el host.

## Endurecimiento de seguridad

Capa por capa, qué protege a la app:

**Borde (Cloudflare)**
- Proxy activo: el visitante final habla con Cloudflare, no con la VM. La IP real
  del origin queda oculta.
- Cert público gestionado por Cloudflare: certificado renovado automáticamente, sin
  Let's Encrypt manual ni cron.
- WAF y mitigación DDoS incluidos en el plan gratuito: bots básicos, escaneos y
  amplification attacks se filtran antes de llegar al origin.

**Origen (Caddy + Spring Boot)**
- TLS interno con Origin Certificate de Cloudflare (15 años): sin renovaciones que
  vigilar.
- `trusted_proxies static` con los 22 rangos CIDR de Cloudflare: `{client_ip}` y los
  headers `X-Real-IP` / `X-Forwarded-For` reflejan la IP real del visitante.
- Cabeceras de seguridad fijas: HSTS (1 año, includeSubDomains), X-Content-Type-Options,
  X-Frame-Options, Referrer-Policy. La cabecera `Server` se elimina.
- WebSocket upgrade explícito para mantener STOMP/SockJS funcionando bajo proxy.
- `request_body max_size 20MB` para limitar uploads abusivos.

**Red (Oracle Cloud)**
- Security List ingress de la VCN solo deja entrar 80 y 443 desde los 15 rangos de
  Cloudflare (uno por regla, IPv4). Todo lo demás se dropea en la capa de red — no
  llega ni a Caddy.
- Puertos internos (8080 de la app, 3306 de MySQL) **no se exponen al host**, solo
  son accesibles dentro de la red Docker `fastdelivery-net`.

**Aplicación**
- JWT con blacklist en memoria (`TokenRevocationService`): el logout invalida el token
  inmediatamente, aunque no haya caducado.
- Cookies host-only sin atributo `Domain`: funcionan en cualquier host (localhost,
  bacodelivery.com, IP) sin necesidad de configuración por entorno.
- Cookies `Secure` derivadas de `request.isSecure()`, leyendo `X-Forwarded-Proto` que
  inyecta Caddy gracias a `server.forward-headers-strategy=framework`.
- Spring Security 6 con configuración estricta de rutas por rol y CSRF en formularios.

**Repositorio**
- `.gitignore` cubre `.env`, `application.properties`, `certs/`, `*.pem`, `*.key`.
- Historial git limpio: `application.properties` y `.env` eliminados de los 11+5
  commits viejos donde estuvieron trackeados, mediante `git filter-repo`.

## Changelog — primer despliegue productivo

Los fixes más relevantes aplicados durante el primer deploy a `bacodelivery.com`:

**Autenticación**
- Cookies JWT host-only (sin `setDomain`), para que funcionen en cualquier host.
- `Secure` derivado de `request.isSecure()`, respetando `X-Forwarded-Proto` de Caddy.
- Enlaces de reset password parametrizados por `app.base.url` (antes apuntaban a `localhost:8080`).

**Despliegue y base de datos**
- `docker-compose.yml`: MySQL y `app` dejan de exponer puertos al host; solo Caddy.
- `docker-compose.yml`: límites de memoria pensados para VM Ampere A1 (6 GB).
- `docker-compose.yml`: `--lower-case-table-names=1` en MySQL para igualar Linux ↔ Windows.
- `docker-compose.yml`: JDBC con `characterEncoding=UTF-8` (Java) en lugar de `utf8mb4` (MySQL).
- `docker-compose.yml`: healthchecks y `restart: unless-stopped` por servicio.
- `Caddyfile`: WebSocket upgrade, HSTS, X-Frame-Options, redirect `www` → apex.

**Init SQL**
- Unificado el nombre de la base (`Fastdelivery` → `FastDelivery`) para que el `USE`
  coincida con `MYSQL_DATABASE`. Los scripts de `Docker/mysql-init/` ahora son
  idempotentes en la primera creación del volumen.

**Frontend**
- Rehechas `register.html`, `profile/view.html`, `profile/edit.html` y `perfil.css` con
  estética coherente (cards, avatar, paleta `#333` / `#f8f9fa`, Roboto).
- Página nueva `/sobre-mi` con hero, funcionalidades, stack, retos y proyectos.

## Changelog — endurecimiento post-deploy

Mejoras de seguridad y operación aplicadas después del primer arranque estable:

**Cloudflare y red**
- Activado el proxy de Cloudflare (modo Full strict) con Origin Certificate de 15 años.
- `Caddyfile`: `tls /etc/caddy/certs/origin.pem /etc/caddy/certs/origin.key` y bloque
  global `servers { trusted_proxies static ... }` con los 22 rangos CIDR de Cloudflare.
- `docker-compose.yml`: nuevo mount `./certs:/etc/caddy/certs:ro` para que Caddy lea
  el cert sin que viaje en la imagen.
- OCI Security List: añadidas 15 reglas ingress para puerto 443 limitadas a los rangos
  IPv4 de Cloudflare. Acceso directo a la IP de la VM bloqueado.

**Repositorio y secretos**
- Auditoría con `git log --all -- ...` localizó secretos antiguos en el historial.
- `git filter-repo --path ... --invert-paths` reescribió el historial completo (master
  y todas las ramas vivas) eliminando `application.properties` y `.env` de cada commit.
- Force push a todas las ramas + `git fetch --prune` en cada clone.
- `.gitignore` actualizado con `certs/`, `*.pem`, `*.key`.
- Limpieza de ramas obsoletas (`local-working`, `test`) que ya no aportaban valor.

## Tests

Cobertura básica con JUnit 5 y Mockito en los puntos críticos:
- `FastDeliveryPV1ApplicationTests` — carga del contexto de Spring.
- `JwtTokenUtilTest` — creación, validación y expiración de tokens.
- `CarritoControllerTest` — flujos del carrito.
- `ProductoServiceTest` — lógica de negocio de productos.

## Licencia

MIT. Ver [`LICENSE`](./LICENSE).

## Contacto

- **Autor:** Alberto Caen
- **GitHub:** [github.com/Albertocaen](https://github.com/Albertocaen)
- **LinkedIn:** [linkedin.com/in/albertocaen77](https://www.linkedin.com/in/albertocaen77)
- **Email:** alberto.caen.1@gmail.com
- **Proyecto en vivo:** [bacodelivery.com](https://bacodelivery.com) · [Sobre el proyecto](https://bacodelivery.com/sobre-mi)
