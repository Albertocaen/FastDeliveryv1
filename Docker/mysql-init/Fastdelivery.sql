CREATE DATABASE IF NOT EXISTS FastDelivery;

USE FastDelivery;

CREATE TABLE PERSONA (
 DNI VARCHAR(20) PRIMARY KEY,
 email VARCHAR(50),
 nombre VARCHAR(50),
 apellido VARCHAR(50),
 telefono VARCHAR(20),
 contrasena VARCHAR(50),
 usuario VARCHAR(50),
 CONSTRAINT chk_persona_usuario CHECK (usuario IN ('admin', 'cliente', 'repartidor'))
);

CREATE TABLE VEHICULO (
                          placa_vehiculo VARCHAR(20) PRIMARY KEY,
                          marca VARCHAR(50),
                          modelo VARCHAR(50),
                          color VARCHAR(50),
                          tipo ENUM('MOTO', 'COCHE')
);

CREATE TABLE MOTO (
                      placa_vehiculo VARCHAR(20) PRIMARY KEY,
                      cilindrada INT,
                      FOREIGN KEY (placa_vehiculo) REFERENCES VEHICULO(placa_vehiculo)
);

CREATE TABLE COCHE (
                       placa_vehiculo VARCHAR(20) PRIMARY KEY,
                       capacidad_carga FLOAT,
                       tipo_combustible VARCHAR(50),
                       FOREIGN KEY (placa_vehiculo) REFERENCES VEHICULO(placa_vehiculo)
);

CREATE TABLE REPARTIDOR (
                            DNI_REPARTIDOR VARCHAR(20) PRIMARY KEY,
                            calificacion FLOAT,
                            horario_trabajo VARCHAR(100),
                            estado_de_disponibilidad VARCHAR(50),
                            cantidad_pedidos INT,
                            placa_vehiculo VARCHAR(20),
                            CONSTRAINT fk_repartidor_persona FOREIGN KEY (DNI_REPARTIDOR) REFERENCES PERSONA(DNI),
                            CONSTRAINT fk_repartidor_vehiculo FOREIGN KEY (placa_vehiculo) REFERENCES VEHICULO(placa_vehiculo),
                            CONSTRAINT max_pedidos_repartidor CHECK (cantidad_pedidos <= 3)
);

CREATE TABLE CLIENTE (
                         DNI_CLIENTE VARCHAR(20) PRIMARY KEY,
                         direccion VARCHAR(100),
                         ciudad VARCHAR(50),
                         codigo_postal VARCHAR(10),
                         cliente_pedido VARCHAR(20),
                         CONSTRAINT fk_cliente_persona FOREIGN KEY (DNI_CLIENTE) REFERENCES PERSONA(DNI),
                         CONSTRAINT fk_cliente_repartidor FOREIGN KEY (cliente_pedido) REFERENCES REPARTIDOR(DNI_REPARTIDOR)
);

CREATE TABLE ADMIN (
                       DNI_ADMIN VARCHAR(20) PRIMARY KEY,
                       FOREIGN KEY (DNI_ADMIN) REFERENCES PERSONA(DNI)
);

CREATE TABLE PROVEEDOR (
                           id_proveedor INT PRIMARY KEY,
                           nombre_empresa VARCHAR(50),
                           telefono VARCHAR(20),
                           email VARCHAR(50)
);

CREATE TABLE PRODUCTO (
                          id_producto INT PRIMARY KEY,
                          nombre VARCHAR(50),
                          descripcion VARCHAR(1000),
                          precio FLOAT,
                          id_proveedor_producto INT,
                          FOREIGN KEY (id_proveedor_producto) REFERENCES PROVEEDOR(id_proveedor)
);

CREATE TABLE STOCK (
                       id_stock INT PRIMARY KEY,
                       cantidad INT,
                       fecha_ingreso DATE,
                       id_producto INT,
                       DNI_ADMIN VARCHAR(20),
                       FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto),
                       FOREIGN KEY (DNI_ADMIN) REFERENCES ADMIN(DNI_ADMIN)
);

CREATE TABLE INCIDENCIA (
                            id_incidencia INT PRIMARY KEY,
                            descripcion VARCHAR(200),
                            fecha DATE,
                            tipo VARCHAR(50),
                            hora TIME,
                            id_stock INT,
                            FOREIGN KEY (id_stock) REFERENCES STOCK(id_stock)
);

CREATE TABLE PEDIDO_CLIENTE (
                                id_pedido_cliente INT PRIMARY KEY,
                                cantidad INT,
                                fecha_pedido DATE,
                                peso FLOAT,
                                estado VARCHAR(50),
                                dni_cliente_pedido VARCHAR(20),
                                dni_repartidor_pedido VARCHAR(20),
                                FOREIGN KEY (dni_cliente_pedido) REFERENCES CLIENTE(DNI_CLIENTE)
);

CREATE TABLE PEDIDO_PROVEEDOR (
                                  id_pedido_proveedor INT PRIMARY KEY,
                                  cantidad INT,
                                  fecha_pedido DATE,
                                  estado VARCHAR(50),
                                  id_proveedor_pedido INT,
                                  dni_admin_pedido VARCHAR(50),
                                  FOREIGN KEY (id_proveedor_pedido) REFERENCES PROVEEDOR(id_proveedor),
                                  FOREIGN KEY (dni_admin_pedido) REFERENCES ADMIN(DNI_ADMIN)
);

CREATE TABLE PEDIDO_PROVEEDOR_PRODUCTO (
                                           id_pedido_proveedor_producto INT PRIMARY KEY,
                                           id_pedido_proveedor_pp INT,
                                           id_producto_pp INT,
                                           cantidad INT,
                                           precio_unitario DECIMAL(10,2),
                                           FOREIGN KEY (id_pedido_proveedor_pp) REFERENCES PEDIDO_PROVEEDOR(id_pedido_proveedor),
                                           FOREIGN KEY (id_producto_pp) REFERENCES PRODUCTO(id_producto)
);

ALTER TABLE PEDIDO_CLIENTE ADD COLUMN id_producto INT;
ALTER TABLE PEDIDO_CLIENTE ADD CONSTRAINT fk_pedido_cliente_producto FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto);

CREATE TABLE PEDIDO_CLIENTE_PRODUCTO (
                                         id_pedido_cliente_producto INT PRIMARY KEY,
                                         id_pedido_cliente_pp INT,
                                         id_producto_pp INT,
                                         cantidad INT,
                                         precio_unitario FLOAT,
                                         subtotal FLOAT,
                                         FOREIGN KEY (id_pedido_cliente_pp) REFERENCES PEDIDO_CLIENTE(id_pedido_cliente),
                                         FOREIGN KEY (id_producto_pp) REFERENCES PRODUCTO(id_producto)
);
