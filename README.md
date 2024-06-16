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

## Estructura del Proyecto

#### Pasos Realizados para el Despliegue en Azure

1. **Preparación del Entorno**
   - **Creación de Variables de Entorno:**
     - Se creó un archivo `.env` con las variables de entorno necesarias para la configuración de la base de datos y otros servicios.

2. **Configuración del Archivo JSON para Azure**
   - Se configuró un archivo JSON para definir los parámetros de despliegue en Azure, como la imagen del contenedor, los puertos expuestos y las variables de entorno.

3. **Comandos Utilizados para el Despliegue**
   - **Inicio de Sesión en Azure CLI:**
     ```sh
     az login
     ```
   - **Creación del Grupo de Recursos:**
     ```sh
     az group create --name myResourceGroup --location eastus
     ```
   - **Creación del Registro de Contenedores de Azure:**
     ```sh
     az acr create --resource-group myResourceGroup --name myRegistry --sku Basic
     ```
   - **Inicio de Sesión en el Registro de Contenedores de Azure:**
     ```sh
     az acr login --name myRegistry
     ```
   - **Construcción y Etiquetado de la Imagen Docker:**
     ```sh
     docker build -t mydockerimage:latest .
     docker tag mydockerimage:latest myRegistry.azurecr.io/mydockerimage:latest
     ```
   - **Publicación de la Imagen en el Registro de Contenedores de Azure:**
     ```sh
     docker push myRegistry.azurecr.io/mydockerimage:latest
     ```
   - **Despliegue del Contenedor en Azure Container Instances:**
     ```sh
        az container create --resource-group FastDelivery --file container-config.json
     ```

4. **Configuración de la Aplicación**
   - **Configuración de Variables de Entorno en Azure: Las variables de entorno fueron definidas en el json con el comando**
     ```sh
     az container create --resource-group FastDelivery --file container-config.json
     ```

5. **Monitoreo y Logs**
   - **Visualización de Logs en Vivo:**
     ```sh
     az container logs --resource-group myResourceGroup --name myContainer --follow
     ```

6. **Detención del Contenedor en Azure**
   - **Uso de los siguientes comandos para detener el contenedor cuando no está en uso:**
     ```sh
     az container stop --resource-group myResourceGroup --name myContainer
     ```
---

#### Pasos Adicionales Realizados

1. **Configuración de Puertos y Conexiones**
   - Se habilitaron conexiones a todos los puertos necesarios en la configuración de red de Azure para asegurar que el tráfico adecuado pueda llegar a la aplicación.

2. **Ajustes de Seguridad en Contraseñas**
   - Se actualizaron las contraseñas en el archivo `.env` y en el sistema para garantizar que cumplan con los requisitos de seguridad, incluyendo caracteres especiales como el punto (.).

3. **Resolución de Errores de Redirección**
   - Se abordaron y corrigieron errores de redirección relacionados con la gestión de sesiones y la autenticación en la aplicación.

4. **Ajustes de CSS y Responsividad**
   - Se realizaron ajustes adicionales en el CSS para asegurar que la interfaz se vea correctamente en dispositivos móviles y pantallas de diferentes tamaños.
   - Problemas específicos con la imagen de fondo y el menú de navegación fueron solucionados para mejorar la apariencia y funcionalidad en dispositivos móviles.


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
