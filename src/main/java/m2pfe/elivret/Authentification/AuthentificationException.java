package m2pfe.elivret.Authentification;

import org.springframework.http.HttpStatus;

/**
 * <p>
 * Exception for when there is an authentification failure.
 * </p>
 * <p>
 * Specify an HttpStatus for the response to send.
 * </p>
 * 
 * @see HttpStatus
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class AuthentificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * HttpStatus of the error.
     * 
     * @see HttpStatus
     */
    private final HttpStatus httpStatus;

    /**
     * <p>
     * Constructor of the AuthentificationException.
     * </p>
     * 
     * @see HttpStatus
     * 
     * @param message The message of the error.
     * @param httpStatus The httpStatus of the error.
     */
    public AuthentificationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    /**
     * <p>
     * Get the HttpStatus of the error.
     * </p>
     * 
     * @see HttpStatus
     * 
     * @return the HttpStatus.
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
}
