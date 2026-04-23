# Fast Delivery
![Logo de FastDelivery](./src/main/resources/static/uploads/cambio-removebg-preview.png)
## Baco 

Fast Delivery-Baco es una solución integral para la gestión y optimización de entregas de productos. Este proyecto tiene como objetivo proporcionar una plataforma eficiente y confiable para gestionar pedidos, rastrear envíos y facilitar la comunicación entre clientes y proveedores.

## Funcionalidades Principales

- **Gestión de Pedidos**: Creación, actualización y seguimiento de pedidos.
- **Carrito de Compras**: Gestión de items en el carrito de compras del cliente.
- **Autenticación y Autorización**: Manejo seguro de usuarios con roles y permisos.
- **Notificaciones en Tiempo Real**: Uso de WebSockets para notificaciones instantáneas.
- **Integración con PayPal**: Procesamiento de pagos a través de PayPal.
- **Restablecimiento de Contraseña**: Funcionalidad para recuperación de cuentas.
- **Configuración de Envío de Correos**: Configuración de notificaciones por correo electrónico.
- **Chatbot Integrado**: Asistencia automatizada a través de un chatbot.

## Requisitos

- Java 11 o superior
- Docker
- Maven

## Instalación

1. Clona el repositorio:
    ```bash
    git clone https://github.com/tu-usuario/fast-delivery.git
    cd fast-delivery
    ```

2. Construye la imagen Docker:
    ```bash
    docker build -t fast-delivery .
    ```

3. Ejecuta los contenedores Docker:
    ```bash
    docker-compose up -d
    ```

## Despliegue Remoto En Oracle Cloud

Esta guia esta pensada para desplegar la aplicacion en contenedores Docker sobre una VM de Oracle Cloud Infrastructure (OCI), usando solo una instancia pequeña, MySQL interno, Caddy como reverse proxy HTTPS y Cloudflare delante del dominio para reducir exposicion ante trafico abusivo.

### 1. Estrategia Recomendada

- Usar una VM Always Free de OCI, preferiblemente `VM.Standard.A1.Flex` con 1 OCPU y 6 GB RAM, o 2 OCPU y 12 GB RAM si hay capacidad.
- Publicar solo los puertos `80` y `443` hacia internet.
- No publicar MySQL. La base de datos queda dentro de la red interna de Docker como `usuario-mysql:3306`.
- Usar Caddy para HTTPS y proxy interno hacia `app:8080`.
- Usar Cloudflare como DNS proxied para ocultar parcialmente el origen y absorber trafico no deseado antes de llegar a Oracle.
- Crear Budget Alerts y, si el despliegue es critico en coste, dejar el servidor en un compartment separado con cuotas.

Importante: ningun proveedor puede prometer coste cero ante cualquier escenario. Lo que hacemos aqui es reducir superficie, usar recursos Always Free, crear alertas y tener un procedimiento de apagado rapido.

### 2. Crear Cuenta Y Limites De Coste En OCI

1. Crea o entra en tu cuenta de Oracle Cloud.
2. Trabaja en la Home Region de tu tenancy, porque los recursos Always Free deben crearse ahi.
3. Ve a `Billing & Cost Management > Budgets`.
4. Crea un budget mensual bajo, por ejemplo `1 EUR` o `1 USD`, sobre el compartment del proyecto o sobre root si no usas compartments.
5. Añade alertas al `50%`, `80%` y `100%`.
6. Opcional pero recomendado: crea un compartment llamado `fastdelivery-prod` y aplica quotas para limitar recursos nuevos.

### 3. Crear La VM

1. Ve a `Compute > Instances > Create instance`.
2. Nombre sugerido: `fastdelivery-prod`.
3. Imagen: Ubuntu 22.04/24.04 LTS o Oracle Linux 9.
4. Shape recomendado: `VM.Standard.A1.Flex`.
5. Asigna recursos moderados:

