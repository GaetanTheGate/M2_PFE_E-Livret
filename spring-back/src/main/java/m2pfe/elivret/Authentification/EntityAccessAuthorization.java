package m2pfe.elivret.Authentification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EAnswer.EAnswerException;
import m2pfe.elivret.EAnswer.EAnswerRepository;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivretException;
import m2pfe.elivret.ELivret.ELivretRepository;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.EQuestion;
import m2pfe.elivret.EQuestion.EQestionRepository;
import m2pfe.elivret.EQuestion.EQuestionException;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.ESection.ESectionException;
import m2pfe.elivret.ESection.ESectionRepository;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserException;
import m2pfe.elivret.EUser.EUserRepository;

/**
 * <p>
 * Component for checking if an authentificated user owns an entity or not.
 * </p>
 * 
 * @see AuthentificationService
 * @see HttpServletRequest
 * @see EUser
 * @see ELivret
 * @see ESection
 * @see EQuestion
 * @see EAnswer
 * @see EUserRepository
 * @see ELivretRepository
 * @see ESectionRepository
 * @see EQuestionRepository
 * @see EAnswerRepository
 * 
 * @author Gaëtan PUPET
 * @version 1.3
 */
@Component(value = "EntityAccessAuthorization")
public class EntityAccessAuthorization {
        /**
         * Service used to authentificate the user.
         */
        @Autowired
        private AuthentificationService service;

        /**
         * Repository for the users.
         */
        @Autowired
        private EUserRepository ur;

        /**
         * Repository for the livrets
         */
        @Autowired
        private ELivretRepository lr;

        /**
         * Repository for the livrets's sections.
         */
        @Autowired
        private ESectionRepository sr;

        /**
         * Repository for the questions in a section.
         */
        @Autowired
        private EQestionRepository qr;

        /**
         * Repository for the answers in a question.
         */
        @Autowired
        private EAnswerRepository ar;

