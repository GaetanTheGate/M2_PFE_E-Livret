package m2pfe.elivret.Authentification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserDTO;
import m2pfe.elivret.EUser.EUserRepository;

import org.springframework.web.bind.annotation.GetMapping;

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
 * @version 1.2
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

    @Autowired
    private EUserRepository u_repo;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();

    /**
     * <p>
     * <b>POST</b> request on path <b>"/login"</b>
     * </p>
     * <p>
     * Allow one to log in with an email and a password.
     * </p>
     * <p>
     * If the user is known, and the password is correct, creates, saves, and return
     * a token the user will be able to use to tell who he is.
     * </p>
     * 
     * @see EUserDTO.In.AuthentificationInformation
     * @see AuthentificationService
     * @see #AuthentificationService.login(String, String)
     * 
     * @param auth The DTO giving the information of authentification.
     * @return A response entity with the status of the request containing :
     *         <ul>
     *         <li>The token if it was created successfuly.</li>
     *         <li>The <i>error message</i> otherwise.</li>
     *         </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody EUserDTO.In.AuthentificationInformation auth) {
        try {
            String token = service.login(auth.getEmail(), auth.getPassword());

            return ResponseEntity.ok(token);
        } catch (AuthentificationException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
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
     * @see #AuthentificationService.logout(HttpServletRequest)
     * @see JwtManager
     * 
     * @param req The header where the token of the user is so that the application
     *            can forget it.
     * 
     * @return The responseEntity of the request
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest req) {
        try {
            service.logout(req);

            return ResponseEntity.ok(null);
        } catch (AuthentificationException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    /**
     * <p>
     * <b>GET</b> request on path <b>"/whoami"</b>
     * </p>
     * <p>
     * Returns the user linked to the token used by the authentificated user.
     * </p>
     * 
     * @see AuthentificationService
     * @see #AuthentificationService.whoAmI(HttpServletRequest)
     * @see JwtManager
     * 
     * @param req The header where the token of the user is so that the application
     *            can find its linked user.
     * 
     * @return The responseEntity with the found user.
     */
    @GetMapping("/whoami")
    public ResponseEntity<EUserDTO.Out.UserInformation> whoAmI(HttpServletRequest req) {
        try {
            EUser me = service.whoAmI(req);
            return ResponseEntity.ok(mapper.map(me, EUserDTO.Out.UserInformation.class));
        } catch (AuthentificationException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    /**
     * <p>
     * <b>PUT</b> request on path <b>"/change-password"</b>
     * </p>
     * <p>
     * Change the password of the user, and clear all token linked with the old
     * password.
     * </p>
     * <p>
     * Returns a new token to log the current user.
     * </p>
     * 
     * @see AuthentificationService
     * @see #AuthentificationService.clearTokensLinkedToUser(String)
     * @see JwtManager
     * 
     * @param userInformations The email, old password, and new password.
     * @param req              The header where the token of the user is so that the
     *                         application
     *                         can find its linked user.
     * @return A new token to log in.
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody EUserDTO.In.ChangePassword userInformations,
            HttpServletRequest req) {
        try {
            EUser user = service.getUserFromMailAndPassword(userInformations.getEmail(),
                    userInformations.getPassword());

            user.setPassword(encoder.encode(userInformations.getNewpassword()));
            user = u_repo.save(user);

            service.clearTokensLinkedToUser(user.getEmail());

            String token = service.login(user.getEmail(), userInformations.getNewpassword());

            return ResponseEntity.ok(token);
        } catch (AuthentificationException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody EUserDTO.In.newUser user, HttpServletRequest req) {
        EUser newuser = mapper.map(user, EUser.class);
        newuser.setPassword("\\");

        newuser = u_repo.save(newuser);

        return ResponseEntity.ok(service.getTokenForUser(newuser));
    }
}
