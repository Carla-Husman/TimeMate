package com.paw.timemate.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class for setting up mail services.
 * It reads mail server properties from the application configuration.
 */
@Configuration
public class MailConfig {
    // Mail server host (e.g., smtp.gmail.com for Gmail)
    @Value("${spring.mail.host}")
    private String mailHost;

    // Mail server port (e.g., 587 for Gmail with STARTTLS)
    @Value("${spring.mail.port}")
    private int mailPort;

    // Username for the mail server authentication (usually the email address)
    @Value("${spring.mail.username}")
    private String mailUsername;

    // Password for the mail server authentication
    @Value("${spring.mail.password}")
    private String mailPassword;

    /**
     * Creates a JavaMailSender bean with the specified mail server properties.
     * This bean is used for sending emails.
     *
     * @return a configured JavaMailSender instance
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
