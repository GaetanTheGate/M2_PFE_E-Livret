package m2pfe.elivret.Authentification;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <p>
 * Class for filtering http request.
 * </p>
 * <p>
 * Filter http request for JWT validity.
 * </p>
 * 
 * @see JwtManager
 * @see OncePerRequestFilter
 * @see HttpServletRequest
 * @see HttpServletResponse
 * @see FilterChain
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    /**
     * Handler for token (JWT).
     * 
     * @see JwtManager
     */
    private JwtManager jwt;

    /**
     * <p>
     * Default constructor for JwtAuthorizationFilter.
     * </p>
     * 
     * @see JwtManager
     * 
     * @param manager The JwtHandler to use for the filtering.
     */
    public JwtAuthorizationFilter(JwtManager manager) {
        this.jwt = manager;
    }

    /**
     * <p>
     * Filter request for JWT validity.
     * </p>
     * <p>
     * If a token is invalid, send an error on the HttpResponse, and block the
     * Authentification.
     * </p>
     * 
     * @see JwtManager
     * @see #JwtManager.validateToken(String)
     * @see HttpServletRequest
     * @see HttpServletResponse
     * @see FilterChain
     * 
     * @param request     The HttpRequest to process.
     * @param response    The HttpResponse to give.
     * @param filterChain The chain to filter.
     * 
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwt.resolveToken(request);

        try {
            if (jwt.validateToken(token)) {
                Authentication auth = jwt.getAuthentication(token); // TODO : Si possible, déplacer le getAuthentication
                                                                    // pour pas qu'il soit dans le JwtManager.
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            // TODO: Else, also do the catch section ?
        } catch (AuthentificationException e) {

            SecurityContextHolder.clearContext();

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.sendError(e.getStatus().value(), e.getMessage());

            return;
        }

        filterChain.doFilter(request, response);
    }
}
