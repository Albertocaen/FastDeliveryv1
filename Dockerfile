FROM eclipse-temurin:22-jdk-alpine AS build

WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:22-jre-alpine

RUN apk add --no-cache apache2 netcat-openbsd

WORKDIR /app

COPY --from=build /workspace/target/FastDeliveryP_v1.jar app.jar
COPY my-directory.conf /etc/apache2/conf.d/

RUN mkdir -p /var/www/logs /app/uploads && \
    touch /var/www/logs/error.log && \
    chmod -R 755 /var/www/logs /app/uploads && \
    echo "Include /etc/apache2/conf.d/*.conf" >> /etc/apache2/httpd.conf

EXPOSE 8080

CMD /bin/sh -c 'httpd && \
  until nc -z "${DB_HOST:-usuario-mysql}" "${DB_PORT:-3306}"; do \
    echo "Esperando a la base de datos en ${DB_HOST:-usuario-mysql}:${DB_PORT:-3306}..."; \
    sleep 2; \
  done; \
  echo "Base de datos lista"; \
  java ${JAVA_OPTS:-} -jar /app/app.jar'
