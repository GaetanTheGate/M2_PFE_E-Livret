package m2pfe.elivret.ESection;

import javax.annotation.PostConstruct;

import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.ELivret.ELivretDTO;
import m2pfe.elivret.EQuestion.EQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.ESection.ESection.ESectionBuilder;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUser.Permission;
import m2pfe.elivret.Populate.EntityManager;
import m2pfe.elivret.UtilityForTest.RestErrorHandlerNoThrows;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = Starter.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestESectionController {

        private RestTemplate rest;
        private static String BASE_URL = "http://localhost:8081/api/sections/";

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
                                .email("section_student@mail.com" + this.hashCode())
                                .password("section_student")
                                .firstName("section_student_FIRST")
                                .lastName("section_student_LAST")
                                .permission(Permission.USER)
                                .build());

                master = manager.saveUser(EUser.builder()
                                .email("section_master@mail.com" + this.hashCode())
                                .password("section_master")
                                .firstName("section_master_FIRST")
                                .lastName("section_master_LAST")
                                .permission(Permission.USER)
                                .build());

                tutor = manager.saveUser(EUser.builder()
                                .email("section_tutor@mail.com" + this.hashCode())
                                .password("section_tutor")
                                .firstName("section_tutor_FIRST")
                                .lastName("section_tutor_LAST")
                                .permission(Permission.USER)
                                .build());

                responsable = manager.saveUser(EUser.builder()
                                .email("section_responsable@mail.com" + this.hashCode())
                                .password("section_responsable")
                                .firstName("section_responsable_FIRST")
                                .lastName("section_responsable_LAST")
                                .permission(Permission.RESPONSABLE)
                                .build());

                someone = manager.saveUser(EUser.builder()
                                .email("section_someone@mail.com" + this.hashCode())
                                .password("section_someone")
                                .firstName("section_someone_FIRST")
                                .lastName("section_someone_LAST")
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

        private ESectionBuilder buildSectionBase(String title, UserRole owner, Boolean visibility) {
                ELivret livret = ELivret.builder()
                                .name(title)
                                .student(student)
                                .master(master)
                                .tutor(tutor)
                                .responsable(responsable)
                                .build();

                livret = manager.saveLivret(livret);

                return ESection.builder()
                                .title(title)
                                .owner(owner)
                                .visibility(visibility)
                                .livret(livret);
        }

        /// GET

        @Test
        public void getFromIdTest() {
                // Given
                ESection s = buildSectionBase("getFromIdTest", UserRole.MASTER, true).build();
                s = manager.saveSection(s);

                String PATH_URL = s.getId().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_master);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_master = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(s.getId(), r_student.getBody().getId());

                assertEquals(HttpStatus.OK, r_master.getStatusCode());
                assertEquals(s.getId(), r_master.getBody().getId());

                assertEquals(HttpStatus.OK, r_tutor.getStatusCode());
                assertEquals(s.getId(), r_tutor.getBody().getId());

                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());
                assertEquals(s.getId(), r_responsable.getBody().getId());

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
                ResponseEntity<ESectionDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
                assertNull(r_student.getBody());
        }

        @Test
        public void getMineTest() {
                // Given
                ESection s1 = buildSectionBase("getMineTest1", UserRole.STUDENT, true).build();

                ESection s2 = buildSectionBase("getMineTest2", UserRole.TUTOR, true).build();

                s1 = manager.saveSection(s1);
                s2 = manager.saveSection(s2);

                String PATH_URL = "/mine";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<List> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), List.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<List> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), List.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<List> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), List.class);

                // Then
                assertEquals(HttpStatus.OK, r_student.getStatusCode());
                assertEquals(1, r_student.getBody().size());

                assertEquals(HttpStatus.OK, r_tutor.getStatusCode());
                assertEquals(1, r_tutor.getBody().size());

                assertEquals(HttpStatus.OK, r_someone.getStatusCode());
                assertEquals(0, r_someone.getBody().size());
        }

        @Test
        public void getMineToCompleteTest() {
                // Given
                ESection s1 = buildSectionBase("getMineToCompleteTest1", UserRole.STUDENT, true)
                                .build();

                ESection s2 = buildSectionBase("getMineToCompleteTest2", UserRole.STUDENT, true)
                                .questions(List.of(EQuestion.builder()
                                                .answers(List.of(EAnswer.builder()
                                                                .build()))
                                                .build()))
                                .build();

                ESection s3 = buildSectionBase("getMineToCompleteTest3", UserRole.STUDENT, false)
                                .questions(List.of(EQuestion.builder()
                                                .answers(List.of(EAnswer.builder()
                                                                .build()))
                                                .build()))
                                .build();

                s1 = manager.saveSection(s1);
                s2 = manager.saveSection(s2);
                s3 = manager.saveSection(s3);

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

        // Rien car pas de méthode de création de section

        /// PUT

        @Test
        public void putVisibilityOnSectionTest() {
                // Given
                ESection s = buildSectionBase("putVisibilityOnSectionTest", UserRole.STUDENT, false).build();

                s = manager.saveSection(s);

                String PATH_URL = "/saveVisibility";

                ESectionDTO.In.Visibility section = new ESectionDTO.In.Visibility();
                section.setId(s.getId());
                section.setVisibility(true);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(section, headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(section, headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(section, headers), ESectionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
                assertEquals(HttpStatus.OK, r_tutor.getStatusCode());

                assertEquals(true, r_tutor.getBody().getVisibility());

        }

        @Test
        public void putVisibilityOnNonExistentSectionTest() {
                String PATH_URL = "/saveVisibility";

                ESectionDTO.In.Visibility section = new ESectionDTO.In.Visibility();
                section.setId(0);
                section.setVisibility(true);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_student);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(section, headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_tutor);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(section, headers), ESectionDTO.Out.AllPublic.class);

                headers.set("Authorization", "Bearer " + token_someone);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                                new HttpEntity<>(section, headers), ESectionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_someone.getStatusCode());
                assertEquals(HttpStatus.NO_CONTENT, r_tutor.getStatusCode());

        }

        /// DELETE

        @Test
        public void deleteSectionFromId() {
                // Given
                ESection s = buildSectionBase("deleteSectionFromId", UserRole.STUDENT, false).build();

                s = manager.saveSection(s);

                String PATH_URL = s.getId().toString();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");
                // When
                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_responsable_get1 = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

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

                ResponseEntity<ESectionDTO.Out.AllPublic> r_responsable_get2 = rest.exchange(BASE_URL + PATH_URL,
                                HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.OK, r_responsable_get1.getStatusCode());
                assertEquals(s.getId(), r_responsable_get1.getBody().getId());

                assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
                assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
                assertEquals(HttpStatus.OK, r_responsable.getStatusCode());

                assertEquals(HttpStatus.NO_CONTENT, r_responsable_get2.getStatusCode());
        }

        @Test
        public void deleteSectionFromIdNonExistent() {
                // Given
                String PATH_URL = "0";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "");

                // When
                headers.set("Authorization", "Bearer " + token_responsable);
                ResponseEntity<ESectionDTO.Out.AllPublic> r_get = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                                new HttpEntity<>(headers), ESectionDTO.Out.AllPublic.class);

                // Then
                assertEquals(HttpStatus.NO_CONTENT, r_get.getStatusCode());
        }
}
