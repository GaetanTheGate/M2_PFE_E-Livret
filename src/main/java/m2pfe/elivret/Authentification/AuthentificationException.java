package m2pfe.elivret.Authentification;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * <p>
 * Exception for when there is an authentification failure.
 * </p>
 * <p>
 * Specify an HttpStatus for the response to send.
 * </p>
 * 
 * @see HttpStatus
 * @see HttpStatusCodeException
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class AuthentificationException extends HttpStatusCodeException {

    // TODO: COMMENTs.
    public AuthentificationException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

}
