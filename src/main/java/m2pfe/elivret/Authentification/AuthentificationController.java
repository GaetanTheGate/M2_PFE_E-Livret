package m2pfe.elivret.Authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @return <ul>
     *  <li>The token if it was created successfuly.</li>
     *  <li><i>null</i> otherwise?</li>
     * </ul>
     */
    @PostMapping("/login")
    public String login(EUserDTO.In.AuthentificationInformation auth) {
        try {
            return service.login(auth.getEmail(), auth.getPassword());
        } catch (AuthentificationException e) {
            return null;
        }
    }

    /**
     * <p>
     * <b>POST</b> request on path <b>"/logout"</b>
     * </p>
     * <p>
     * Allow one to log out from their token.
     * </p>
     * <p>
     * Will simply make the application forget the token, rendering it useless.</p>
     * 
     * @see AuthentificationService
     * @see #AuthentificationService.logout(String)
     * 
     * @param token The token the application will forget.
     */
    @PostMapping("/logout")
    public void logout(String token) {
        service.logout(token);
    }
}
