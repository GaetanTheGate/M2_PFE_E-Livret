package m2pfe.elivret.Authentification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import m2pfe.elivret.Starter;

@SpringBootTest(classes = Starter.class)
public class TestAuthentificationService {
    @Autowired
    private AuthentificationService service;

    @Test
    public String testLogin(){
        String token = service.login("email", "password");

        assertNotNull(token);

        return token;
    }

    @Test
    public void testLoginInvalidEmail(){
        assertThrows(AuthentificationException.class, () -> service.login("invalid", "password"));
    }

    @Test
    public void testLoginWrongPassword(){
        assertThrows(AuthentificationException.class, () -> service.login("email", "wrong"));
    }

    @Test
    public void testLogout(){
        String token = testLogin();

        // TODO: quand sera possible, ajouter une opération demandant un token

        service.logout(token);

        // TODO: quand sera possible, ajouter une opération demandant un token et vérifier que ca throw 
    }

    @Test
    public void testLogoutInexistantToken(){
        String token = "inextistant";

        assertThrows(AuthentificationException.class, () -> service.logout(token));
    }


}
