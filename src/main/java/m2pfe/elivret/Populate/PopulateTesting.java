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

        createExampleLivret();
    }

    private void createUsers(){
        createAndSaveUser("email", "password" , UserRole.RESPONSABLE);
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
        ELivret livret = new ELivret(0, "Livret_test", student, tutor, master, responsable, null);

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
        createAndSaveEQuestion(section, "Question 2 :", QuestionType.CHECKBOX);
        createAndSaveEQuestion(section, "Question 3 :", QuestionType.RADIO);

        return section;
    }

    private EQuestion createAndSaveEQuestion(ESection section, String title, QuestionType type){
        EQuestion question;
        
        switch (type) {
            case LABEL:
                question = labelQuestion();
                break;
            case TEXT:
                question = singleQuestion("REPONSE : " + title);
                break;
            case CHECKBOX:
            case RADIO:
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

        return question;
    }

    private EAnswer createAndSaveAnswer(String proposition, String value, EQuestion question) {
        return ar.save(new EAnswer(0, proposition, null, question));
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


    private void createExampleLivret() {
        EUser student = createAndSaveUser("etudiant2@mail.com", "etudiant2", UserRole.STUDENT);

        EUser responsable   = ur.findByEmail("responsable@mail.com").get();
        EUser tutor         = ur.findByEmail("tuteur@mail.com").get();
        EUser master        = ur.findByEmail("maitre@mail.com").get();


        ELivret livret              = lr.save(new ELivret(0, "Livret d'apprentissage", student, tutor, master, responsable, null));


        ESection s_ident_student    = sr.save(new ESection(0, UserRole.STUDENT, true, "Fiche d'identité de l'apprenti(e)"               , livret, null));
        ESection s_ident_company    = sr.save(new ESection(0, UserRole.MASTER , true, "Informations sur l'entreprise d'accueil"         , livret, null));
        ESection s_ident_master     = sr.save(new ESection(0, UserRole.MASTER , true, "Fiche d'identité du maitre d'apprentissage"      , livret, null));
        ESection s_ident_fomatation = sr.save(new ESection(0, UserRole.TUTOR  , true, "Informations sur la formation de l'apprenti(e)"  , livret, null));
        ESection s_ident_tutor      = sr.save(new ESection(0, UserRole.TUTOR  , true, "Fiche d'identité de tuteur pédagogique"          , livret, null));
        
        ESection s_present_forma    = sr.save(new ESection(0, UserRole.TUTOR  , true, "Présentation de la formation"                    , livret, null));
        ESection s_calendar         = sr.save(new ESection(0, UserRole.TUTOR  , true, "Le calendrier d'alterance"                       , livret, null));

        ESection s_suivi_1_sem      = sr.save(new ESection(0, UserRole.MASTER , true, "Fiche de suivi du 1er semestre"                          , livret, null));
        ESection s_comment_master_1 = sr.save(new ESection(0, UserRole.MASTER , true, "Commentaire du maitre d'apprentissage du 1er semestre"   , livret, null));
        ESection s_comment_stud_1   = sr.save(new ESection(0, UserRole.STUDENT, true, "Commentaire de l'apprenti(e) du 1er semestre"            , livret, null));
        ESection s_comment_tutor_1  = sr.save(new ESection(0, UserRole.TUTOR  , true, "Commentaire du tuteur pédagogique du 1er semestre"       , livret, null));
        
        ESection s_suivi_2_sem      = sr.save(new ESection(0, UserRole.MASTER , true, "Fiche de suivi du 2ème semestre"                         , livret, null));
        ESection s_comment_master_2 = sr.save(new ESection(0, UserRole.MASTER , true, "Commentaire du maitre d'apprentissage du 2ème semestre"  , livret, null));
        ESection s_comment_stud_2   = sr.save(new ESection(0, UserRole.STUDENT, true, "Commentaire de l'apprenti(e) du 2ème semestre"           , livret, null));
        ESection s_comment_tutor_2  = sr.save(new ESection(0, UserRole.TUTOR  , true, "Commentaire du tuteur pédagogique du 2ème semestre"      , livret, null));
        
        
        ESection s_eval_student     = sr.save(new ESection(0, UserRole.STUDENT, true, "Evaluation de la formation par les apprentis"    , livret, null));
        ESection s_eval_company     = sr.save(new ESection(0, UserRole.MASTER , true, "Evaluation de la formation par l'entreprise"     , livret, null));
    
    
        EQuestion q_ident_student_1     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.RADIO) .title("Civilité").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_1).proposition("Homme").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_1).proposition("Femme").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_1).proposition("Autre").build());
        EQuestion q_ident_student_2     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.TEXT)  .title("Nom").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_2).build());
        EQuestion q_ident_student_3     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.TEXT)  .title("Prénom").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_3).build());
        EQuestion q_ident_student_4     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.TEXT)  .title("Date de naissance").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_4).build());
        EQuestion q_ident_student_5     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.TEXT)  .title("Adresse").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_5).build());
        EQuestion q_ident_student_6     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.TEXT)  .title("Téléphone").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_6).build());
        EQuestion q_ident_student_7     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.TEXT)  .title("Mail").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_7).build());
        EQuestion q_ident_student_8     = qr.save(EQuestion.builder().section(s_ident_student)      .type(QuestionType.TEXT)  .title("Dates de contrat").build());
            ar.save(EAnswer.builder()       .question(q_ident_student_8).build());

        EQuestion q_ident_company_1     = qr.save(EQuestion.builder().section(s_ident_company)      .type(QuestionType.TEXT)  .title("Nom").build());
            ar.save(EAnswer.builder()       .question(q_ident_company_1).build());
        EQuestion q_ident_company_2     = qr.save(EQuestion.builder().section(s_ident_company)      .type(QuestionType.TEXT)  .title("Adresse").build());
            ar.save(EAnswer.builder()       .question(q_ident_company_2).build());
        
        EQuestion q_ident_master_1      = qr.save(EQuestion.builder().section(s_ident_master)       .type(QuestionType.TEXT)  .title("Nom").build());
            ar.save(EAnswer.builder()       .question(q_ident_master_1).build());
        EQuestion q_ident_master_2      = qr.save(EQuestion.builder().section(s_ident_master)       .type(QuestionType.TEXT)  .title("Prénom").build());
            ar.save(EAnswer.builder()       .question(q_ident_master_2).build());
        EQuestion q_ident_master_3      = qr.save(EQuestion.builder().section(s_ident_master)       .type(QuestionType.TEXT)  .title("Fonction").build());
            ar.save(EAnswer.builder()       .question(q_ident_master_3).build());
        EQuestion q_ident_master_4      = qr.save(EQuestion.builder().section(s_ident_master)       .type(QuestionType.TEXT)  .title("Téléphone").build());
            ar.save(EAnswer.builder()       .question(q_ident_master_4).build());
        EQuestion q_ident_master_5      = qr.save(EQuestion.builder().section(s_ident_master)       .type(QuestionType.TEXT)  .title("Mail").build());
            ar.save(EAnswer.builder()       .question(q_ident_master_5).build());
        
        EQuestion q_ident_fomatation_1  = qr.save(EQuestion.builder().section(s_ident_fomatation)   .type(QuestionType.TEXT)  .title("Mail").build());
            ar.save(EAnswer.builder()       .question(q_ident_fomatation_1).build());
        EQuestion q_ident_fomatation_2  = qr.save(EQuestion.builder().section(s_ident_fomatation)   .type(QuestionType.TEXT)  .title("Adresse").build());
            ar.save(EAnswer.builder()       .question(q_ident_fomatation_2).build());
        
        EQuestion q_ident_tutor_1       = qr.save(EQuestion.builder().section(s_ident_tutor)        .type(QuestionType.TEXT)  .title("Nom").build());
            ar.save(EAnswer.builder()       .question(q_ident_tutor_1).build());
        EQuestion q_ident_tutor_2       = qr.save(EQuestion.builder().section(s_ident_tutor)        .type(QuestionType.TEXT)  .title("Prénom").build());
            ar.save(EAnswer.builder()       .question(q_ident_tutor_2).build());
        EQuestion q_ident_tutor_3       = qr.save(EQuestion.builder().section(s_ident_tutor)        .type(QuestionType.TEXT)  .title("Fonction").build());
            ar.save(EAnswer.builder()       .question(q_ident_tutor_3).build());
        EQuestion q_ident_tutor_4       = qr.save(EQuestion.builder().section(s_ident_tutor)        .type(QuestionType.TEXT)  .title("Téléphone").build());
            ar.save(EAnswer.builder()       .question(q_ident_tutor_4).build());
        EQuestion q_ident_tutor_5       = qr.save(EQuestion.builder().section(s_ident_tutor)        .type(QuestionType.TEXT)  .title("Mail").build());
            ar.save(EAnswer.builder()       .question(q_ident_tutor_5).build());

    }
}