```text
OCPU: 1
Memory: 6 GB
Boot volume: 50 GB
```

6. Red: crea una VCN nueva o usa una existente.
7. Subnet: publica.
8. Public IPv4: activada.
9. Guarda la clave SSH privada que Oracle te entregue o sube tu clave publica.

Si Oracle devuelve `Out of capacity for shape VM.Standard.A1.Flex`, no es un error de configuracion del proyecto. Significa que no hay capacidad disponible en ese availability domain para el shape Always Free. Prueba:

```text
AD 1
AD 2
AD 3
```

Si sigue fallando, vuelve a intentarlo mas tarde. En cuentas Always Free es habitual que `VM.Standard.A1.Flex` no tenga capacidad inmediata.

No cambies a shapes que no indiquen `Always Free-eligible`, como `VM.Standard.A2.Flex`, si quieres evitar costes. Como alternativa de emergencia puedes usar `VM.Standard.E2.1.Micro`, pero solo tiene 1 GB de RAM y para esta app con Docker + Spring + MySQL puede quedarse muy corta. Si usas `E2.1.Micro`, crea swap antes de desplegar:

```bash
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
free -h
```

### 3.1. Alternativas Si Oracle No Tiene Capacidad

Para este proyecto, la opcion gratuita mas adecuada sigue siendo OCI `VM.Standard.A1.Flex`, porque permite Docker Compose con varios contenedores y persistencia local. Si no hay capacidad, estas son las alternativas realistas:

- `Google Cloud Free Tier e2-micro`: puede ser Always Free en regiones concretas de Estados Unidos, con limites de disco y trafico. Tiene poca RAM para Spring + MySQL + Docker, asi que requeriria swap y vigilancia de billing. No es ideal si tu prioridad absoluta es cero riesgo de coste.
- `Render Free`: permite web services gratuitos, pero los duerme tras inactividad, el filesystem es efimero, Free Postgres expira y no sirve bien para este stack MySQL + uploads persistentes. Ademas, SMTP por puertos comunes como 587 puede estar restringido.
- `Koyeb Free`: permite un web service gratis, pero el free instance es pequeño y sin volumen persistente para la app. Su base de datos gestionada gratuita es PostgreSQL, no MySQL, por lo que habria que migrar la app de MySQL a PostgreSQL.
- `Fly.io`: ya no es una opcion de free tier general para cuentas nuevas; funciona por pago por uso.

Conclusion practica: si necesitas gratis completo con Docker Compose y MySQL, espera o reintenta OCI A1. Si aceptas cambiar arquitectura, la alternativa mas viable seria migrar la base de datos a PostgreSQL y desplegar en Koyeb/Render con sus limitaciones gratuitas.

### 4. Reglas De Red En OCI

En la Security List o Network Security Group de la VM, deja solo:

```text
TCP 22   desde tu IP publica personal
TCP 80   desde 0.0.0.0/0
TCP 443  desde 0.0.0.0/0
```

No abras `3306`. No abras `8080`. La aplicacion Java queda detras de Caddy y MySQL queda interno.

Si no sabes tu IP publica, buscala desde tu navegador con "what is my ip" y usa `/32`, por ejemplo:

```text
203.0.113.10/32
```

### 5. Instalar Docker En La VM

Conectate por SSH:

```bash
ssh ubuntu@IP_DEL_SERVIDOR
```

Actualiza el servidor:

```bash
sudo apt update
sudo apt upgrade -y
```

Instala Docker:

```bash
curl -fsSL https://get.docker.com | sudo sh
sudo usermod -aG docker $USER
newgrp docker
```

Comprueba:

```bash
docker --version
docker compose version
```

### 6. Subir El Proyecto Al Servidor

Clona la rama de despliegue:

```bash
git clone URL_DE_TU_REPO FastDeliveryv1
cd FastDeliveryv1
git checkout NOMBRE_DE_TU_RAMA_REMOTA
```