        /**
         * <p>
         * Tells if the authenticated user matches the user's id.
         * </p>
         * 
         * @see HttpServletRequest
         * @see EUser
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The id of the user to match.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * @throws AuthentificationException
         * @throws EUserException
         */
        public boolean isUserMe(HttpServletRequest req, Integer id) throws AuthentificationException, EUserException {
                EUser ru = service.whoAmI(req);
                EUser iu = ur.findById(id)
                                .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT, "User's id not found."));

                return ru.getId() == iu.getId();
        }

        /**
         * <p>
         * Tells if the authenticated user matches the user.
         * </p>
         * 
         * @see HttpServletRequest
         * @see EUser
         * @see #isUserMe(HttpServletRequest, Integer)
         * 
         * @param req  The request where to find the token of the authenticated user.
         * @param user The user to match.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         */
        public boolean isUserMe(HttpServletRequest req, EUser user) throws AuthentificationException, EUserException {
                return isUserMe(req, user.getId());
        }

        /**
         * <p>
         * Tells if the authenticated user matches an user from a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The id of the livret to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         */
        public boolean isMeFromLivret(HttpServletRequest req, Integer id)
                        throws AuthentificationException, EUserException, ELivretException {
                EUser ru = service.whoAmI(req);

                ELivret l = lr.findById(id)
                                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT,
                                                "Livret's id not found."));

                int exception_counter = UserRole.values().length;
                /** Loop for no more useful when we add role in EUser **/
                for (UserRole role : UserRole.values()) {

                        try {
                                EUser lu = ur.findById(l.getUserFromRole(role).getId())
                                                .orElseThrow();

                                if (ru.getId() == lu.getId())
                                        return true;
                        } catch (Exception e) {
                                exception_counter -= 1;
                                if (exception_counter == 0)
                                        throw new EUserException(HttpStatus.NO_CONTENT,
                                                        "No users found for each of the livret's users value.");
                        }
                }

                return false;
        }

        /**
         * <p>
         * Tells if the authenticated user matches an user from a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isMeFromLivret(HttpServletRequest, Integer)
         * 
         * @param req    The request where to find the token of the authenticated user.
         * @param livret The livret to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         */
        public boolean isMeFromLivret(HttpServletRequest req, ELivret livret)
                        throws AuthentificationException, EUserException, ELivretException {
                return isMeFromLivret(req, livret.getId());
        }

        /**
         * <p>
         * Tells if the authenticated user matches an user from a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isMeFromLivret(HttpServletRequest, Integer)
         * 
         * @param req     The request where to find the token of the authenticated user.
         * @param section The section to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         */
        public boolean isMeFromLivret(HttpServletRequest req, ESection section)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException {
                return isMeFromLivret(req, sr.findById(section.getId())
                                .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT,
                                                "Section's id not found."))
                                .getLivret());
        }

        /**
         * <p>
         * Tells if the authenticated user matches an user from a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isMeFromLivret(HttpServletRequest, Integer)
         * 
         * @param req      The request where to find the token of the authenticated
         *                 user.
         * @param question The question to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         */
        public boolean isMeFromLivret(HttpServletRequest req, EQuestion question)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException,
                        EQuestionException {
                return isMeFromLivret(req, qr.findById(question.getId())
                                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT,
                                                "Question's id not found."))
                                .getSection());
        }

        /**
         * <p>
         * Tells if the authenticated user matches an user from a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isMeFromLivret(HttpServletRequest, Integer)
         * 
         * @param req    The request where to find the token of the authenticated user.
         * @param answer The answer to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         * @throws EAnswerException
         */
        public boolean isMeFromLivret(HttpServletRequest req, EAnswer answer) throws AuthentificationException,
                        EUserException, ELivretException, ESectionException, EQuestionException, EAnswerException {
                return isMeFromLivret(req, ar.findById(answer.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT,
                                                "Answer's id not found."))
                                .getQuestion());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The id of the Livret to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         */
        public boolean isLivretMine(HttpServletRequest req, Integer id)
                        throws AuthentificationException, EUserException, ELivretException {
                EUser ru = service.whoAmI(req);

                ELivret l = lr.findById(id)
                                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT,
                                                "Livret's id not found."));

                EUser lu = ur.findById(l.getUserFromRole(ELivret.UserRole.RESPONSABLE).getId())
                                .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT,
                                                "Livret's owner not found."));

                return ru.getId() == lu.getId();
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isLivretMine(HttpServletRequest, Integer)
         * 
         * @param req    The request where to find the token of the authenticated user.
         * @param livret The livret to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         */
        public boolean isLivretMine(HttpServletRequest req, ELivret livret)
                        throws AuthentificationException, EUserException, ELivretException {
                return isLivretMine(req, livret.getId());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isLivretMine(HttpServletRequest, Integer)
         * 
         * @param req     The request where to find the token of the authenticated user.
         * @param section The section to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         */
        public boolean isLivretMine(HttpServletRequest req, ESection section)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException {
                return isLivretMine(req, sr.findById(section.getId())
                                .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT,
                                                "Section's id not found."))
                                .getLivret());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isLivretMine(HttpServletRequest, Integer)
         * 
         * @param req      The request where to find the token of the authenticated
         *                 user.
         * @param question The question to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         */
        public boolean isLivretMine(HttpServletRequest req, EQuestion question)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException,
                        EQuestionException {
                return isLivretMine(req, qr.findById(question.getId())
                                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT,
                                                "Question's id not found."))
                                .getSection());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #isLivretMine(HttpServletRequest, Integer)
         * 
         * @param req    The request where to find the token of the authenticated user.
         * @param answer The answer to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         * @throws EAnswerException
         */
        public boolean isLivretMine(HttpServletRequest req, EAnswer answer) throws AuthentificationException,
                        EUserException, ELivretException, ESectionException, EQuestionException, EAnswerException {
                return isLivretMine(req, ar.findById(answer.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT,
                                                "Answer's id not found."))
                                .getQuestion());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of the section.
         * </p>
         * 
         * @see HttpServletRequest
         * @see Esection
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The id of the section to check.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         */
        public boolean isSectionMine(HttpServletRequest req, Integer id)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException {
                EUser ru = service.whoAmI(req);

                ESection s = sr.findById(id)
                                .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT,
                                                "Section's id not found."));

                ELivret l = lr.findById(s.getLivret().getId())
                                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT,
                                                "Livret's id not found."));

                EUser su = ur.findById(l.getUserFromRole(s.getOwner()).getId())
                                .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT, "User's id not found."));

                return ru.getId() == su.getId();
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of the section.
         * </p>
         * 
         * @see HttpServletRequest
         * @see Esection
         * 
         * @param req     The request where to find the token of the authenticated user.
         * @param section The section to check.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         */
        public boolean isSectionMine(HttpServletRequest req, ESection section)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException {
                return isSectionMine(req, section.getId());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of the question.
         * </p>
         * 
         * @see HttpServletRequest
         * @see EQuestion
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The of the question to check.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         */
        public boolean isQuestionMine(HttpServletRequest req, Integer id)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException,
                        EQuestionException {
                EQuestion question = qr.findById(id)
                                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT,
                                                "Question's id not found."));

                return isSectionMine(req, question.getSection());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of the question.
         * </p>
         * 
         * @see HttpServletRequest
         * @see EQuestion
         * 
         * @param req      The request where to find the token of the authenticated
         *                 user.
         * @param question The question to check.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         */
        public boolean isQuestionMine(HttpServletRequest req, EQuestion question)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException,
                        EQuestionException {
                return isQuestionMine(req, question.getId());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of the answer.
         * </p>
         * 
         * @see HttpServletRequest
         * @see EAnswer
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The id of the answer to check.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         * @throws EAnswerException
         */
        public boolean isAnswerMine(HttpServletRequest req, Integer id)
                        throws AuthentificationException, EUserException,
                        ELivretException, ESectionException, EQuestionException, EAnswerException {
                EAnswer answer = ar.findById(id)
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT,
                                                "Answer's id not found."));

                return isQuestionMine(req, answer.getQuestion());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the owner of the answer.
         * </p>
         * 
         * @see HttpServletRequest
         * @see EAnswer
         * 
         * @param req    The request where to find the token of the authenticated user.
         * @param answer The answer to check.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         * @throws EAnswerException
         */
        public boolean isAnswerMine(HttpServletRequest req, EAnswer answer) throws AuthentificationException,
                        EUserException, ELivretException, ESectionException, EQuestionException, EAnswerException {
                return isAnswerMine(req, answer.getId());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the tutor of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The id of the Livret to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         */
        public boolean amITutor(HttpServletRequest req, Integer id)
                        throws AuthentificationException, EUserException, ELivretException {
                EUser ru = service.whoAmI(req);

                ELivret l = lr.findById(id)
                                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT,
                                                "Livret's id not found."));

                EUser lu = ur.findById(l.getUserFromRole(ELivret.UserRole.TUTOR).getId())
                                .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT,
                                                "Livret's tutor not found."));

                return ru.getId() == lu.getId();
        }

        /**
         * <p>
         * Tells if the authenticated user matches the tutor of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #amITutor(HttpServletRequest, Integer)
         * 
         * @param req    The request where to find the token of the authenticated user.
         * @param livret The livret to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         */
        public boolean amITutor(HttpServletRequest req, ELivret livret)
                        throws AuthentificationException, EUserException, ELivretException {
                return amITutor(req, livret.getId());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the tutor of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #amITutor(HttpServletRequest, Integer)
         * 
         * @param req     The request where to find the token of the authenticated user.
         * @param section The section to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         */
        public boolean amITutor(HttpServletRequest req, ESection section)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException {
                return amITutor(req, sr.findById(section.getId())
                                .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT,
                                                "Section's id not found."))
                                .getLivret());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the tutor of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #amITutor(HttpServletRequest, Integer)
         * 
         * @param req The request where to find the token of the authenticated user.
         * @param id  The section to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         */
        public boolean amITutorFromSection(HttpServletRequest req, Integer id)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException {
                return amITutor(req, sr.findById(id)
                                .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT,
                                                "Section's id not found."))
                                .getLivret());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the tutor of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #amITutor(HttpServletRequest, Integer)
         * 
         * @param req      The request where to find the token of the authenticated
         *                 user.
         * @param question The question to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         */
        public boolean amITutor(HttpServletRequest req, EQuestion question)
                        throws AuthentificationException, EUserException, ELivretException, ESectionException,
                        EQuestionException {
                return amITutor(req, qr.findById(question.getId())
                                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT,
                                                "Question's id not found."))
                                .getSection());
        }

        /**
         * <p>
         * Tells if the authenticated user matches the tutor of a livret.
         * </p>
         * 
         * @see HttpServletRequest
         * @see ELivret
         * @see #amITutor(HttpServletRequest, Integer)
         * 
         * @param req    The request where to find the token of the authenticated user.
         * @param answer The answer to check from.
         * @return <i>true</i> if it matches, <i>false</i> otherwise.
         * 
         * @throws AuthentificationException
         * @throws EUserException
         * @throws ELivretException
         * @throws ESectionException
         * @throws EQuestionException
         * @throws EAnswerException
         */
        public boolean amITutor(HttpServletRequest req, EAnswer answer) throws AuthentificationException,
                        EUserException, ELivretException, ESectionException, EQuestionException, EAnswerException {
                return amITutor(req, ar.findById(answer.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT,
                                                "Answer's id not found."))
                                .getQuestion());
        }
}