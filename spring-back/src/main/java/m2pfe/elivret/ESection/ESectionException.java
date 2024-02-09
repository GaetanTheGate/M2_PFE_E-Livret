package m2pfe.elivret.ESection;

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
public class ESectionException extends HttpStatusCodeException {
    /**
     * <p>
     * Constructor of the ESectionException class.
     * </p>
     * <p>
     * Calls the super constructor
     * </p>
     * 
     * @param statusCode
     * @param statusText
     */
    public ESectionException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

}
