package m2pfe.elivret.EQuestion;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * <p>
 * Exception for when there is an AbstractEQuestion failure.
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
public class EQuestionException extends HttpStatusCodeException {
    /**
     * <p>
     * Constructor of the AbstractEQuestion class.
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
