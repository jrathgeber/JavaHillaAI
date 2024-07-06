package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "my-hilla-app")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {

        // SpringApplication.run(Application.class, args);

        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);
        //Arrays.stream(app.getBeanDefinitionNames()).forEach(System.out::println);

    }

}
