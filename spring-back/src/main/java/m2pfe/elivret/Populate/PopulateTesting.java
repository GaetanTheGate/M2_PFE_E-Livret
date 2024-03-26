package m2pfe.elivret.Populate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EAnswer.EAnswerRepository;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EModel.ELivretModel;
import m2pfe.elivret.EQuestion.EQuestion;
import m2pfe.elivret.EQuestion.EQestionRepository;
import m2pfe.elivret.EQuestion.EQuestion.QuestionType;
import m2pfe.elivret.EQuestion.QuestionType.NoChoiceQuestion;
import m2pfe.elivret.EQuestion.QuestionType.MultipleChoiceQuestion;
import m2pfe.elivret.EQuestion.QuestionType.SingleChoiceQuestion;
import m2pfe.elivret.ELivret.ELivretRepository;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.ESection.ESectionRepository;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserRepository;
import m2pfe.elivret.EUser.EUser.Permission;
import m2pfe.elivret.JSONConverter.JSONAndELivretModelConverter;

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
@SuppressWarnings("all")
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

        @Autowired
        private JSONAndELivretModelConverter converter;

        @Autowired
        private EntityManager manager;

        private ModelMapper mapper = new ModelMapper();

        @PostConstruct
        public void initTest() throws IOException {
                System.out.println("-----------------");
                System.out.println("----INIT-TEST----");
                System.out.println("-----------------");

                createUsers();
                createELivrets();

                createExampleLivret();
        }

        private void createUsers() {
                createAndSaveUser("email", "password", Permission.RESPONSABLE, "John", "Respo");
        }

        private EUser createAndSaveUser(String mail, String password, Permission Permission, String firstName,
                        String lastName) {
                EUser user = new EUser();
                user.setEmail(mail);
                user.setPassword(encoder.encode(password));
                user.setPermission(Permission);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                return ur.save(user);
        }

        private void createELivrets() {

                EUser student = createAndSaveUser("etudiant@mail.com", "etudiant", Permission.USER, "Antoine",
                                "Dulac");
                EUser tutor = createAndSaveUser("tuteur@mail.com", "tuteur", Permission.USER, "Vincent", "Lacroix");
                EUser master = createAndSaveUser("maitre@mail.com", "maitre", Permission.USER, "Sylvie", "Roidurelle");
                EUser responsable = createAndSaveUser("responsable@mail.com", "responsable", Permission.RESPONSABLE,
                                "Jaspar",
                                "Raux");

                ELivret livret = createAndSaveElivret(student, tutor, master, responsable);
        }

        private ELivret createAndSaveElivret(EUser student, EUser tutor, EUser master, EUser responsable) {
                ELivret livret = new ELivret(0, "Livret_test", student, tutor, master, responsable, null);

                livret = lr.save(livret);

                createAndSaveESection(livret, UserRole.STUDENT, "student Visible", true);
                createAndSaveESection(livret, UserRole.STUDENT, "student Invisible", false);
                createAndSaveESection(livret, UserRole.TUTOR, "tutor Visible", true);
                createAndSaveESection(livret, UserRole.TUTOR, "tutor Invisible", false);
                createAndSaveESection(livret, UserRole.MASTER, "master Visible", true);
                createAndSaveESection(livret, UserRole.MASTER, "master Invisible", false);

                return lr.findById(livret.getId()).get();
        }

        private ESection createAndSaveESection(ELivret livret, UserRole owner, String title, boolean visibility) {
                ESection section = new ESection(0, null, owner, visibility, title, livret, null);

                section = sr.save(section);

                createAndSaveEQuestion(section, "Texte a caractere informatif :", QuestionType.LABEL);
                createAndSaveEQuestion(section, "Question 1 :", QuestionType.TEXT);
                createAndSaveEQuestion(section, "Question 2 :", QuestionType.CHECKBOX);
                createAndSaveEQuestion(section, "Question 3 :", QuestionType.RADIO);

                return sr.findById(section.getId()).get();
        }

        private EQuestion createAndSaveEQuestion(ESection section, String title, QuestionType type) {
                EQuestion question = new EQuestion();

                question.setId(0);
                question.setSection(section);
                question.setTitle(title);
                question.setType(type);

                question = qr.save(question);

                switch (type) {
                        case TEXT:
                                createAndSaveAnswer("REPONSE", "valeur", question);
                                break;
                        case CHECKBOX:
                        case RADIO:
                                createAndSaveAnswer("REPONSE 1", "false", question);
                                createAndSaveAnswer("REPONSE 2", "true", question);
                                createAndSaveAnswer("REPONSE 3", "false", question);
                                break;
                        case LABEL:
                        default:
                                break;
                }

                return qr.findById(question.getId()).get();
        }

        private EAnswer createAndSaveAnswer(String proposition, String value, EQuestion question) {
                return ar.save(new EAnswer(0, proposition, null, question));
        }

        private void createExampleLivret() throws IOException {
                String livret_name = "Livret d'apprentissage";

                EUser student = createAndSaveUser("etudiant2@mail.com", "etudiant2", Permission.USER, "Guillaume",
                                "Dulac");

                EUser responsable = ur.findByEmail("responsable@mail.com").get();
                EUser tutor = ur.findByEmail("tuteur@mail.com").get();
                EUser master = ur.findByEmail("maitre@mail.com").get();

                String jsonFile = "src/main/resources/dev_livret_models/example_livret.json";

                FileInputStream fis = new FileInputStream(jsonFile);
                byte[] buffer = new byte[10];
                StringBuilder sb = new StringBuilder();
                while (fis.read(buffer) != -1) {
                        sb.append(new String(buffer));
                        buffer = new byte[10];
                }
                fis.close();

                String content = sb.toString();

                String json = content;
                ELivretModel model = converter.JSONStringToELivretModel(json);
                ELivret livret = mapper.map(model, ELivret.class);

                livret.setName(livret_name);
                livret.setStudent(student);
                livret.setMaster(master);
                livret.setTutor(tutor);
                livret.setResponsable(responsable);

                livret = manager.saveLivret(livret);

        }
}
