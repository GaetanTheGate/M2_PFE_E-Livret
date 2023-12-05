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

    // TODO : Comments
    public ESectionException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
    
}
