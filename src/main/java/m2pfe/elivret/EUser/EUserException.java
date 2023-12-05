package m2pfe.elivret.EUser;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * <p>
 * Exception for when there is an EUser failure.
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
public class EUserException extends HttpStatusCodeException {

    // TODO: Comment
    public EUserException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}