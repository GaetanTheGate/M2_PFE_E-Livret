package m2pfe.elivret.Security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import m2pfe.elivret.Authentification.JwtAuthorizationFilter;
import m2pfe.elivret.Authentification.JwtManager;

/**
 * <p>
 * Configure any request to be filtered with the JwtFilter.
 * </p>
 * 
 * @see JwtManager
 * @see JwtAuthorizationFilter
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class JwtFiltrerConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    /**
     * The JwtManager the filter will be using.
     * 
     * @see JwtManager
     * @see JwtAuthorizationFilter
     */
    private JwtManager jwt;

    /**
     * <p>
     * Constructor of the JwtFiltrerConfigurer class.
     * </p>
     * 
     * @see JwtManager
     * @see JwtAuthorizationFilter
     * 
     * @param manager The JwtManager the filter will be using.
     */
    public JwtFiltrerConfigurer(JwtManager manager) {
        this.jwt = manager;
    }

    /**
     * <p>
     * Configure the request so that they are filtered with the JwtFilter.
     * </p>
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtAuthorizationFilter customFilter = new JwtAuthorizationFilter(jwt);

        http
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
