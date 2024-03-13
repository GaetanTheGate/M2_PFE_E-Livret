package m2pfe.elivret.EAnswer;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * <p>
 * Exception for when there is an EAnswer failure.
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
public class EAnswerException extends ResponseStatusException {
    /**
     * <p>
     * Constructor of the EAnswer class.
     * </p>
     * <p>
     * Calls the super constructor
     * </p>
     *
     * @param statusCode
     * @param statusText
     */
    public EAnswerException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

}
