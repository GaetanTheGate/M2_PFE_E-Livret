package m2pfe.elivret.EAnswer;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EAnswer.EAnswer.EAnswerBuilder;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.EQuestion;
import m2pfe.elivret.EQuestion.EQuestion.QuestionType;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUser.Permission;
import m2pfe.elivret.Populate.EntityManager;
import m2pfe.elivret.UtilityForTest.RestErrorHandlerNoThrows;

@SpringBootTest(classes = Starter.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestEAnswerController {

        private RestTemplate rest;
        private static String BASE_URL = "http://localhost:8081/api/answers/";

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
                                .email("answer_student@mail.com" + this.hashCode())
                                .password("answer_student")
                                .firstName("answer_student_FIRST")
                                .lastName("answer_student_LAST")
                                .permission(Permission.USER)
                                .build());

                master = manager.saveUser(EUser.builder()
                                .email("answer_master@mail.com" + this.hashCode())
                                .password("answer_master")
                                .firstName("answer_master_FIRST")
                                .lastName("answer_master_LAST")
                                .permission(Permission.USER)
                                .build());

                tutor = manager.saveUser(EUser.builder()
                                .email("answer_tutor@mail.com" + this.hashCode())
                                .password("answer_tutor")
                                .firstName("answer_tutor_FIRST")
                                .lastName("answer_tutor_LAST")
                                .permission(Permission.USER)
                                .build());

                responsable = manager.saveUser(EUser.builder()
                                .email("answer_responsable@mail.com" + this.hashCode())
                                .password("answer_responsable")
                                .firstName("answer_responsable_FIRST")
                                .lastName("answer_responsable_LAST")
                                .permission(Permission.RESPONSABLE)
                                .build());

                someone = manager.saveUser(EUser.builder()
                                .email("answer_someone@mail.com" + this.hashCode())
                                .password("answer_someone")
                                .firstName("answer_someone_FIRST")
                                .lastName("answer_someone_LAST")
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

        private EAnswerBuilder buildAnswerBase(String title, String value, UserRole owner, Boolean visibility,
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

                EQuestion question = EQuestion.builder()
                                .title(title)
                                .type(type)
                                .section(section)
                                .build();

                question = manager.saveQuestion(question);

                return EAnswer.builder()
                                .proposition(title)
                                .value(value)
                                .question(question);
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

        // Rien car pas de méthode de création de réponse

        /// PUT
        @Test
        public void putPropositionTest() {

        }

        @Test
        public void putPropositionInNonExistentAnswerTest() {

        }

        @Test
        public void putValueTest() {

        }

        @Test
        public void putValueInNonExistentAnswerTest() {

        }

        /// DELETE
        @Test
        public void deleteAnswerFromId() {

        }

        @Test
        public void deleteAnswerFromIdNonExistent() {

        }
}
