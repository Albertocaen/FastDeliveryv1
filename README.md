# Fast Delivery

Fast Delivery es una solución integral para la gestión y optimización de entregas de productos. Este proyecto tiene como objetivo proporcionar una plataforma eficiente y confiable para gestionar pedidos, rastrear envíos y facilitar la comunicación entre clientes y proveedores.

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
- `HomeController.java`: Controlador para la página principal.
- `PasswordResetController.java`: Controlador para restablecimiento de contraseñas.
- `PayPalController.java`: Controlador para integración con PayPal.
- `PedidoClienteController.java`: Controlador para gestión de pedidos de clientes.
- `ProductoController.java`: Controlador para gestión de productos.
- `UserProfileController.java`: Controlador para gestión de perfiles de usuario.
- `WebSocketController.java`: Controlador para gestión de WebSockets.

#### Entidades

- `Admin.java`: Entidad para administradores.
- `CarritoItem.java`: Entidad para items en el carrito de compras.
- `Cliente.java`: Entidad para clientes.
- `Coche.java`: Entidad para coches.
- `Moto.java`: Entidad para motos.
- `Notification.java`: Entidad para notificaciones.
- `PedidoCliente.java`: Entidad para pedidos de clientes.
- `PedidoProveedor.java`: Entidad para pedidos de proveedores.
- `Producto.java`: Entidad para productos.
- `Usuario.java`: Entidad para usuarios.

#### Configuración

- `MailConfig.java`: Configuración para el envío de correos electrónicos.
- `PaypalConfig.java`: Configuración para integración con PayPal.
- `WebConfig.java`: Configuración general de la aplicación web.
- `WebSocketConfig.java`: Configuración para WebSockets.

### Recursos y Plantillas

- `src/main/resources/templates`: Plantillas HTML para la interfaz de usuario.
    - `home/index.html`: Página principal.
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

- Email: tu-email@example.com
- GitHub: [tu-usuario](https://github.com/Albertocaen)
