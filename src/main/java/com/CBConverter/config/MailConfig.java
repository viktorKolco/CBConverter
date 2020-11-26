package com.CBConverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private final static String host = "smtp.yandex.ru";
    //todo: поменять почту для рассылок
    private final static String username = "kalko1996@yandex.ru";

    private final static String password = "";

    private final static int port = 465;

    private final static String debug = "true";

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.debug", debug);

        return mailSender;
    }
}
