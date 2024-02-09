package m2pfe.elivret.Authentification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserRepository;

/**
 * <p>
 * Service for authentification methods using tokens (JWT).
 * </p>
 * 
 * @see JwtManager
 * @see EUserRepository
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
@Service
public class AuthentificationService {
    /**
     * Attribute used to check the validity of an authentification attempt.
     * 
     * @see AuthenticationManager
     */
    @Autowired
    AuthenticationManager authentication;

    /**
     * Attribute used to handle the tokens (JWT).
     * 
     * @see JwtManager
     */
    @Autowired
    private JwtManager jwt;

    /**
     * Attribute used to fetch EUsers.
     * 
     * @see EUserRepository
     */
    @Autowired
    private EUserRepository ur;

    /**
     * <p>
     * Log in a user from its email and password.
     * </p>
     * <p>
     * If the authentification informations matches something, creates and return a
     * token for the user.
     * </p>
     * 
     * @see JwtManager
     * @see #JwtManager.createToken(EUser)
     * @see AuthentificationException
     * 
     * @param email    The email used to identify the user?
     * @param password The password of the user.
     * @return The token created.
     * @throws AuthentificationException if it couldn't log in the user.
     */
    public String login(String email, String password) throws AuthentificationException {
        try {
            authentication.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            EUser user = ur.findByEmail(email)
                    .orElseThrow(Exception::new);

            return jwt.createToken(user);
        } catch (Exception e) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Invalid username/password supplied");
        }
    }

    /**
     * <p>
     * Log out an user from the application using its token.
     * </p>
     * <p>
     * Makes the application forget the token.
     * </p>
     * 
     * @see JwtManager
     * @see #JwtManager.forgetToken(String)
     * 
     * @param token The token to forget.
     * @throws AuthentificationException
     */
    public void logout(String token) throws AuthentificationException {
        jwt.forgetToken(token);
    }

    /**
     * <p>
     * Log out an user from the application using its token from the HttpRequest.
     * </p>
     * <p>
     * Makes the application forget the token.
     * </p>
     * 
     * @see #logout(String)
     * @see HttpServletRequest
     * @see JwtManager
     * @see #JwtManager.forgetToken(String)
     * 
     * @param req The request where the token is.
     * @throws AuthentificationException
     */
    public void logout(HttpServletRequest req) throws AuthentificationException {
        String token = jwt.resolveToken(req);

        logout(token);
    }

    /**
     * <p>
     * Fetch the user linked to the token.
     * </p>
     * 
     * @see JwtManager
     * 
     * @param token The token associated with the user.
     * @return The current user if found.
     * @throws AuthentificationException
     */
    public EUser whoAmI(String token) throws AuthentificationException {
        String myEmail = jwt.resolveEmail(token);

        return ur.findByEmail(myEmail)
                .orElseThrow(() -> new AuthentificationException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Token's email unknown."));
    }

    /**
     * <p>
     * Fetch the user linked to the token in the request.
     * </p>
     * 
     * @see #whoAmI(String)
     * @see JwtManager
     * 
     * @param req The request where the token associated with the user is.
     * @return The current user if found.
     * @throws AuthentificationException
     */
    public EUser whoAmI(HttpServletRequest req) throws AuthentificationException {
        String token = jwt.resolveToken(req);

        return whoAmI(token);
    }
}
