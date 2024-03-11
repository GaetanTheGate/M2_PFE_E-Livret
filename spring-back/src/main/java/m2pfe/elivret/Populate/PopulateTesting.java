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
import m2pfe.elivret.EUser.EUser.Permission;

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

        @PostConstruct
        public void initTest() {
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
                ESection section = new ESection(0, owner, visibility, title, livret, null);

                section = sr.save(section);

                createAndSaveEQuestion(section, "Texte a caractere informatif :", QuestionType.LABEL);
                createAndSaveEQuestion(section, "Question 1 :", QuestionType.TEXT);
                createAndSaveEQuestion(section, "Question 2 :", QuestionType.CHECKBOX);
                createAndSaveEQuestion(section, "Question 3 :", QuestionType.RADIO);

                return sr.findById(section.getId()).get();
        }

        private EQuestion createAndSaveEQuestion(ESection section, String title, QuestionType type) {
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
                                question = multipleQuestion(List.of("Valeur 1", "Valeur 2", "Valeur 3"),
                                                List.of("Reponse 1 : " + title, "Reponse 2 : " + title,
                                                                "Reponse 3 : " + title));
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

                return qr.findById(question.getId()).get();
        }

        private EAnswer createAndSaveAnswer(String proposition, String value, EQuestion question) {
                return ar.save(new EAnswer(0, proposition, null, question));
        }

        private SingleChoiceQuestion singleQuestion(String anwser) {
                return new SingleChoiceQuestion();
        }

        private MultipleChoiceQuestion multipleQuestion(List<String> values, List<String> answers) {
                // List<EAnswer> as = answers.stream().map(answer ->
                // createAndSaveAnswer(answer)).toList();
                return new MultipleChoiceQuestion();
        }

        private NoChoiceQuestion labelQuestion() {
                return new NoChoiceQuestion();
        }

        private void createExampleLivret() {
                EUser student = createAndSaveUser("etudiant2@mail.com", "etudiant2", Permission.USER, "Guillaume",
                                "Dulac");

                EUser responsable = ur.findByEmail("responsable@mail.com").get();
                EUser tutor = ur.findByEmail("tuteur@mail.com").get();
                EUser master = ur.findByEmail("maitre@mail.com").get();

                ELivret livret = lr.save(
                                new ELivret(0, "Livret d'apprentissage", student, tutor, master, responsable, null));

                ESection s_ident_student = sr
                                .save(new ESection(0, UserRole.STUDENT, true, "Fiche d'identité de l'apprenti(e)",
                                                livret, null));
                ESection s_ident_company = sr
                                .save(new ESection(0, UserRole.MASTER, true, "Informations sur l'entreprise d'accueil",
                                                livret, null));
                ESection s_ident_master = sr.save(
                                new ESection(0, UserRole.MASTER, true, "Fiche d'identité du maitre d'apprentissage",
                                                livret, null));
                ESection s_ident_fomatation = sr.save(
                                new ESection(0, UserRole.TUTOR, true, "Informations sur la formation de l'apprenti(e)",
                                                livret, null));
                ESection s_ident_tutor = sr
                                .save(new ESection(0, UserRole.TUTOR, true, "Fiche d'identité de tuteur pédagogique",
                                                livret, null));

                ESection s_present_forma = sr
                                .save(new ESection(0, UserRole.TUTOR, true, "Présentation de la formation", livret,
                                                null));
                ESection s_calendar = sr
                                .save(new ESection(0, UserRole.TUTOR, true, "Le calendrier d'alterance", livret, null));

                ESection s_suivi_1_sem = sr
                                .save(new ESection(0, UserRole.MASTER, true, "Fiche de suivi du 1er semestre", livret,
                                                null));
                ESection s_observ_master_1 = sr.save(new ESection(0, UserRole.MASTER, true,
                                "Observations du maître d’apprentissage du 2ème semestre", livret, null));
                ESection s_comment_master_1 = sr.save(new ESection(0, UserRole.MASTER, true,
                                "Commentaire du maitre d'apprentissage du 1er semestre", livret, null));
                ESection s_comment_stud_1 = sr.save(
                                new ESection(0, UserRole.STUDENT, true, "Commentaire de l'apprenti(e) du 1er semestre",
                                                livret, null));
                ESection s_comment_tutor_1 = sr.save(new ESection(0, UserRole.TUTOR, true,
                                "Commentaire du tuteur pédagogique du 1er semestre", livret, null));

                ESection s_suivi_2_sem = sr
                                .save(new ESection(0, UserRole.MASTER, true, "Fiche de suivi du 2ème semestre", livret,
                                                null));
                ESection s_observ_master_2 = sr.save(new ESection(0, UserRole.MASTER, true,
                                "Observations du maître d’apprentissage du 2ème semestre", livret, null));
                ESection s_comment_master_2 = sr.save(new ESection(0, UserRole.MASTER, true,
                                "Commentaire du maitre d'apprentissage du 2ème semestre", livret, null));
                ESection s_comment_stud_2 = sr.save(
                                new ESection(0, UserRole.STUDENT, true, "Commentaire de l'apprenti(e) du 2ème semestre",
                                                livret, null));
                ESection s_comment_tutor_2 = sr.save(new ESection(0, UserRole.TUTOR, true,
                                "Commentaire du tuteur pédagogique du 2ème semestre", livret, null));

                ESection s_eval_student = sr.save(
                                new ESection(0, UserRole.STUDENT, true, "Evaluation de la formation par les apprentis",
                                                livret, null));
                ESection s_eval_company = sr.save(
                                new ESection(0, UserRole.MASTER, true, "Evaluation de la formation par l'entreprise",
                                                livret, null));

                EQuestion q_ident_student_1 = qr
                                .save(EQuestion.builder().section(s_ident_student).type(QuestionType.RADIO)
                                                .title("Civilité").build());
                ar.save(EAnswer.builder().question(q_ident_student_1).proposition("Homme").build());
                ar.save(EAnswer.builder().question(q_ident_student_1).proposition("Femme").build());
                ar.save(EAnswer.builder().question(q_ident_student_1).proposition("Autre").build());
                EQuestion q_ident_student_2 = qr
                                .save(EQuestion.builder().section(s_ident_student).type(QuestionType.TEXT).title("Nom")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_student_2).build());
                EQuestion q_ident_student_3 = qr
                                .save(EQuestion.builder().section(s_ident_student).type(QuestionType.TEXT)
                                                .title("Prénom").build());
                ar.save(EAnswer.builder().question(q_ident_student_3).build());
                EQuestion q_ident_student_4 = qr
                                .save(EQuestion.builder().section(s_ident_student).type(QuestionType.TEXT)
                                                .title("Date de naissance").build());
                ar.save(EAnswer.builder().question(q_ident_student_4).build());
                EQuestion q_ident_student_5 = qr
                                .save(EQuestion.builder().section(s_ident_student).type(QuestionType.TEXT)
                                                .title("Adresse").build());
                ar.save(EAnswer.builder().question(q_ident_student_5).build());
                EQuestion q_ident_student_6 = qr
                                .save(EQuestion.builder().section(s_ident_student).type(QuestionType.TEXT)
                                                .title("Téléphone").build());
                ar.save(EAnswer.builder().question(q_ident_student_6).build());
                EQuestion q_ident_student_7 = qr
                                .save(EQuestion.builder().section(s_ident_student).type(QuestionType.TEXT).title("Mail")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_student_7).build());
                EQuestion q_ident_student_8 = qr.save(
                                EQuestion.builder().section(s_ident_student).type(QuestionType.TEXT)
                                                .title("Dates de contrat").build());
                ar.save(EAnswer.builder().question(q_ident_student_8).build());

                EQuestion q_ident_company_1 = qr
                                .save(EQuestion.builder().section(s_ident_company).type(QuestionType.TEXT).title("Nom")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_company_1).build());
                EQuestion q_ident_company_2 = qr
                                .save(EQuestion.builder().section(s_ident_company).type(QuestionType.TEXT)
                                                .title("Adresse").build());
                ar.save(EAnswer.builder().question(q_ident_company_2).build());

                EQuestion q_ident_master_1 = qr
                                .save(EQuestion.builder().section(s_ident_master).type(QuestionType.TEXT).title("Nom")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_master_1).build());
                EQuestion q_ident_master_2 = qr
                                .save(EQuestion.builder().section(s_ident_master).type(QuestionType.TEXT)
                                                .title("Prénom").build());
                ar.save(EAnswer.builder().question(q_ident_master_2).build());
                EQuestion q_ident_master_3 = qr
                                .save(EQuestion.builder().section(s_ident_master).type(QuestionType.TEXT)
                                                .title("Fonction").build());
                ar.save(EAnswer.builder().question(q_ident_master_3).build());
                EQuestion q_ident_master_4 = qr
                                .save(EQuestion.builder().section(s_ident_master).type(QuestionType.TEXT)
                                                .title("Téléphone").build());
                ar.save(EAnswer.builder().question(q_ident_master_4).build());
                EQuestion q_ident_master_5 = qr
                                .save(EQuestion.builder().section(s_ident_master).type(QuestionType.TEXT).title("Mail")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_master_5).build());

                EQuestion q_ident_fomatation_1 = qr
                                .save(EQuestion.builder().section(s_ident_fomatation).type(QuestionType.TEXT)
                                                .title("Mail").build());
                ar.save(EAnswer.builder().question(q_ident_fomatation_1).build());
                EQuestion q_ident_fomatation_2 = qr
                                .save(EQuestion.builder().section(s_ident_fomatation).type(QuestionType.TEXT)
                                                .title("Adresse").build());
                ar.save(EAnswer.builder().question(q_ident_fomatation_2).build());

                EQuestion q_ident_tutor_1 = qr
                                .save(EQuestion.builder().section(s_ident_tutor).type(QuestionType.TEXT).title("Nom")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_tutor_1).build());
                EQuestion q_ident_tutor_2 = qr
                                .save(EQuestion.builder().section(s_ident_tutor).type(QuestionType.TEXT).title("Prénom")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_tutor_2).build());
                EQuestion q_ident_tutor_3 = qr
                                .save(EQuestion.builder().section(s_ident_tutor).type(QuestionType.TEXT)
                                                .title("Fonction").build());
                ar.save(EAnswer.builder().question(q_ident_tutor_3).build());
                EQuestion q_ident_tutor_4 = qr
                                .save(EQuestion.builder().section(s_ident_tutor).type(QuestionType.TEXT)
                                                .title("Téléphone").build());
                ar.save(EAnswer.builder().question(q_ident_tutor_4).build());
                EQuestion q_ident_tutor_5 = qr
                                .save(EQuestion.builder().section(s_ident_tutor).type(QuestionType.TEXT).title("Mail")
                                                .build());
                ar.save(EAnswer.builder().question(q_ident_tutor_5).build());

                EQuestion q_present_forma_1 = qr.save(EQuestion.builder().section(s_present_forma)
                                .type(QuestionType.LABEL)
                                .title("Le document complet de présentation de la formation est disponible à cette adresse")
                                .build());
                EQuestion q_present_forma_2 = qr.save(EQuestion.builder().section(s_present_forma)
                                .type(QuestionType.LABEL)
                                .title("https://jean-luc-massat.pedaweb.univ-amu.fr/ens/alternance-en-m2idl/").build());

                EQuestion q_calendar_1 = qr.save(EQuestion.builder().section(s_calendar).type(QuestionType.LABEL)
                                .title("Le calendrier d’alternance de la formation est disponible à cette adresse")
                                .build());
                EQuestion q_calendar_2 = qr.save(EQuestion.builder().section(s_calendar).type(QuestionType.LABEL)
                                .title("https://jean-luc-massat.pedaweb.univ-amu.fr/ens/alternance-en-m2idl/").build());

                // TODO : rajouter les observations aux choix radio, comme dans le tableau
                EQuestion s_suivi_1_sem_1 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.LABEL)
                                .title(
                                                "Le présent document de suivi est à remplir par le Maître d’apprentissage en présence de l’apprenti. NB : ce document compte pour l'obtention du diplôme.")
                                .build());
                EQuestion s_suivi_1_sem_2 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.LABEL)
                                .title("Critères d'application.").build());
                EQuestion s_suivi_1_sem_3 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.RADIO)
                                .title("Efficacité au travail (ponctuel, opérationnel, actif, sens du travail bien fait...)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_1_sem_3).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_1_sem_3).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_1_sem_3).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_1_sem_3).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_1_sem_3).build());
                EQuestion s_suivi_1_sem_4 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.RADIO)
                                .title(
                                                "Compréhension et assimilation (niveau de connaissances, rapidité à intégrer des éléments nouveaux...)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_1_sem_4).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_1_sem_4).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_1_sem_4).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_1_sem_4).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_1_sem_4).build());
                EQuestion s_suivi_1_sem_5 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.RADIO)
                                .title("Curiosité d'esprit (dynamisme, initiative, anticipation...)").build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_1_sem_5).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_1_sem_5).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_1_sem_5).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_1_sem_5).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_1_sem_5).build());
                EQuestion s_suivi_1_sem_6 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.RADIO)
                                .title("Coopération (volonté d'apprendre, polyvalence, interroge ses formateurs)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_1_sem_6).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_1_sem_6).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_1_sem_6).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_1_sem_6).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_1_sem_6).build());
                EQuestion s_suivi_1_sem_7 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.RADIO)
                                .title(
                                                "Relations avec le personnel, esprit d'équipe (aptitude à communiquer, oral et écrit, sens du contact, intégration dans l'équipe)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_1_sem_7).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_1_sem_7).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_1_sem_7).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_1_sem_7).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_1_sem_7).build());
                EQuestion s_suivi_1_sem_8 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.RADIO)
                                .title(
                                                "Disponibilité (esprit d'équipe, prêt à rendre service sollicite travail et responsabilités, adaptabilité)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_1_sem_8).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_1_sem_8).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_1_sem_8).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_1_sem_8).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_1_sem_8).build());
                EQuestion s_suivi_1_sem_9 = qr.save(EQuestion.builder().section(s_suivi_1_sem).type(QuestionType.RADIO)
                                .title("APPRÉCIATION GÉNÉRALE").build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_1_sem_9).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_1_sem_9).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_1_sem_9).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_1_sem_9).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_1_sem_9).build());

                EQuestion q_observ_master_1_1 = qr.save(
                                EQuestion.builder().section(s_observ_master_1).type(QuestionType.TEXT)
                                                .title("Points forts").build());
                ar.save(EAnswer.builder().question(q_observ_master_1_1).build());
                EQuestion q_observ_master_1_2 = qr
                                .save(EQuestion.builder().section(s_observ_master_1).type(QuestionType.TEXT)
                                                .title("Points à améliorer").build());
                ar.save(EAnswer.builder().question(q_observ_master_1_2).build());

                // TODO : Pour les commentaires, les signatures
                EQuestion q_comment_master_1_1 = qr.save(EQuestion.builder().section(s_comment_master_1)
                                .type(QuestionType.LABEL)
                                .title("A la fin de chaque période en entreprise, l’apprenti-e doit formuler ci-après, ses remarques, ses questions, ses difficultés, en ce qui concerne :\n"
                                                + "• la définition des objectifs,\n" + "• les moyens mis en œuvre,\n"
                                                + "• l’adéquation entre le sujet proposé et l’apprentissage réel,\n"
                                                + "• les connaissances requises ou acquises,\n"
                                                + "• le vécu relationnel.")
                                .build());
                EQuestion q_comment_master_1_2 = qr
                                .save(EQuestion.builder().section(s_comment_master_1).type(QuestionType.TEXT)
                                                .title("Ecrire un commentaire").build());
                ar.save(EAnswer.builder().question(q_comment_master_1_2).build());

                EQuestion q_comment_stud_1_1 = qr.save(EQuestion.builder().section(s_comment_stud_1)
                                .type(QuestionType.LABEL)
                                .title("A la fin de chaque période en entreprise, l’apprenti-e doit formuler ci-après, ses remarques, ses questions, ses difficultés, en ce qui concerne :\n"
                                                + "• la définition des objectifs,\n" + "• les moyens mis en œuvre,\n"
                                                + "• l’adéquation entre le sujet proposé et l’apprentissage réel,\n"
                                                + "• les connaissances requises ou acquises,\n"
                                                + "• le vécu relationnel.")
                                .build());
                EQuestion q_comment_stud_1_2 = qr
                                .save(EQuestion.builder().section(s_comment_stud_1).type(QuestionType.TEXT)
                                                .title("Ecrire un commentaire").build());
                ar.save(EAnswer.builder().question(q_comment_stud_1_2).build());

                EQuestion q_comment_tutor_1_1 = qr.save(EQuestion.builder().section(s_comment_tutor_1)
                                .type(QuestionType.LABEL)
                                .title("A la fin de chaque période en entreprise, l’apprenti-e doit formuler ci-après, ses remarques, ses questions, ses difficultés, en ce qui concerne :\n"
                                                + "• la définition des objectifs,\n" + "• les moyens mis en œuvre,\n"
                                                + "• l’adéquation entre le sujet proposé et l’apprentissage réel,\n"
                                                + "• les connaissances requises ou acquises,\n"
                                                + "• le vécu relationnel.")
                                .build());
                EQuestion q_comment_tutor_1_2 = qr
                                .save(EQuestion.builder().section(s_comment_tutor_1).type(QuestionType.TEXT)
                                                .title("Ecrire un commentaire").build());
                ar.save(EAnswer.builder().question(q_comment_tutor_1_2).build());

                // TODO : rajouter les observations aux choix radio, comme dans le tableau
                EQuestion s_suivi_2_sem_1 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.LABEL)
                                .title(
                                                "Le présent document de suivi est à remplir par le Maître d’apprentissage en présence de l’apprenti. NB : ce document compte pour l'obtention du diplôme.")
                                .build());
                EQuestion s_suivi_2_sem_2 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.LABEL)
                                .title("Critères d'application.").build());
                EQuestion s_suivi_2_sem_3 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.RADIO)
                                .title("Efficacité au travail (ponctuel, opérationnel, actif, sens du travail bien fait...)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_2_sem_3).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_2_sem_3).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_2_sem_3).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_2_sem_3).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_2_sem_3).build());
                EQuestion s_suivi_2_sem_4 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.RADIO)
                                .title(
                                                "Compréhension et assimilation (niveau de connaissances, rapidité à intégrer des éléments nouveaux...)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_2_sem_4).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_2_sem_4).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_2_sem_4).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_2_sem_4).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_2_sem_4).build());
                EQuestion s_suivi_2_sem_5 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.RADIO)
                                .title("Curiosité d'esprit (dynamisme, initiative, anticipation...)").build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_2_sem_5).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_2_sem_5).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_2_sem_5).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_2_sem_5).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_2_sem_5).build());
                EQuestion s_suivi_2_sem_6 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.RADIO)
                                .title("Coopération (volonté d'apprendre, polyvalence, interroge ses formateurs)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_2_sem_6).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_2_sem_6).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_2_sem_6).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_2_sem_6).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_2_sem_6).build());
                EQuestion s_suivi_2_sem_7 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.RADIO)
                                .title(
                                                "Relations avec le personnel, esprit d'équipe (aptitude à communiquer, oral et écrit, sens du contact, intégration dans l'équipe)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_2_sem_7).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_2_sem_7).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_2_sem_7).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_2_sem_7).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_2_sem_7).build());
                EQuestion s_suivi_2_sem_8 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.RADIO)
                                .title(
                                                "Disponibilité (esprit d'équipe, prêt à rendre service sollicite travail et responsabilités, adaptabilité)")
                                .build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_2_sem_8).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_2_sem_8).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_2_sem_8).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_2_sem_8).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_2_sem_8).build());
                EQuestion s_suivi_2_sem_9 = qr.save(EQuestion.builder().section(s_suivi_2_sem).type(QuestionType.RADIO)
                                .title("APPRÉCIATION GÉNÉRALE").build());
                ar.save(EAnswer.builder().proposition("A - 16/20").question(s_suivi_2_sem_9).build());
                ar.save(EAnswer.builder().proposition("B - 14/20").question(s_suivi_2_sem_9).build());
                ar.save(EAnswer.builder().proposition("C - 12/20").question(s_suivi_2_sem_9).build());
                ar.save(EAnswer.builder().proposition("D - 10/20").question(s_suivi_2_sem_9).build());
                ar.save(EAnswer.builder().proposition("E - 08/20").question(s_suivi_2_sem_9).build());

                EQuestion q_observ_master_2_1 = qr.save(
                                EQuestion.builder().section(s_observ_master_2).type(QuestionType.TEXT)
                                                .title("Points forts").build());
                ar.save(EAnswer.builder().question(q_observ_master_2_1).build());
                EQuestion q_observ_master_2_2 = qr
                                .save(EQuestion.builder().section(s_observ_master_2).type(QuestionType.TEXT)
                                                .title("Points à améliorer").build());
                ar.save(EAnswer.builder().question(q_observ_master_2_2).build());

                // TODO : Pour les commentaires, les signatures
                EQuestion q_comment_master_2_1 = qr.save(EQuestion.builder().section(s_comment_master_2)
                                .type(QuestionType.LABEL)
                                .title("A la fin de chaque période en entreprise, l’apprenti-e doit formuler ci-après, ses remarques, ses questions,\n"
                                                + "ses difficultés, en ce qui concerne :\n"
                                                + "• la définition des objectifs,\n"
                                                + "• les moyens mis en œuvre,\n"
                                                + "• l’adéquation entre le sujet proposé et l’apprentissage réel,\n"
                                                + "• les connaissances requises ou acquises,\n"
                                                + "• le vécu relationnel.")
                                .build());
                EQuestion q_comment_master_2_2 = qr
                                .save(EQuestion.builder().section(s_comment_master_2).type(QuestionType.TEXT)
                                                .title("Ecrire un commentaire").build());
                ar.save(EAnswer.builder().question(q_comment_master_2_2).build());

                EQuestion q_comment_stud_2_1 = qr.save(EQuestion.builder().section(s_comment_stud_2)
                                .type(QuestionType.LABEL)
                                .title("A la fin de chaque période en entreprise, l’apprenti-e doit formuler ci-après, ses remarques, ses questions,\n"
                                                + "ses difficultés, en ce qui concerne :\n"
                                                + "• la définition des objectifs,\n"
                                                + "• les moyens mis en œuvre,\n"
                                                + "• l’adéquation entre le sujet proposé et l’apprentissage réel,\n"
                                                + "• les connaissances requises ou acquises,\n"
                                                + "• le vécu relationnel.")
                                .build());
                EQuestion q_comment_stud_2_2 = qr
                                .save(EQuestion.builder().section(s_comment_stud_2).type(QuestionType.TEXT)
                                                .title("Ecrire un commentaire").build());
                ar.save(EAnswer.builder().question(q_comment_stud_2_2).build());

                EQuestion q_comment_tutor_2_1 = qr.save(EQuestion.builder().section(s_comment_tutor_2)
                                .type(QuestionType.LABEL)
                                .title("A la fin de chaque période en entreprise, l’apprenti-e doit formuler ci-après, ses remarques, ses questions,\n"
                                                + "ses difficultés, en ce qui concerne :\n"
                                                + "• la définition des objectifs,\n"
                                                + "• les moyens mis en œuvre,\n"
                                                + "• l’adéquation entre le sujet proposé et l’apprentissage réel,\n"
                                                + "• les connaissances requises ou acquises,\n"
                                                + "• le vécu relationnel.")
                                .build());
                EQuestion q_comment_tutor_2_2 = qr
                                .save(EQuestion.builder().section(s_comment_tutor_2).type(QuestionType.TEXT)
                                                .title("Ecrire un commentaire").build());
                ar.save(EAnswer.builder().question(q_comment_tutor_2_2).build());

                EQuestion q_eval_student_1 = qr
                                .save(EQuestion.builder().section(s_eval_student).type(QuestionType.LABEL)
                                                .title("1) Évaluation des enseignements par les apprentis").build());
                EQuestion q_eval_student_2 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.LABEL).title(
                                                "Le responsable de la formation doit mettre en œuvre un questionnaire permettant aux apprentis d’évaluer les enseignements (il existe des modèles disponibles à FORMASUP PACA/CFA Épure Méditerranée).")
                                .build());
                EQuestion q_eval_student_3 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.LABEL)
                                .title("2) Enquête de satisfaction des apprentis (enquête FORMASUP PACA/CFA Épure Méditerranée)")
                                .build());
                EQuestion q_eval_student_4 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.LABEL).title(
                                                "Entre mars et juillet, les apprentis doivent se connecter sur la plateforme collaborative du CFA pour répondre aux enquêtes à l’adresse URL : https://espace.cfa-epure.com/")
                                .build());
                EQuestion q_eval_student_5 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.LABEL).title(
                                                "Chaque responsable de formation à accès au questionnaire de FORMASUP PACA/CFA Épure Méditerranée en se connectant sur la plateforme collaborative.")
                                .build());
                EQuestion q_eval_student_6 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.LABEL)
                                .title("3) Questionnaire de fin d’année à l’attention des apprentis (modèle !)")
                                .build());
                EQuestion q_eval_student_7 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.RADIO)
                                .title("Les connaissances acquises vous semblent-elles utilisables en pratique ?")
                                .build());
                ar.save(EAnswer.builder().proposition("Oui").question(q_eval_student_7).build());
                ar.save(EAnswer.builder().proposition("Non").question(q_eval_student_7).build());
                EQuestion q_eval_student_8 = qr
                                .save(EQuestion.builder().section(s_eval_student).type(QuestionType.RADIO)
                                                .title("Le rythme d'alternance vous paraît-il satisfaisant ?").build());
                ar.save(EAnswer.builder().proposition("Oui").question(q_eval_student_8).build());
                ar.save(EAnswer.builder().proposition("Non").question(q_eval_student_8).build());
                EQuestion q_eval_student_9 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.RADIO).title(
                                                "Êtes-vous satisfait du secrétariat de la formation en alternance et de la communication des informations ?")
                                .build());
                ar.save(EAnswer.builder().proposition("Oui").question(q_eval_student_9).build());
                ar.save(EAnswer.builder().proposition("Non").question(q_eval_student_9).build());
                EQuestion q_eval_student_10 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.RADIO)
                                .title("Êtes-vous satisfait des locaux et équipements mis à disposition par l'Université ?")
                                .build());
                ar.save(EAnswer.builder().proposition("Oui").question(q_eval_student_10).build());
                ar.save(EAnswer.builder().proposition("Non").question(q_eval_student_10).build());
                EQuestion q_eval_student_11 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.RADIO)
                                .title("Êtes-vous satisfait des services associés (restauration universitaire, logement étudiant, bibliothèque...) ?")
                                .build());
                ar.save(EAnswer.builder().proposition("Oui").question(q_eval_student_11).build());
                ar.save(EAnswer.builder().proposition("Non").question(q_eval_student_11).build());
                EQuestion q_eval_student_12 = qr.save(EQuestion.builder().section(s_eval_student)
                                .type(QuestionType.RADIO)
                                .title("Avez-vous rencontré des difficultés autres durant l'année universitaire ?")
                                .build());
                ar.save(EAnswer.builder().proposition("Oui").question(q_eval_student_12).build());
                ar.save(EAnswer.builder().proposition("Non").question(q_eval_student_12).build());
                EQuestion q_eval_student_13 = qr
                                .save(EQuestion.builder().section(s_eval_student).type(QuestionType.TEXT)
                                                .title("Commentaire général").build());
                ar.save(EAnswer.builder().question(q_eval_student_13).build());

                EQuestion q_eval_company_1 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.LABEL)
                                                .title("Contenu et organisation").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_1).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_1).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_1).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_1).build());
                EQuestion q_eval_company_2 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Apport des enseignements en rapport avec le métier").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_2).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_2).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_2).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_2).build());
                EQuestion q_eval_company_3 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Rythme de l'alternance").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_3).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_3).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_3).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_3).build());
                EQuestion q_eval_company_4 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.LABEL)
                                                .title("Relation avec la Formation").build());
                EQuestion q_eval_company_5 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Informations par courrier, mails, téléphone").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_5).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_5).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_5).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_5).build());
                EQuestion q_eval_company_6 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Relation administratives").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_6).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_6).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_6).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_6).build());
                EQuestion q_eval_company_7 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Rencontre avec la formation").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_7).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_7).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_7).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_7).build());
                EQuestion q_eval_company_8 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.LABEL)
                                                .title("Suivi de l’apprenti").build());
                EQuestion q_eval_company_9 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Communication par le livret d'alternance").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_9).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_9).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_9).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_9).build());
                EQuestion q_eval_company_10 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Visites du tuteur universitaire").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_10).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_10).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_10).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_10).build());
                EQuestion q_eval_company_11 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.LABEL)
                                                .title("Évaluation globale").build());
                EQuestion q_eval_company_12 = qr
                                .save(EQuestion.builder().section(s_eval_company).type(QuestionType.RADIO)
                                                .title("Sur l'année universitaire").build());
                ar.save(EAnswer.builder().proposition("Très satisfait").question(q_eval_company_12).build());
                ar.save(EAnswer.builder().proposition("Satisfait").question(q_eval_company_12).build());
                ar.save(EAnswer.builder().proposition("Peu satisfait").question(q_eval_company_12).build());
                ar.save(EAnswer.builder().proposition("Pas satisfait").question(q_eval_company_12).build());
                EQuestion q_eval_company_13 = qr.save(
                                EQuestion.builder().section(s_eval_company).type(QuestionType.TEXT)
                                                .title("Commentaires").build());
                ar.save(EAnswer.builder().question(q_eval_company_13).build());

                // TODO : inclure les dates et les signatures dans l'application
        }
}
