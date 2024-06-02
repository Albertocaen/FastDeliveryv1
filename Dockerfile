# Usar una imagen base de OpenJDK con Alpine
FROM eclipse-temurin:22-jdk-alpine

# Instalar Apache
RUN apk add --no-cache apache2

# Crear un directorio para la aplicación
WORKDIR /app

# Copiar el JAR desde tu máquina local al contenedor
COPY FastDeliveryP_v1-0.0.1-SNAPSHOT.jar app.jar

# Copiar el archivo de configuración de Apache desde tu máquina local al contenedor
COPY my-directory.conf /etc/apache2/conf.d/

# Crear y configurar los directorios para los logs de Apache
RUN mkdir -p /var/www/logs && \
    touch /var/www/logs/error.log && \
    chmod -R 755 /var/www/logs

# Configurar ServerName para suprimir la advertencia
RUN echo "ServerName localhost" >> /etc/apache2/httpd.conf

# Incluir la configuración de Apache desde el directorio conf.d
RUN echo "Include /etc/apache2/conf.d/*.conf" >> /etc/apache2/httpd.conf

# Exponer el puerto en el que la aplicación se ejecuta
EXPOSE 8080

# Iniciar Apache en segundo plano y luego ejecutar la aplicación
CMD ["/bin/sh", "-c", "httpd && java -jar /app/app.jar"]
