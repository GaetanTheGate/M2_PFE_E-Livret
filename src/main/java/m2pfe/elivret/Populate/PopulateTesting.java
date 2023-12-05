package m2pfe.elivret.Populate;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.ELivret.ELivretRepository;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.ESection.ESectionRepository;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserRepository;

@Profile("testing")
@Configuration
public class PopulateTesting {
    @Autowired
    private EUserRepository ur;

    @Autowired
    private ELivretRepository lr;

    @Autowired
    private ESectionRepository sr;

    @Autowired
    private PasswordEncoder encoder;
    
    @PostConstruct
    public void initTest(){
        System.out.println("-----------------");
        System.out.println("----INIT-TEST----");
        System.out.println("-----------------");

        createUsers();
        createELivrets();
    }

    private void createUsers(){
        createAndSaveUser("email", "password");

        createAndSaveUser("responsable@mail.com", "responsable");

    }

    private EUser createAndSaveUser(String mail, String password) {
        EUser user = new EUser();
        user.setEmail(mail);
        user.setPassword(encoder.encode(password));
        return ur.save(user);
    }

    private void createELivrets(){

        EUser student = createAndSaveUser("etudiant@mail.com", "etudiant");
        EUser tutor = createAndSaveUser("tuteur@mail.com", "tuteur");
        EUser master = createAndSaveUser("maitre@mail.com", "maitre");

        ELivret livret = createAndSaveElivret(student, tutor, master);

        createAndSaveEsESection(livret, UserRole.STUDENT, "student Visible", true);
        createAndSaveEsESection(livret, UserRole.STUDENT, "student Invisible", false);
        createAndSaveEsESection(livret, UserRole.TUTOR, "tutor Visible", true);
        createAndSaveEsESection(livret, UserRole.TUTOR, "tutor Invisible", false);
        createAndSaveEsESection(livret, UserRole.MASTER, "master Visible", true);
        createAndSaveEsESection(livret, UserRole.MASTER, "master Invisible", false);
    }

    private ELivret createAndSaveElivret(EUser student, EUser tutor, EUser master){
        ELivret livret = new ELivret(0, student, tutor, master, null);

        return lr.save(livret);
    }

    private ESection createAndSaveEsESection(ELivret livret, UserRole owner, String title, boolean visibility){
        List<String> questions = List.of("q1", "q2");
        List<String> answers = List.of("a1", "a2");
        
        ESection section = new ESection(0, owner, visibility, title, questions, answers, livret);

        return sr.save(section);
    }
}
