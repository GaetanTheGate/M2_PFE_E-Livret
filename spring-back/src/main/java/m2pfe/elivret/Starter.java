package m2pfe.elivret;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.EUser.EUser;

/**
 * <p>
 * A Spring Boot service used to start the web application.
 * </p>
 * <p>
 * The application is a web application that allows actors of an "elivret
 * d'alternance" to easily answer and complete an elivret.
 * </p>
 * 
 * @see ELivret
 * @see EUser
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@SpringBootApplication(exclude = { SecurityFilterAutoConfiguration.class })
@EnableJpaRepositories(basePackageClasses = Starter.class)
@EntityScan(basePackageClasses = Starter.class)
@EnableTransactionManagement
public class Starter {
    /**
     * The function called to run the application with Spring Boot.
     */
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
