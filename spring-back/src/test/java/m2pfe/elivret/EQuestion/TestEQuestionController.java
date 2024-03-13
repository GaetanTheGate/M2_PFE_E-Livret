package m2pfe.elivret.EQuestion;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.EQuestion.EQuestionBuilder;
import m2pfe.elivret.EQuestion.EQuestion.QuestionType;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUser.Permission;
import m2pfe.elivret.Populate.EntityManager;
import m2pfe.elivret.UtilityForTest.RestErrorHandlerNoThrows;

@SpringBootTest(classes = Starter.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestEQuestionController {

        private RestTemplate rest;
        private static String BASE_URL = "http://localhost:8081/api/questions/";

        @Autowired
        private EntityManager manager;

        @Autowired
        private AuthentificationService auth_service;

        private EUser student;
        private EUser master;
        private EUser tutor;
        private EUser responsable;
        private EUser someone;

        private String token_student;
        private String token_master;
        private String token_tutor;
        private String token_responsable;
        private String token_someone;

        @PostConstruct
        public void init() {
                rest = new RestTemplate();
                rest.setErrorHandler(new RestErrorHandlerNoThrows());

                setupUsers();
                setupTokens();
        }

        private void setupUsers() {
                student = manager.saveUser(EUser.builder()
                                .email("question_student@mail.com" + this.hashCode())
                                .password("question_student")
                                .firstName("question_student_FIRST")
                                .lastName("question_student_LAST")
                                .permission(Permission.USER)
                                .build());

                master = manager.saveUser(EUser.builder()
                                .email("question_master@mail.com" + this.hashCode())
                                .password("question_master")
                                .firstName("question_master_FIRST")
                                .lastName("question_master_LAST")
                                .permission(Permission.USER)
                                .build());

                tutor = manager.saveUser(EUser.builder()
                                .email("question_tutor@mail.com" + this.hashCode())
                                .password("question_tutor")
                                .firstName("question_tutor_FIRST")
                                .lastName("question_tutor_LAST")
                                .permission(Permission.USER)
                                .build());

                responsable = manager.saveUser(EUser.builder()
                                .email("question_responsable@mail.com" + this.hashCode())
                                .password("question_responsable")
                                .firstName("question_responsable_FIRST")
                                .lastName("question_responsable_LAST")
                                .permission(Permission.RESPONSABLE)
                                .build());

                someone = manager.saveUser(EUser.builder()
                                .email("question_someone@mail.com" + this.hashCode())
                                .password("question_someone")
                                .firstName("question_someone_FIRST")
                                .lastName("question_someone_LAST")
                                .permission(Permission.USER)
                                .build());
        }

        private void setupTokens() {
                token_student = auth_service.getTokenForUser(student);
                token_master = auth_service.getTokenForUser(master);
                token_tutor = auth_service.getTokenForUser(tutor);
                token_responsable = auth_service.getTokenForUser(responsable);
                token_someone = auth_service.getTokenForUser(someone);
        }

        private EQuestionBuilder buildQuestionBase(String title, UserRole owner, Boolean visibility,
                        QuestionType type) {
                ELivret livret = ELivret.builder()
                                .name(title)
                                .student(student)
                                .master(master)
                                .tutor(tutor)
                                .responsable(responsable)
                                .build();

                livret = manager.saveLivret(livret);

                ESection section = ESection.builder()
                                .title(title)
                                .owner(owner)
                                .visibility(visibility)
                                .livret(livret)
                                .build();

                section = manager.saveSection(section);

                return EQuestion.builder()
                                .title(title)
                                .type(type)
                                .section(section);
        }

        /// GET

        @Test
        public void getFromIdTest() {

        }

        @Test
        public void getFromIdNonExistentTest() {

        }

        @Test
        public void getMineToCompleteTest() {

        }

        /// POST

        // Rien car pas de méthode de création de question

        /// PUT

        // Rien car pas de méthode de modification de question

        /// DELETE

        @Test
        public void deleteQuestionFromId() {

        }

        @Test
        public void deleteQuestionFromIdNonExistent() {

        }
}
