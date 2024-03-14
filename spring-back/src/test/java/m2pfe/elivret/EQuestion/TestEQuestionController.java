package m2pfe.elivret.EQuestion;

import javax.annotation.PostConstruct;

import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.ESection.ESectionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
                // Given
                EQuestion q = buildQuestionBase("getFromIdTest",UserRole.MASTER,true,QuestionType.TEXT).build();
                q = manager.saveQuestion(q);

                String PATH_URL = q.getId().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_master);
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_master = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(q.getId(), r_student.getBody().getId());

                assertEquals(HttpStatus.OK, r_master.getStatusCode());
                assertEquals(q.getId(), r_master.getBody().getId());

                assertEquals(HttpStatus.OK, r_tutor.getStatusCode());
                assertEquals(q.getId(), r_tutor.getBody().getId());

                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());
                assertEquals(q.getId(), r_responsable.getBody().getId());

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
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
                assertNull(r_student.getBody());
        }

        @Test
        public void getMineToCompleteTest() {
                // Given
                EQuestion q1 = buildQuestionBase("getMineToCompleteTest1",UserRole.STUDENT,true,QuestionType.TEXT)
                        .build();
                EQuestion q2 = buildQuestionBase("getMineToCompleteTest2",UserRole.STUDENT,true,QuestionType.TEXT)
                        .answers(List.of(EAnswer.builder()
                                .build()))

                        .build();
                EQuestion q3 = buildQuestionBase("getMineToCompleteTest3",UserRole.STUDENT,false,QuestionType.TEXT)
                        .answers(List.of(EAnswer.builder()
                                .build()))

                        .build();

                q1 = manager.saveQuestion(q1);
                q2 = manager.saveQuestion(q2);
                q3 = manager.saveQuestion(q3);

                String PATH_URL = "/mine/tocomplete";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<List> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), List.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(1, r_student.getBody().size());

        }

        /// POST

        // Rien car pas de méthode de création de question

        /// PUT

        // Rien car pas de méthode de modification de question

        /// DELETE

        @Test
        public void deleteQuestionFromId() {
                //Given
                EQuestion q = buildQuestionBase("deleteQuestionFromId",UserRole.STUDENT,true, QuestionType.TEXT).build();

                q = manager.saveQuestion(q);

                String PATH_URL = q.getId().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");
                // When
                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_responsable_get1 = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

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

                ResponseEntity<EQuestionDTO.Out.AllPublic> r_responsable_get2 = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.OK, r_responsable_get1.getStatusCode());
                assertEquals(q.getId(), r_responsable_get1.getBody().getId());

                assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());


                assertEquals(HttpStatus.NO_CONTENT, r_responsable_get2.getStatusCode());
        }

        @Test
        public void deleteQuestionFromIdNonExistent() {
                // Given
                String PATH_URL = "0";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<EQuestionDTO.Out.AllPublic> r_get = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                        new HttpEntity<>(headers), EQuestionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_get.getStatusCode());
        }
}
