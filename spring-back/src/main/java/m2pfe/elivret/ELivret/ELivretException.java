package m2pfe.elivret.ELivret;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
public class ELivretException extends ResponseStatusException {
    /**
     * <p>
     * Constructor of the ELivretException class.
     * </p>
     * <p>
     * Calls the super constructor
     * </p>
     * 
     * @param statusCode
     * @param statusText
     */
    public ELivretException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

}
