package m2pfe.elivret.ESection;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    }

    @Test
    public void getFromIdNonExistentTest() {

    }

    @Test
    public void getMineTest() {

    }

    @Test
    public void getMineToCompleteTest() {

    }

    /// POST

    // Rien car pas de méthode de création de section

    /// PUT

    @Test
    public void putVisilityOnSectionTest() {

    }

    @Test
    public void putVisilityOnNonExistentSectionTest() {

    }

    /// DELETE

    @Test
    public void deleteSectionFromId() {

    }

    @Test
    public void deleteSectionFromIdNonExistent() {

    }
}
