package m2pfe.elivret.ELivret;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * <p>
 * Exception for when there is an ESection failure.
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
public class ELivretException extends HttpStatusCodeException {

    // TODO: COMMENTS.
    public ELivretException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
    
}
