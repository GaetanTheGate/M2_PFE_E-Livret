package m2pfe.elivret.Authentification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import m2pfe.elivret.EUser.EUserDTO;

/**
 * <p>
 * Rest controller for the authentification system.
 * </p>
 * <p>
 * Manages the request from the path "/authentification".
 * </p>
 * <p>
 * Gives and takes the EUser DTOs when processing the request.
 * </p>
 * 
 * @see EUserDTO
 * @see AuthentificationService
 * @see JwtManager
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@RestController
@RequestMapping("/authentification")
public class AuthentificationController {
    /**
     * Service used to do the authentification in the application.
     * 
     * @see AuthentificationService
     */
    @Autowired
    private AuthentificationService service;

    /**
     * Handler used for JWT manipulation.
     * 
     * @see JwtManager
     */
    @Autowired
    private JwtManager jwt;
    
    /**
     * <p>
     * <b>POST</b> request on path <b>"/login"</b>
     * </p>
     * <p>
     * Allow one to log in with an email and a password.
     * </p>
     * <p>
     * If the user is known, and the password is correct, creates, saves, and return a token the user will be able to use to tell who he is.
     * </p>
     * 
     * @see EUserDTO.In.AuthentificationInformation
     * @see AuthentificationService
     * @see #AuthentificationService.login(String, String)
     * 
     * @param auth The DTO giving the information of authentification.
     * @return A response entity with the status of the request containing :<ul>
     *  <li>The token if it was created successfuly.</li>
     *  <li>The <i>error message</i> otherwise.</li>
     * </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody EUserDTO.In.AuthentificationInformation auth) {
        try {
            String token = service.login(auth.getEmail(), auth.getPassword());

            return ResponseEntity.ok(token);
        } catch (AuthentificationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    /**
     * <p>
     * <b>POST</b> request on path <b>"/logout"</b>
     * </p>
     * <p>
     * Allow one to log out from their token. Return the status of the request.
     * </p>
     * <p>
     * Will simply make the application forget the token, rendering it useless.
     * </p>
     * 
     * @see AuthentificationService
     * @see #AuthentificationService.logout(String)
     * @see JwtManager
     * 
     * @param token The token the application will forget.
     * 
     * @return The responseEntity of the request
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest req) {
        String token = jwt.resolveToken(req);

        try {
            service.logout(token);

            return ResponseEntity.ok(null);
        } catch (AuthentificationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
