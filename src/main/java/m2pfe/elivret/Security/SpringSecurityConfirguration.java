package m2pfe.elivret.Security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import io.jsonwebtoken.JwtHandler;
import m2pfe.elivret.Authentification.JwtAuthorizationFilter;
import m2pfe.elivret.Authentification.JwtManager;

/**
 * <p>
 * Enable Spring security for the project and configure it.
 * </p>
 * <p>
 * Configure the security to filter the request with JWT.
 * </p>
 * 
 * @see JwtHandler
 * @see WebSecurityConfigurerAdapter
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
// @Profile("usejwt")
@Configuration
@EnableWebSecurity
public class SpringSecurityConfirguration extends WebSecurityConfigurerAdapter {
    /**
     * The handler for the tokens (JWT).
     */
    @Autowired
    private JwtManager jwt;

    /**
     * <p>
     * Initialize the SpringSecurity.
     * </p>
     */
    @PostConstruct
    public void init(){

    }

    /**
     * <p>
     * Filter any entering http request so that it must : <ul>
     *  <li>Disable the <i>CSRF</i></li>
     *  <li>The policy of the session management is <i>STATELESS</i></li>
     *  <li>Grant access to all the request.</li>
     *  <li>Apply a JWT filter </li>
     * </ul>
     * </p>
     * 
     * @see HttpSecurity
     * @see JwtAuthorizationFilter
     * @see JwtFiltrerConfigurer
     * 
     * @param http The request to filter
     * @return The SecurityChainFilter created.
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf(c -> c.disable())
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(basic -> basic.disable())
            .authorizeRequests(requests -> requests
                .anyRequest().permitAll()
                )
            .apply(new JwtFiltrerConfigurer(jwt));

        //return http.build();
    }
}
