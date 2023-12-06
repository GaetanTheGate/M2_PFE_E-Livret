package m2pfe.elivret.Populate;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EAnswer.EAnswerRepository;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.AbstractEQuestion;
import m2pfe.elivret.EQuestion.EQestionRepository;
import m2pfe.elivret.EQuestion.AbstractEQuestion.QuestionType;
import m2pfe.elivret.EQuestion.QuestionType.NoChoiceQuestion;
import m2pfe.elivret.EQuestion.QuestionType.MultipleChoiceQuestion;
import m2pfe.elivret.EQuestion.QuestionType.SingleChoiceQuestion;
import m2pfe.elivret.ELivret.ELivretRepository;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.ESection.ESectionRepository;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserRepository;

/**
 * <p>
 * Service used to populate the repositories for <b>testing purpose only</b>.
 * </p>
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
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
    private EQestionRepository qr;

    @Autowired
    private EAnswerRepository ar;

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
        createAndSaveUser("email", "password" , UserRole.RESPONSABLE);

        createAndSaveUser("responsable@mail.com", "responsable" , UserRole.RESPONSABLE);

    }

    private EUser createAndSaveUser(String mail, String password, ELivret.UserRole role) {
        EUser user = new EUser();
        user.setEmail(mail);
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        return ur.save(user);
    }

    private void createELivrets(){

        EUser student = createAndSaveUser("etudiant@mail.com", "etudiant", UserRole.STUDENT);
        EUser tutor = createAndSaveUser("tuteur@mail.com", "tuteur", UserRole.TUTOR);
        EUser master = createAndSaveUser("maitre@mail.com", "maitre", UserRole.MASTER);
        EUser responsable = createAndSaveUser("responsable@mail.com", "responsable", UserRole.RESPONSABLE);

        createAndSaveElivret(student, tutor, master, responsable);
    }

    private ELivret createAndSaveElivret(EUser student, EUser tutor, EUser master , EUser responsable){
        ELivret livret = new ELivret(0, student, tutor, master, responsable, null);

        livret = lr.save(livret);

        createAndSaveESection(livret, UserRole.STUDENT, "student Visible", true);
        createAndSaveESection(livret, UserRole.STUDENT, "student Invisible", false);
        createAndSaveESection(livret, UserRole.TUTOR, "tutor Visible", true);
        createAndSaveESection(livret, UserRole.TUTOR, "tutor Invisible", false);
        createAndSaveESection(livret, UserRole.MASTER, "master Visible", true);
        createAndSaveESection(livret, UserRole.MASTER, "master Invisible", false);

        return livret;
    }

    private ESection createAndSaveESection(ELivret livret, UserRole owner, String title, boolean visibility){
        ESection section = new ESection(0, owner, visibility, title, livret, null);

        section = sr.save(section);

        createAndSaveEQuestion(section, "Texte a caractere informatif :", QuestionType.LABEL);
        createAndSaveEQuestion(section, "Question 1 :", QuestionType.TEXT);
        createAndSaveEQuestion(section, "Question 2 :", QuestionType.SINGLE_CHECKBOX);
        createAndSaveEQuestion(section, "Question 3 :", QuestionType.MULTI_CHECKBOX);
        createAndSaveEQuestion(section, "Question 4 :", QuestionType.RATIOBUTTON);

        return section;
    }

    private AbstractEQuestion createAndSaveEQuestion(ESection section, String title, QuestionType type){
        AbstractEQuestion question;
        
        switch (type) {
            case LABEL:
                question = labelQuestion();
                break;
            case TEXT:
            case SINGLE_CHECKBOX:
                question = singleQuestion("REPONSE : " + title);
                break;
            case MULTI_CHECKBOX:
            case RATIOBUTTON:
                question = multipleQuestion(List.of("Valeur 1", "Valeur 2", "Valeur 3"), List.of("Reponse 1 : " + title, "Reponse 2 : " + title, "Reponse 3 : " + title));
                break;
            default:
                question = labelQuestion();
                break;
        }

        question.setId(0);
        question.setSection(section);
        question.setTitle(title);
        question.setType(type);

        question = qr.save(question);

        switch (type) {
            case TEXT:
            case SINGLE_CHECKBOX:
                createAndSaveAnswer("REPONSE : " + title, question);
                break;
            case MULTI_CHECKBOX:
            case RATIOBUTTON:
                createAndSaveAnswer("REPONSE 3 : " + title, question);
                createAndSaveAnswer("REPONSE 2 : " + title, question);
                createAndSaveAnswer("REPONSE 1 : " + title, question);
                break;
            case LABEL:
            default:
                break;
        }

        return question;
    }

    private EAnswer createAndSaveAnswer(String value, AbstractEQuestion question) {
        return ar.save(new EAnswer(0, value, question));
    }

    private SingleChoiceQuestion singleQuestion(String anwser){
        return new SingleChoiceQuestion();
    }

    private MultipleChoiceQuestion multipleQuestion(List<String> values, List<String> answers){
        // List<EAnswer> as = answers.stream().map(answer -> createAndSaveAnswer(answer)).toList();
        return new MultipleChoiceQuestion();
    }

    private NoChoiceQuestion labelQuestion(){
        return new NoChoiceQuestion();
    }
}
