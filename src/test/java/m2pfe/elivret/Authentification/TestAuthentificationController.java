package m2pfe.elivret.Authentification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.EUser.EUserDTO;

@SpringBootTest(classes = Starter.class)
public class TestAuthentificationController {
    
    private RestTemplate rest = new RestTemplate();

    private static String BASE_URL = "http://localhost:8081/authentification/";

    @Test
    public void testLogin(){
        EUserDTO.In.AuthentificationInformation auth = new EUserDTO.In.AuthentificationInformation();

        auth.setEmail("email");
        auth.setPassword("password");

        ResponseEntity<String> response = rest.postForEntity(BASE_URL+"/login", auth, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(null, response.getBody());
    }
    
}
