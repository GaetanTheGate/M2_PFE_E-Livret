package m2pfe.elivret.ELivret;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import m2pfe.elivret.Starter;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.ELivret.ELivret.ELivretBuilder;
import m2pfe.elivret.ELivret.ELivretDTO.Out.AllPublic;
import m2pfe.elivret.EModel.ELivretModel;
import m2pfe.elivret.EQuestion.EQuestion;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUser.Permission;
import m2pfe.elivret.Populate.EntityManager;
import m2pfe.elivret.UtilityForTest.RestErrorHandlerNoThrows;

@SpringBootTest(classes = Starter.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestELivretController {

    private RestTemplate rest;
    private static String BASE_URL = "http://localhost:8081/api/livrets/";

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
                .email("livret_student@mail.com" + this.hashCode())
                .password("livret_student")
                .firstName("livret_student_FIRST")
                .lastName("livret_student_LAST")
                .permission(Permission.USER)
                .build());

        master = manager.saveUser(EUser.builder()
                .email("livret_master@mail.com" + this.hashCode())
                .password("livret_master")
                .firstName("livret_master_FIRST")
                .lastName("livret_master_LAST")
                .permission(Permission.USER)
                .build());

        tutor = manager.saveUser(EUser.builder()
                .email("livret_tutor@mail.com" + this.hashCode())
                .password("livret_tutor")
                .firstName("livret_tutor_FIRST")
                .lastName("livret_tutor_LAST")
                .permission(Permission.USER)
                .build());

        responsable = manager.saveUser(EUser.builder()
                .email("livret_responsable@mail.com" + this.hashCode())
                .password("livret_responsable")
                .firstName("livret_responsable_FIRST")
                .lastName("livret_responsable_LAST")
                .permission(Permission.RESPONSABLE)
                .build());

        someone = manager.saveUser(EUser.builder()
                .email("livret_someone@mail.com" + this.hashCode())
                .password("livret_someone")
                .firstName("livret_someone_FIRST")
                .lastName("livret_someone_LAST")
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

    private ELivretBuilder buildLivretBase(String name) {
        return ELivret.builder()
                .name(name)
                .student(student)
                .master(master)
                .tutor(tutor)
                .responsable(responsable);
    }

    /// GET

    @Test
    public void getFromIdTest() {
        // Given
        ELivret l = buildLivretBase("getFromIdTest").build();
        l = manager.saveLivret(l);

        String PATH_URL = l.getId().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        // When
        headers.set("Authorization", "Bearer " + token_student);
        ResponseEntity<AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_master);
        ResponseEntity<AllPublic> r_master = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_tutor);
        ResponseEntity<AllPublic> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_someone);
        ResponseEntity<AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.OK, r_student.getStatusCode());
        assertEquals(l.getId(), r_student.getBody().getId());

        assertEquals(HttpStatus.OK, r_master.getStatusCode());
        assertEquals(l.getId(), r_master.getBody().getId());

        assertEquals(HttpStatus.OK, r_tutor.getStatusCode());
        assertEquals(l.getId(), r_tutor.getBody().getId());

        assertEquals(HttpStatus.OK, r_responsable.getStatusCode());
        assertEquals(l.getId(), r_responsable.getBody().getId());

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
        ResponseEntity<AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
        assertNull(r_student.getBody().getId());
    }

    @Test
    public void getModelFromIdTest() {
        // Given
        ELivret l = buildLivretBase("getModelFromIdTest")
                .sections(List.of(new ESection(), new ESection()))
                .build();
        l = manager.saveLivret(l);

        String PATH_URL = l.getId().toString() + "/model";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        // When
        headers.set("Authorization", "Bearer " + token_tutor);
        ResponseEntity<ELivretModel> r_tutor = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), ELivretModel.class);

        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<ELivretModel> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), ELivretModel.class);

        headers.set("Authorization", "Bearer " + token_someone);
        ResponseEntity<ELivretModel> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), ELivretModel.class);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, r_tutor.getStatusCode());
        assertNull(r_tutor.getBody());

        assertEquals(HttpStatus.OK, r_responsable.getStatusCode());
        assertEquals(l.getSections().size(), r_responsable.getBody().getSections().size());

        assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
        assertNull(r_someone.getBody());
    }

    @Test
    public void getModelFromIdNonExistentTest() {
        // Given
        String PATH_URL = "0/model";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        // When
        headers.set("Authorization", "Bearer " + token_student);
        ResponseEntity<AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, r_student.getStatusCode());
        assertNull(r_student.getBody().getId());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void getMineTest() {
        // Given
        ELivret l1 = buildLivretBase("getMineTest1")
                .student(null)
                .sections(List.of(new ESection(), new ESection()))
                .build();

        ELivret l2 = buildLivretBase("getMineTest2")
                .sections(List.of(new ESection(), new ESection()))
                .build();

        l1 = manager.saveLivret(l1);
        l2 = manager.saveLivret(l2);

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
        assertEquals(2, r_tutor.getBody().size());

        assertEquals(HttpStatus.OK, r_someone.getStatusCode());
        assertEquals(0, r_someone.getBody().size());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void getMineToCompleteTest() {
        // Given
        ELivret l1 = buildLivretBase("getMineToCompleteTest1")
                .sections(List.of(new ESection()))
                .build();

        ELivret l2 = buildLivretBase("getMineToCompleteTest2")
                .sections(List.of(ESection.builder()
                        .questions(List.of(EQuestion.builder()
                                .answers(List.of(EAnswer.builder()
                                        .build()))
                                .build()))
                        .build()))
                .build();

        l1 = manager.saveLivret(l1);
        l2 = manager.saveLivret(l2);

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

    @Test
    public void postCreateLivretTest() {
        // Given
        ELivretDTO.In.Create livret = new ELivretDTO.In.Create();
        livret.setName("postCreateLivretTest");

        String PATH_URL = "/create";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        // When
        headers.set("Authorization", "Bearer " + token_someone);
        ResponseEntity<AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.POST,
                new HttpEntity<>(livret, headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.POST,
                new HttpEntity<>(livret, headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
        assertEquals(HttpStatus.OK, r_responsable.getStatusCode());

        assertEquals(livret.getName(), r_responsable.getBody().getName());
    }

    /// PUT

    @Test
    public void putModelFromIdTest() {
        // Given
        ELivret l1 = buildLivretBase("putModelFromIdTest1")
                .sections(List.of(new ESection()))
                .build();
        ELivret l2 = buildLivretBase("putModelFromIdTest2")
                .build();

        l1 = manager.saveLivret(l1);
        l2 = manager.saveLivret(l2);

        String PATH_URL = l1.getId().toString() + "/model";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        headers.set("Authorization", "Bearer " + token_responsable);
        ELivretModel model = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), ELivretModel.class).getBody();

        PATH_URL = l2.getId().toString() + "/model";

        // When
        headers.set("Authorization", "Bearer " + token_student);
        ResponseEntity<AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(model, headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_someone);
        ResponseEntity<AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(model, headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_model = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(model, headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
        assertEquals(HttpStatus.OK, r_model.getStatusCode());

        assertEquals(l1.getSections().size(), r_model.getBody().getSections().size());

        assertEquals(l2.getName(), r_model.getBody().getName());
        assertEquals(l2.getStudent(), r_model.getBody().getStudent());
        assertEquals(l2.getMaster(), r_model.getBody().getMaster());
        assertEquals(l2.getTutor(), r_model.getBody().getTutor());
        assertEquals(l2.getResponsable(), r_model.getBody().getResponsable());
    }

    @Test
    public void putModelFromIdNonExistentTest() {
        // Given
        String PATH_URL = "0/model";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        ELivretModel model = new ELivretModel();

        // When
        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_model = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(model, headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, r_model.getStatusCode());
    }

    @Test
    public void putActorsTest() {
        // Given
        ELivret l = buildLivretBase("putActorsTest")
                .student(null)
                .master(null)
                .tutor(null)
                .build();
        l = manager.saveLivret(l);

        String PATH_URL = "set-actors?setStudent=true&setMaster=true&setTutor=false";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        ELivretDTO.In.Actors actors = new ELivretDTO.In.Actors();
        actors.setId(l.getId());
        actors.setStudentId(student.getId());
        actors.setMasterId(master.getId());
        actors.setTutorId(tutor.getId());

        // When
        headers.set("Authorization", "Bearer " + token_student);
        ResponseEntity<AllPublic> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(actors, headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_someone);
        ResponseEntity<AllPublic> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(actors, headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(actors, headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
        assertEquals(HttpStatus.OK, r_responsable.getStatusCode());

        assertEquals(student.getId(), r_responsable.getBody().getStudent().getId());
        assertEquals(master.getId(), r_responsable.getBody().getMaster().getId());
        assertNull(r_responsable.getBody().getTutor());
    }

    @Test
    public void putActorsWithNonExistentActorTest() {
        // Given
        ELivret l = buildLivretBase("putActorsWithNonExistentActorTest")
                .student(null)
                .build();
        l = manager.saveLivret(l);

        String PATH_URL = "set-actors?setStudent=true";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        ELivretDTO.In.Actors actors = new ELivretDTO.In.Actors();
        actors.setId(l.getId());
        actors.setStudentId(0);

        // When
        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(actors, headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, r_responsable.getStatusCode());
    }

    @Test
    public void putActorsWithNonExistentLivretTest() {
        // Given
        String PATH_URL = "set-actors?setStudent=true";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        ELivretDTO.In.Actors actors = new ELivretDTO.In.Actors();
        actors.setId(0);
        actors.setStudentId(student.getId());

        // When
        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.PUT,
                new HttpEntity<>(actors, headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, r_responsable.getStatusCode());
    }

    /// DELETE

    @Test
    public void deleteLivretFromIdTest() {
        // Given
        ELivret l = buildLivretBase("deleteLivretFromIdTest")
                .sections(List.of(new ESection(), new ESection()))
                .build();
        l = manager.saveLivret(l);

        String PATH_URL = l.getId().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        // When
        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_responsable_get1 = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        headers.set("Authorization", "Bearer " + token_student);
        ResponseEntity<Void> r_student = rest.exchange(BASE_URL + PATH_URL, HttpMethod.DELETE,
                new HttpEntity<>(headers), Void.class);

        headers.set("Authorization", "Bearer " + token_someone);
        ResponseEntity<Void> r_someone = rest.exchange(BASE_URL + PATH_URL, HttpMethod.DELETE,
                new HttpEntity<>(headers), Void.class);

        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<Void> r_responsable = rest.exchange(BASE_URL + PATH_URL, HttpMethod.DELETE,
                new HttpEntity<>(headers), Void.class);

        ResponseEntity<AllPublic> r_responsable_get2 = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, r_student.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, r_someone.getStatusCode());
        assertEquals(HttpStatus.OK, r_responsable.getStatusCode());

        assertEquals(HttpStatus.OK, r_responsable_get2.getStatusCode());
        assertEquals(l.getId(), r_responsable_get2.getBody().getId());

        assertEquals(HttpStatus.OK, r_responsable_get2.getStatusCode());
        assertEquals(null, r_responsable_get2.getBody());
    }

    @Test
    public void deleteLivretFromIdNonExistentTest() {
        // Given
        String PATH_URL = "0";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "");

        // When
        headers.set("Authorization", "Bearer " + token_responsable);
        ResponseEntity<AllPublic> r_get = rest.exchange(BASE_URL + PATH_URL, HttpMethod.GET,
                new HttpEntity<>(headers), AllPublic.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, r_get.getStatusCode());
    }
}
