package m2pfe.elivret.Authentification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.EUser.EUserDTO;

@SpringBootTest(classes = Starter.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAuthentificationController {
    
    private RestTemplate rest = new RestTemplate();

    private static String BASE_URL = "http://localhost:8081/authentification/";

    private String validLogin(){
        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();

        auth.setEmail("email");
        auth.setPassword("password");

        return rest.postForEntity(BASE_URL+"/login", auth, String.class).getBody();
    }

    @Test
    public void testLogin(){
        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();

        auth.setEmail("email");
        auth.setPassword("password");

        ResponseEntity<String> response = rest.postForEntity(BASE_URL+"/login", auth, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testLoginInvalidEmail(){
        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();

        auth.setEmail("invalid");
        auth.setPassword("password");

        ResponseEntity<String> response = rest.postForEntity(BASE_URL+"/login", auth, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testLoginInvalidPassword(){
        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();

        auth.setEmail("email");
        auth.setPassword("wrong");

        ResponseEntity<String> response = rest.postForEntity(BASE_URL+"/login", auth, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testLogout(){
        String token = validLogin();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        

        ResponseEntity<String> response = rest.postForEntity(BASE_URL+"/logout", entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testLogoutInvalidToken(){
        String token = "invalid";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        
        
        ResponseEntity<String> response = rest.postForEntity(BASE_URL+"/logout", entity, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
