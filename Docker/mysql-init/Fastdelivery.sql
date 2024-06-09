CREATE DATABASE IF NOT EXISTS FastDelivery;

USE FastDelivery;

CREATE TABLE IF NOT EXISTS PERSONA (
                                       DNI VARCHAR(20) PRIMARY KEY,
                                       email VARCHAR(50),
                                       nombre VARCHAR(50),
                                       apellido VARCHAR(50),
                                       telefono VARCHAR(20),
                                       contrasena VARCHAR(100),
                                       usuario VARCHAR(50),
                                       CONSTRAINT chk_persona_usuario CHECK (usuario IN ('admin', 'cliente', 'repartidor'))
);

CREATE TABLE IF NOT EXISTS VEHICULO (
                                        placa_vehiculo VARCHAR(20) PRIMARY KEY,
                                        marca VARCHAR(50),
                                        modelo VARCHAR(50),
                                        color VARCHAR(50),
                                        tipo ENUM('MOTO', 'COCHE')
);

CREATE TABLE IF NOT EXISTS MOTO (
                                    placa_vehiculo VARCHAR(20) PRIMARY KEY,
                                    cilindrada INT,
                                    FOREIGN KEY (placa_vehiculo) REFERENCES VEHICULO(placa_vehiculo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COCHE (
                                     placa_vehiculo VARCHAR(20) PRIMARY KEY,
                                     capacidad_carga FLOAT,
                                     tipo_combustible VARCHAR(50),
                                     FOREIGN KEY (placa_vehiculo) REFERENCES VEHICULO(placa_vehiculo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS REPARTIDOR (
                                          DNI_REPARTIDOR VARCHAR(20) PRIMARY KEY,
                                          calificacion FLOAT,
                                          horario_trabajo VARCHAR(100),
                                          estado_de_disponibilidad VARCHAR(50),
                                          cantidad_pedidos INT,
                                          placa_vehiculo VARCHAR(20),
                                          CONSTRAINT fk_repartidor_persona FOREIGN KEY (DNI_REPARTIDOR) REFERENCES PERSONA(DNI) ON DELETE CASCADE,
                                          CONSTRAINT fk_repartidor_vehiculo FOREIGN KEY (placa_vehiculo) REFERENCES VEHICULO(placa_vehiculo) ON DELETE SET NULL,
                                          CONSTRAINT max_pedidos_repartidor CHECK (cantidad_pedidos <= 3)
);

CREATE TABLE IF NOT EXISTS CLIENTE (
                                       DNI_CLIENTE VARCHAR(20) PRIMARY KEY,
                                       direccion VARCHAR(100),
                                       ciudad VARCHAR(50),
                                       codigo_postal VARCHAR(10),
                                       cliente_pedido VARCHAR(20),
                                       CONSTRAINT fk_cliente_persona FOREIGN KEY (DNI_CLIENTE) REFERENCES PERSONA(DNI) ON DELETE CASCADE,
                                       CONSTRAINT fk_cliente_repartidor FOREIGN KEY (cliente_pedido) REFERENCES REPARTIDOR(DNI_REPARTIDOR) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ADMIN (
                                     DNI_ADMIN VARCHAR(20) PRIMARY KEY,
                                     FOREIGN KEY (DNI_ADMIN) REFERENCES PERSONA(DNI) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PROVEEDOR (
                                         id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
                                         nombre_empresa VARCHAR(50),
                                         telefono VARCHAR(20),
                                         email VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS PRODUCTO (
                                        id_producto INT AUTO_INCREMENT PRIMARY KEY,
                                        nombre VARCHAR(50),
                                        descripcion VARCHAR(1000),
                                        precio FLOAT,
                                        id_proveedor_producto INT,
                                        img VARCHAR(100),
                                        FOREIGN KEY (id_proveedor_producto) REFERENCES PROVEEDOR(id_proveedor) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS STOCK (
                                     id_stock INT AUTO_INCREMENT PRIMARY KEY,
                                     cantidad INT,
                                     fecha_ingreso DATE,
                                     id_producto INT,
                                     DNI_ADMIN VARCHAR(20),
                                     FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto) ON DELETE CASCADE,
                                     FOREIGN KEY (DNI_ADMIN) REFERENCES ADMIN(DNI_ADMIN) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS PEDIDO_CLIENTE (
                                              id_pedido_cliente INT AUTO_INCREMENT PRIMARY KEY,
                                              cantidad INT,
                                              fecha_pedido DATE,
                                              peso FLOAT,
                                              estado VARCHAR(50),
                                              dni_cliente_pedido VARCHAR(20),
                                              dni_repartidor_pedido VARCHAR(20),
                                              id_producto INT,
                                              FOREIGN KEY (dni_cliente_pedido) REFERENCES CLIENTE(DNI_CLIENTE) ON DELETE CASCADE,
                                              FOREIGN KEY (dni_repartidor_pedido) REFERENCES REPARTIDOR(DNI_REPARTIDOR) ON DELETE SET NULL,
                                              FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS PEDIDO_PROVEEDOR (
                                                id_pedido_proveedor INT AUTO_INCREMENT PRIMARY KEY,
                                                cantidad INT,
                                                fecha_pedido DATE,
                                                estado VARCHAR(50),
                                                id_proveedor_pedido INT,
                                                dni_admin_pedido VARCHAR(20),
                                                FOREIGN KEY (id_proveedor_pedido) REFERENCES PROVEEDOR(id_proveedor) ON DELETE CASCADE,
                                                FOREIGN KEY (dni_admin_pedido) REFERENCES ADMIN(DNI_ADMIN) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS PEDIDO_CLIENTE_PRODUCTO (
                                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                                       id_pedido_cliente INT,
                                                       id_producto INT,
                                                       cantidad INT,
                                                       precio DECIMAL(10, 2),
                                                       fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                       comentarios VARCHAR(255),
                                                       FOREIGN KEY (id_pedido_cliente) REFERENCES PEDIDO_CLIENTE(id_pedido_cliente) ON DELETE CASCADE,
                                                       FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS PEDIDO_PROVEEDOR_PRODUCTO (
                                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                                         id_pedido_proveedor INT,
                                                         id_producto INT,
                                                         cantidad INT,
                                                         precio DECIMAL(10, 2),
                                                         fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                         comentarios VARCHAR(255),
                                                         FOREIGN KEY (id_pedido_proveedor) REFERENCES PEDIDO_PROVEEDOR(id_pedido_proveedor) ON DELETE CASCADE,
                                                         FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto) ON DELETE CASCADE
);