Prepara el `.env`:

```bash
cp .env.production.example .env
nano .env
```

Valores que debes cambiar:

```env
APP_BASE_URL=https://tu-dominio.com
API_BASE_URL=https://tu-dominio.com
CADDY_SITE_ADDRESS=tu-dominio.com
PAYPAL_SUCCESS_URL=https://tu-dominio.com/pedidos/success
PAYPAL_CANCEL_URL=https://tu-dominio.com/pedidos/cancel
MYSQL_ROOT_PASSWORD=password-root-segura
MYSQL_PASSWORD=password-db-segura
SPRING_DATASOURCE_PASSWORD=password-db-segura
JWT_SECRET=secreto-largo-unico-de-al-menos-64-caracteres
SPRING_MAIL_USERNAME=tu-correo
SPRING_MAIL_PASSWORD=tu-app-password
PAYPAL_CLIENT_ID=tu-client-id
PAYPAL_CLIENT_SECRET=tu-client-secret
```

La base de datos debe quedarse asi cuando uses Docker Compose:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://usuario-mysql:3306/FastDelivery
SPRING_DATASOURCE_USERNAME=caen
MYSQL_USER=caen
MYSQL_DATABASE=FastDelivery
RUTA_IMAGENES=/app/uploads
UPLOADS_PATH=/app/uploads
```

### 7. Dominio Y Cloudflare

Compra o usa un dominio en cualquier registrador. Recomendado: gestionar DNS con Cloudflare.

1. Añade el dominio a Cloudflare.
2. Cambia los nameservers del dominio por los nameservers que te indique Cloudflare.
3. En Cloudflare DNS, crea estos registros:

```text
Tipo: A
Nombre: @
Contenido: IP_PUBLICA_DEL_SERVIDOR
Proxy: Proxied
TTL: Auto
```

```text
Tipo: CNAME
Nombre: www
Contenido: tu-dominio.com
Proxy: Proxied
TTL: Auto
```

4. En `SSL/TLS`, usa `Full (strict)` cuando Caddy ya tenga certificado activo.
5. Si al principio falla el certificado, espera unos minutos y revisa logs de Caddy.

Mientras no tengas dominio, puedes desplegar por IP cambiando temporalmente:

```env
CADDY_SITE_ADDRESS=:80
APP_BASE_URL=http://IP_DEL_SERVIDOR
API_BASE_URL=http://IP_DEL_SERVIDOR
PAYPAL_SUCCESS_URL=http://IP_DEL_SERVIDOR/pedidos/success
PAYPAL_CANCEL_URL=http://IP_DEL_SERVIDOR/pedidos/cancel
```

Para PayPal real conviene esperar al dominio, porque las URLs de retorno deben ser estables.

### 8. Levantar La Aplicacion

Desde la carpeta del proyecto:

```bash
docker compose config --quiet
docker compose up -d --build
```

Verifica contenedores:

```bash
docker compose ps
```

Ver logs:

```bash
docker compose logs -f app
docker compose logs -f caddy
docker compose logs -f usuario-mysql
```

Probar:

```bash
curl -I https://tu-dominio.com
```

### 9. Actualizar Despliegue

Cuando subas cambios a GitHub:

```bash
git pull
docker compose up -d --build
docker image prune -f
```

### 10. Copias Y Persistencia

Los datos importantes quedan aqui:

```text
mysql-data     volumen Docker de MySQL
./uploads      imagenes/subidas de la aplicacion
caddy-data     certificados HTTPS de Caddy
```

Copia rapida de seguridad:

```bash
mkdir -p backups
docker exec usuario-mysql mysqldump -u caen -p FastDelivery > backups/fastdelivery.sql
tar -czf backups/uploads.tar.gz uploads
```

### 11. Medidas Anti-Coste Y DDoS

- Mantener MySQL sin puerto publico.
- Mantener `8080` cerrado al exterior.
- Abrir SSH solo a tu IP.
- Usar Cloudflare con DNS proxied para `@` y `www`.
- Activar Budget Alerts en OCI.
- Revisar `Billing & Cost Management > Cost Analysis`.
- Si ves trafico raro, parar la app:

```bash
docker compose down
```

- Si quieres bloquear web sin apagar la VM:

```bash
sudo iptables -I INPUT -p tcp --dport 80 -j DROP
sudo iptables -I INPUT -p tcp --dport 443 -j DROP
```

Para quitar ese bloqueo:

```bash
sudo iptables -D INPUT -p tcp --dport 80 -j DROP
sudo iptables -D INPUT -p tcp --dport 443 -j DROP
```

### 12. Referencias Oficiales

- Oracle Always Free Resources: https://docs.oracle.com/iaas/Content/FreeTier/resourceref.htm
- Oracle Budgets: https://docs.oracle.com/iaas/Content/Billing/Concepts/budgetsoverview.htm
- Oracle Compartment Quotas: https://docs.oracle.com/en-us/iaas/Content/General/Concepts/resourcequotas.htm
- Oracle WAF Rate Limiting: https://docs.oracle.com/iaas/Content/WAF/RateLimiting/rate_limiting_rule_management.htm
- Cloudflare Proxied DNS Records: https://developers.cloudflare.com/dns/manage-dns-records/reference/proxied-dns-records/
- Cloudflare SSL Full Strict: https://developers.cloudflare.com/ssl/origin-configuration/ssl-modes/full-strict/

## Estructura del Proyecto


### Configuración y Dependencias

- `pom.xml`: Archivo de configuración de Maven.
- `Dockerfile`: Archivo de configuración para la creación de la imagen Docker.
- `docker-compose.yml`: Archivo de configuración para la orquestación de contenedores Docker.

### Código Fuente

#### Paquete Principal

- `src/main/java/org/proyecto/fastdeliveryp_v1/FastDeliveryPV1Application.java`: Clase principal para iniciar la aplicación.

#### Controladores

- `AuthController.java`: Controlador para autenticación de usuarios.
- `CarritoController.java`: Controlador para gestión del carrito de compras.
- `ClienteController.java`: Controlador para gestión de clientes.
- `CustomErrorController` : Controlador para el manejo de errores
- `HomeController.java`: Controlador para la página principal.
- `MotoController.java`: Controllador para manejar las motos asignadas
- `PasswordResetController.java`: Controlador para restablecimiento de contraseñas.
- `PayPalController.java`: Controlador para integración con PayPal.
- `PedidoClienteController.java`: Controlador para gestión de pedidos de clientes.
- `PedidoProveedorController.java`: Controlador para gestiónde de pedidos a proveedores
- `ProveedorController.java`: Controlador para manejar a los proveedores
- `ProductoController.java`: Controlador para gestión de productos.
- `RepartidorController.java`: Controlador para el manejo de los repartidores
- `StockController.java`: Controlador para la gestion del stock
- `UserProfileController.java`: Controlador para gestión de perfiles de usuario.
- `WebSocketController.java`: Controlador para gestión de WebSockets.

#### RestController

- `AdminRestController.java`: Controlador para la gestion de los usuarios administradores
- `MotoRestController.java`: Controlador para gestión de motos.
- `ClienteRestController.java`: Controlador para la gestion de los usuarios clientes
- `CocheRestController.java`: Controlador para la gestion de los coches
- `PedidoClienteRestController.java`: Controlador para la gestion de pedidos de clientes
- `PedidoProveedorRestController.java`: Controlador para la gestion de pedidos a proveedores
- `PersonaRestController.java`: Controlador de gestion centralizada de los usuarios 
- `ProductoRestController.java`: Controlador para gestion de los productos
- `ProveedorRestController.java`: Controlador para gestion de los proveedores
- `RepartidorRestController.java`: Controlador para gestion de Repartidores
- `VehiculoRestController.java`: Controlador para la gestion de vehiculos

#### Entidades
- `Persona.java`: Entidad General de atributos de (Clientesm,Repartidores,Administradores).
- `Admin.java`: Entidad para administradores.
- `Cliente.java`: Entidad para clientes.
- `Coche.java`: Entidad para coches.
- `Moto.java`: Entidad para motos.
- `PedidoCliente.java`: Entidad para pedidos de clientes.
- `PedidoClienteProducto.java`: Entidad para la relación entre pedidos de clientes y productos.
- `PedidoProveedor.java`: Entidad para pedidos de proveedores.
- `PedidoProveedorProducto.java`: Entidad para la relación entre pedidos de proveedores y productos.
- `Producto.java`: Entidad para productos.
- `Proveedor.java`: Entidad para proveedores.
- `Repartidor.java`: Entidad para repartidores.
- `Stock.java`: Entidad para stock de productos.
- `Vehiculo.java`: Entidad para vehículos.

#### Clases
- `CarritoItem.java`: Clase Para logica del carrito
- `Notification.java`: Manejo de las notificaciones del websock
- `NotificationMessage.java`: Manejo del Mensaje del websock
- `ImageUrlValidator.java`: Validacion de numeros
- `NumericValidator.java`: Validador de tipo de archivo

#### Configuración

- `MailConfig.java`: Configuración para el envío de correos electrónicos.
- `PaypalConfig.java`: Configuración para integración con PayPal.
- `WebConfig.java`: Configuración general de la aplicación web.
- `WebSocketConfig.java`: Configuración para WebSockets.
- `AppConfig.java`: Configuración del bean RestTemplate para inyección de dependencias
- `CookieInterceptor.java`: Método que intercepta la solicitud HTTP y añade la cookie JWT al encabezado si está presente.


#### Seguridad

- `JwtTokenUtil.java`: Utilidad para manejo de operaciones con JWT.
- `PasswordEncryptor.java`: Componente para cifrado de contraseñas al iniciar la aplicación.
- `JwtTokenFilter.java`: Filtro para autenticación de peticiones usando JWT.
- `SecurityConfig.java`: Configuración de seguridad para la aplicación.

### Recursos y Plantillas

- `src/main/resources/templates`: Plantillas HTML para la interfaz de usuario, login, register
    - `home/*`: Página principal y Gestor
    - `carrito/ver.html`: Gestion de pago de productos
    - `clientes/*`: Plantillas relacionadas con la gestión de Clientes
    - `error/*`: Plantillas relacionadas con los errores de la web
    - `repartidores/*`: Plantillas relacionadas con los repartidores
    - `stock/*`: Plantillas relacionadas con el manejo del stock
    - `pedidos/*`: Plantillas relacionadas con la gestión de pedidos.
    - `productos/*`: Plantillas relacionadas con la gestión de productos.
    - `profile/*`: Plantillas relacionadas con los perfiles de usuario.
    - `fragmentos/*`: Fragmentos de plantillas reutilizables (e.g., navegación, pie de página).

### Pruebas

- `src/test/java/org/proyecto/fastdeliveryp_v1/FastDeliveryPV1ApplicationTests.java`: Pruebas para la aplicación principal.
- `CarritoControllerTest.java`: Pruebas para el controlador del carrito de compras.
- `JwtTokenUtilTest.java`: Pruebas para utilidades de tokens JWT.
- `ProductoServiceTest.java`: Pruebas para el servicio de productos.

## Contribuciones

Las contribuciones son bienvenidas. Por favor, envía un pull request o abre un issue para discutir cualquier cambio que te gustaría realizar.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

### Contacto

Para consultas adicionales, puedes contactarme a través de:

- Email: alberto.caen.1@gmail.com
- GitHub: https://github.com/Albertocaen
