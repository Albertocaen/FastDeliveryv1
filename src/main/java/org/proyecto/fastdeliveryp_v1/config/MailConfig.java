package org.proyecto.fastdeliveryp_v1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    // Inyección del valor del nombre de usuario desde el archivo de propiedades
    @Value("${spring.mail.username}")
    private String name;

    // Inyección del valor de la contraseña desde el archivo de propiedades
    @Value("${spring.mail.password}")
    private String password;

    // Inyección del valor del host desde el archivo de propiedades
    @Value("${spring.mail.host}")
    private String host;

    /**
     * Configura y proporciona un bean JavaMailSender.
     * @return una instancia configurada de JavaMailSender.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configura el host y el puerto del servidor de correo
        mailSender.setHost(host);
        mailSender.setPort(587);

        // Configura las credenciales de autenticación
        mailSender.setUsername(name);
        mailSender.setPassword(password);

        // Configura las propiedades adicionales para la conexión SMTP (Simple Mail Transfer Protocol), protocolo estandar para
        // la transmision de correos
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}