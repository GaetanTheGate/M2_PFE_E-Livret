package m2pfe.elivret.EQuestion;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * <p>
 * Exception for when there is an EQuestion failure.
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
public class EQuestionException extends ResponseStatusException {
    /**
     * <p>
     * Constructor of the EQuestion class.
     * </p>
     * <p>
     * Calls the super constructor
     * </p>
     *
     * @param statusCode
     * @param statusText
     */
    public EQuestionException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

}
