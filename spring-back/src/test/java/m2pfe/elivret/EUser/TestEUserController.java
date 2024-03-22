package m2pfe.elivret.EUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EAnswer.EAnswerDTO;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.EQuestion.QuestionType;
import m2pfe.elivret.EUser.EUser.Permission;
import m2pfe.elivret.Populate.EntityManager;
import m2pfe.elivret.UtilityForTest.RestErrorHandlerNoThrows;

@SpringBootTest(classes = Starter.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestEUserController {

        private RestTemplate rest;
        private static String BASE_URL = "http://localhost:8081/api/users/";

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
                                .email("user_student@mail.com" + this.hashCode())
                                .password("user_student")
                                .firstName("user_student_FIRST")
                                .lastName("user_student_LAST")
                                .permission(Permission.USER)
                                .build());

                master = manager.saveUser(EUser.builder()
                                .email("user_master@mail.com" + this.hashCode())
                                .password("user_master")
                                .firstName("user_master_FIRST")
                                .lastName("user_master_LAST")
                                .permission(Permission.USER)
                                .build());

                tutor = manager.saveUser(EUser.builder()
                                .email("user_tutor@mail.com" + this.hashCode())
                                .password("user_tutor")
                                .firstName("user_tutor_FIRST")
                                .lastName("user_tutor_LAST")
                                .permission(Permission.USER)
                                .build());

                responsable = manager.saveUser(EUser.builder()
                                .email("user_responsable@mail.com" + this.hashCode())
                                .password("user_responsable")
                                .firstName("user_responsable_FIRST")
                                .lastName("user_responsable_LAST")
                                .permission(Permission.RESPONSABLE)
                                .build());

                someone = manager.saveUser(EUser.builder()
                                .email("user_someone@mail.com" + this.hashCode())
                                .password("user_someone")
                                .firstName("user_someone_FIRST")
                                .lastName("user_someone_LAST")
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

        @Test
        public void getByIdTest() {
                // Given
                String PATH_URL = student.getId().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EUserDTO.Out.UserInformation> r_student = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), EUserDTO.Out.UserInformation.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<EUserDTO.Out.UserInformation> r_someone = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), EUserDTO.Out.UserInformation.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(student.getId(), r_student.getBody().getId());

                assertEquals(HttpStatus.OK, r_someone.getStatusCode());
                assertEquals(student.getId(), r_student.getBody().getId());
        }

        @Test
        public void getByIdNonExistentTest() {
                // Given
                String PATH_URL = "0";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<EUserDTO.Out.UserInformation> r_student = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), EUserDTO.Out.UserInformation.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
        }

        @Test
        public void postSearchUser() {
                // Given
                String PATH_URL = "search";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                EUserDTO.In.SearchInformation infos = new EUserDTO.In.SearchInformation();
                infos.setEmail(student.getEmail());

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<List> r_student = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.POST,
                                new HttpEntity<>(infos, headers), List.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertNotEquals(0, r_student.getBody().size());
        }

        @Test
        public void createUserTest() {
                String PATH_URL = "create-user";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                EUserDTO.In.NewUser infos = new EUserDTO.In.NewUser();
                infos.setEmail("createUserTest" + this.hashCode());
                infos.setFirstName("createUserTest" + this.hashCode());
                infos.setLastName("createUserTest" + this.hashCode());

                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<String> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.POST,
                                new HttpEntity<>(infos, headers), String.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<String> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.POST,
                                new HttpEntity<>(infos, headers), String.class);

                // Then
                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());

                assertNotNull(r_responsable.getBody());
        }
}
