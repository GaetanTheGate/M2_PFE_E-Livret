package m2pfe.elivret.Authentification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.Security.MyUserDetailsService;

/**
 * <p>
 * Token (JWT) manager.
 * </p>
 * <p>
 * Manage the tokens and the validity of them.
 * </p>
 * 
 * @see Jwts
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Service
public class JwtManager {
    /**
     * The header where to look for the token.
     */
    private static final String REQ_HEADER = "Authorization";
    /**
     * The prefix of the token to substract from the bearer.
     */ 
    private static final String TOK_PREFIX = "Bearer "; 
    
    // TODO: replace the secret key with something, maybe fetch it from a server
    /**
     * The secret key to encode the JWT.
     */
    private String secretKey = "SECRET";
    
    /**
     * The parser which to parse the JWT.
     * 
     * @see Jwts
     * @see JwtParser
     */
    private JwtParser parser;

    /**
     * Length of validity of the token in milliseconds.
     */
	private long tokenValidityLenght = 1 * 60 * 60 * 1000; // lenght * 60 * 60 * 1000 <=> lenght in hour

    /**
     * The list of all the created tokens, thus which are allowed to be validated.
     */
    private List<String> authorizedTokens = new ArrayList<>();

    /**
     * The userService to loard user.
     * 
     * @see MyUserDetailsService
     */
    @Autowired
    private MyUserDetailsService userService;

    /**
     * <p>
     * Initialization of the JwtManager
     * </p>
     * <p>
     * Initialize the parser to match the secret key.
     * </p>
     * 
     * @see Jwts
     */
    @PostConstruct
    protected void init() {
        parser = Jwts.parser()
            .setSigningKey(secretKey); // TODO: encode secret key
    }

    /**
     * <p>
     * From an EUser, create a JWT token.
     * </p>
     * <p>
     * Also add the created token to the list of valid tokens.
     * </p>
     * 
     * @see EUser
     * @see Jwts
     * @see Claims
     * 
     * @param user The user for who the token is created for.
     * @return The created token
     */
    public String createToken(EUser user) {
        Claims claims = Jwts.claims()
            .setSubject(user.getEmail());


        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityLenght);

        String token = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();


        authorizedTokens.add(token);


        return token;
    }

    /**
     * <p>
     * Forget a valid token, rendering it useless.
     * </p>
     * 
     * @see #createToken(EUser)
     * 
     * @param token The token to forget.
     * @throws AuthentificationException
     */
    public void forgetToken(String token) throws AuthentificationException {
        if(! authorizedTokens.remove(token))
            throw new AuthentificationException(HttpStatus.NO_CONTENT, "Unknown token to forget");
    }

    /**
     * <p>
     * Clean all non-valid tokens.
     * </p>
     * 
     * @see #forgetToken(String)
     */
    public void cleanAuthorizedTokens(){
        for(String token : authorizedTokens){
            if(!validateToken(token))
                forgetToken(token);
        }
    }

    /**
     * <p>
     * Check if a token is valid.
     * </p>
     * <p>
     * A token is valid if : <ul>
     * <li>It is not null</li>
     * <li>It is known from the manager</li>
     * <li>It can be parsed with the secret key</li>
     * <li>It hasn't expired</li>
     * </ul>
     * </p>
     * 
     * @see #createToken(EUser)
     * @see Jwts
     * 
     * @param token The token to process the validity. 
     * @return <ul>
     *  <li><i>true</i> if it is valid</li>
     *  <li><i>false</i> otherwise</li>
     * </ul>
     * @throws AuthentificationException
     */
    public boolean validateToken(String token) throws AuthentificationException{
        try {
            if(token == null || !authorizedTokens.contains(token))
                return false;

            parser.parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException e) {
            forgetToken(token);
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Expired token");
        }
        catch (Exception e) {
            throw new AuthentificationException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid token");
        }
    }

    /**
     * <p>
     * Process a http request to determine the token it contains. 
     * </p>
     * 
     * @see HttpServletRequest
     * 
     * @param request The http request where to search the token.
     * @return <ul>
     *  <li>The processed <i>token</i> if it was found.</li>
     *  <li><i>null</i> otherwise.</li>
     * </ul>
     * 
     * The processed token.
     */
    public String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader(REQ_HEADER);
        if (bearer != null && bearer.startsWith(TOK_PREFIX))
            return bearer.substring(TOK_PREFIX.length());

        return null;
    }

    /**
     * <p>
     * Process a token to determine the email it contains. 
     * </p>
     * 
     * @see Jwts
     * @see #validateToken(String)
     * 
     * @param token The token to process.
     * @return The email fournd.
     * @throws AuthentificationException
     */
    public String resolveEmail(String token) throws AuthentificationException {
        if(!validateToken(token))
            throw new AuthentificationException(HttpStatus.NO_CONTENT, "Unknown token to process.");

        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * <p>
     * Process an Authentication from a token.
     * </p>
     * <p>
     * The authentication contains the user's details.
     * </p>
     * 
     * @see Authentication
     * @see MyUserDetailsService
     * 
     * @param token
     * @return The created Authentication.
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(resolveEmail(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
