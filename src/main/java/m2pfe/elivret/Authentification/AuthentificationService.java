package m2pfe.elivret.Authentification;

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
 * @version 1.0
 */
@Service
public class AuthentificationService {
    // TODO: Comment.
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
     * If the authentification informations matches something, creates and return a token for the user.
     * </p>
     * 
     * @see JwtManager
     * @see #JwtManager.createToken(EUser)
     * @see AuthentificationException
     * 
     * @param email The email used to identify the user?
     * @param password The password of the user.
     * @return The token created.
     * @throws AuthentificationException if it couldn't log in the user.
     */
    public String login(String email, String password) throws AuthentificationException {
        try {
            authentication.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            // TODO: check the authentification to see if email and password are correct.

            EUser user = ur.findByEmail(email)
                .orElseThrow(Exception::new); // TODO : Une vrai exception

            return jwt.createToken(user);
        } catch (Exception e) {
            throw new AuthentificationException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
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
     */
    public void logout(String token) {
        jwt.forgetToken(token);
    }
}
