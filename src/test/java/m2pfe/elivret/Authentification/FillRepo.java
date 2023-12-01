package m2pfe.elivret.Authentification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserRepository;

@Profile("testing")
@Configuration
public class FillRepo {
    @Autowired
    private EUserRepository ur;
    
    @PostConstruct
    public void initTest(){
        System.out.println("-----------------");
        System.out.println("----INIT-TEST----");
        System.out.println("-----------------");
        createUsers();
    }

    private void createUsers(){
        createAndSaveUser("email", "password");
    }

    private EUser createAndSaveUser(String mail, String password) {
        EUser user = new EUser();
        user.setEmail(mail);
        user.setPassword(password);
        return ur.save(user);
    }
}
