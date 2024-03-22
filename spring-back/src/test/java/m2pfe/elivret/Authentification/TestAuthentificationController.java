package m2pfe.elivret.Authentification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserDTO;
import m2pfe.elivret.EUser.EUserRepository;
import m2pfe.elivret.UtilityForTest.RestErrorHandlerNoThrows;

@SpringBootTest(classes = Starter.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAuthentificationController {
    @Autowired
    public EUserRepository u_repo;

    @Autowired
    public PasswordEncoder encoder;

    private RestTemplate rest = new RestTemplate();
    private static String BASE_URL = "http://localhost:8081/authentification/";

    private String email;
    private String password;

    @PostConstruct
    public void init() {
        rest = new RestTemplate();
        rest.setErrorHandler(new RestErrorHandlerNoThrows());

        initUser();
    }

    private void initUser() {
        email = "email" + this.hashCode();
        password = "password" + this.hashCode();

        u_repo.save(EUser.builder().email(email).password(encoder.encode(password)).firstName("TEST").lastName("TEST")
                .build());
    }

    private String validLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();
        auth.setEmail(email);
        auth.setPassword(password);

        ResponseEntity<String> r_login = rest.exchange(BASE_URL + "/login", HttpMethod.POST,
                new HttpEntity<>(auth, headers), String.class);

        return r_login.getBody();
    }

    @Test
    public void testLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();
        auth.setEmail(email);
        auth.setPassword(password);

        ResponseEntity<String> r_login = rest.exchange(BASE_URL + "/login", HttpMethod.POST,
                new HttpEntity<>(auth, headers), String.class);

        assertEquals(HttpStatus.OK, r_login.getStatusCode());
        assertNotNull(r_login.getBody());
    }

    @Test
    public void testLoginInvalidEmail() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();
        auth.setEmail("invalid");
        auth.setPassword(password);

        ResponseEntity<String> r_login = rest.exchange(BASE_URL + "/login", HttpMethod.POST,
                new HttpEntity<>(auth, headers), String.class);

        assertEquals(HttpStatus.FORBIDDEN, r_login.getStatusCode());
    }

    @Test
    public void testLoginInvalidPassword() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();
        auth.setEmail(email);
        auth.setPassword("invalid");

        ResponseEntity<String> r_login = rest.exchange(BASE_URL + "/login", HttpMethod.POST,
                new HttpEntity<>(auth, headers), String.class);

        assertEquals(HttpStatus.FORBIDDEN, r_login.getStatusCode());
    }

    @Test
    public void testWhoAmI() {
        String token = validLogin();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<EUserDTO.Out.UserInformation> r_whoami = rest.exchange(BASE_URL + "/whoami", HttpMethod.GET,
                new HttpEntity<>(headers), EUserDTO.Out.UserInformation.class);

        assertEquals(HttpStatus.OK, r_whoami.getStatusCode());
        assertNotNull(r_whoami.getBody());
    }

    @Test
    public void testWhoAmIInvalidToken() {
        String token = "token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<EUserDTO.Out.UserInformation> r_whoami = rest.exchange(BASE_URL + "/whoami", HttpMethod.GET,
                new HttpEntity<>(headers), EUserDTO.Out.UserInformation.class);

        assertEquals(HttpStatus.NO_CONTENT, r_whoami.getStatusCode());
    }

    @Test
    public void testLogout() {
        String token = validLogin();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<String> r_logout = rest.exchange(BASE_URL + "/logout", HttpMethod.POST,
                new HttpEntity<>(headers), String.class);

        ResponseEntity<EUserDTO.Out.UserInformation> r_whoami = rest.exchange(BASE_URL + "/whoami", HttpMethod.GET,
                new HttpEntity<>(headers), EUserDTO.Out.UserInformation.class);

        assertEquals(HttpStatus.OK, r_logout.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, r_whoami.getStatusCode());
    }

    @Test
    public void testLogoutInvalidToken() {
        String token = "invalid";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<String> r_logout = rest.exchange(BASE_URL + "/logout", HttpMethod.POST,
                new HttpEntity<>(headers), String.class);

        // TODO : WhoAmi

        assertEquals(HttpStatus.NO_CONTENT, r_logout.getStatusCode());
    }
}
