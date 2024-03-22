package m2pfe.elivret.EAnswer;

import javax.annotation.PostConstruct;

import m2pfe.elivret.EQuestion.EQuestionDTO;
import m2pfe.elivret.ESection.ESectionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
                // Given
                EAnswer a = buildAnswerBase("getFromIdTest", "test", UserRole.MASTER, true, QuestionType.TEXT).build();
                a = manager.saveAnswer(a);

                String PATH_URL = a.getId().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_master);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_master = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(a.getId(), r_student.getBody().getId());

                assertEquals(HttpStatus.OK, r_master.getStatusCode());
                assertEquals(a.getId(), r_master.getBody().getId());

                assertEquals(HttpStatus.OK, r_tutor.getStatusCode());
                assertEquals(a.getId(), r_tutor.getBody().getId());

                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());
                assertEquals(a.getId(), r_responsable.getBody().getId());

                assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
                assertNull(r_someone.getBody());
        }

        @Test
        public void getFromIdNonExistentTest() {
                // Given
                String PATH_URL = "0";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
                assertNull(r_student.getBody());
        }

        @Test
        public void getMineToCompleteTest() {
                // Given
                EAnswer a1 = buildAnswerBase("getMineToCompleteTest1", "test", UserRole.STUDENT, true,
                                QuestionType.TEXT).build();
                EAnswer a2 = buildAnswerBase("getMineToCompleteTest2", "", UserRole.STUDENT, false, QuestionType.TEXT)
                                .build();
                EAnswer a3 = buildAnswerBase("getMineToCompleteTest3", "", UserRole.STUDENT, true, QuestionType.TEXT)
                                .build();
                EAnswer a4 = buildAnswerBase("getMineToCompleteTest4", null, UserRole.STUDENT, true, QuestionType.TEXT)
                                .build();

                a1 = manager.saveAnswer(a1);
                a2 = manager.saveAnswer(a2);
                a3 = manager.saveAnswer(a3);
                a4 = manager.saveAnswer(a4);

                String PATH_URL = "/mine/tocomplete";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<List> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), List.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(2, r_student.getBody().size());
        }

        /// POST

        // Rien car pas de méthode de création de réponse

        /// PUT
        @Test
        public void putPropositionTest() {
                // Given
                EAnswer a = buildAnswerBase("putPropositionTest", "test", UserRole.STUDENT, false, QuestionType.TEXT)
                                .build();

                a = manager.saveAnswer(a);

                String PATH_URL = "/saveProposition";

                EAnswerDTO.In.Proposition answer = new EAnswerDTO.In.Proposition();
                answer.setId(a.getId());
                answer.setProposition("newProposition");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_tutor.getStatusCode());
                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());

                assertEquals("newProposition", r_responsable.getBody().getProposition());
        }

        @Test
        public void putPropositionInNonExistentAnswerTest() {
                // Given
                String PATH_URL = "/saveProposition";

                EAnswerDTO.In.Proposition answer = new EAnswerDTO.In.Proposition();
                answer.setId(0);
                answer.setProposition("newProposition");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_someone.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_tutor.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_responsable.getStatusCode());
        }

        @Test
        public void putValueTest() {
                // Given
                EAnswer a = buildAnswerBase("putValueTest", "test", UserRole.STUDENT, false, QuestionType.TEXT).build();

                a = manager.saveAnswer(a);

                String PATH_URL = "/saveValue";

                EAnswerDTO.In.Value answer = new EAnswerDTO.In.Value();
                answer.setId(a.getId());
                answer.setValue("newValue");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_tutor.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_responsable.getStatusCode());

                assertEquals("newValue", r_student.getBody().getValue());
        }

        @Test
        public void putValueInNonExistentAnswerTest() {
                // Given
                String PATH_URL = "/saveValue";

                EAnswerDTO.In.Value answer = new EAnswerDTO.In.Value();
                answer.setId(0);
                answer.setValue("newValue");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.PUT,
                                new HttpEntity<>(answer, headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_someone.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_tutor.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_responsable.getStatusCode());

        }

        /// DELETE
        @Test
        public void deleteAnswerFromId() {
                // Given
                EAnswer a = buildAnswerBase("deleteAnswerFromId", "test", UserRole.STUDENT, true, QuestionType.TEXT)
                                .build();

                a = manager.saveAnswer(a);

                String PATH_URL = a.getId().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");
                // When
                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_responsable_get1 = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                System.out.println("SUPPRESION 1");
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<Void> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.DELETE,
                                new HttpEntity<>(headers), Void.class);

                System.out.println("SUPPRESION 2");
                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<Void> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.DELETE,
                                new HttpEntity<>(headers), Void.class);

                System.out.println("SUPPRESION 3");
                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<Void> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.DELETE,
                                new HttpEntity<>(headers), Void.class);

                ResponseEntity<EAnswerDTO.Out.AllPublic> r_responsable_get2 = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.OK, r_responsable_get1.getStatusCode());
                assertEquals(a.getId(), r_responsable_get1.getBody().getId());

                assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());

                assertEquals(HttpStatus.NO_CONTENT, r_responsable_get2.getStatusCode());
        }

        @Test
        public void deleteAnswerFromIdNonExistent() {
                // Given
                String PATH_URL = "0";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EAnswerDTO.Out.AllPublic> r_get = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), EAnswerDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_get.getStatusCode());
        }
}
