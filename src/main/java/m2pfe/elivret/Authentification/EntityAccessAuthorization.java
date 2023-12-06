package m2pfe.elivret.Authentification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivretException;
import m2pfe.elivret.ELivret.ELivretRepository;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.ESection.ESectionException;
import m2pfe.elivret.ESection.ESectionRepository;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserException;
import m2pfe.elivret.EUser.EUserRepository;

/**
 * <p>
 * Service for checking if an authentificated user owns an entity or not.
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
 * @version 1.0
 */
@Service
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
     * <p>
     * Tells if the authenticated user matches the user's id.
     * </p>
     * 
     * @see HttpServletRequest
     * @see EUser
     * 
     * @param req The request where to find the token of the authenticated user.
     * @param id The id of the user to match.
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
     * @param req The request where to find the token of the authenticated user.
     * @param user The user to match.
     * @return <i>true</i> if it matches, <i>false</i> otherwise.
     * @throws EUserException
     */
    public boolean isUserMe(HttpServletRequest req, EUser user) throws EUserException {
        return isUserMe(req, user.getId());
    }

    // TODO: décommenter et compléter quand il y aura un owner à un livret
    // public boolean isLivretMine(HttpServletRequest req, Integer id) throws AuthentificationException, {
    //     EUser ru = service.whoAmI(req);

    //     return ru.getId() == iu.getId();
    // }

    // public boolean isLivretMine(HttpServletRequest req, ELivret livret) throws AuthentificationException, {

    //     return isLivretMine(req, livret.getId());
    // }

    /**
     * <p>
     * Tells if the authenticated user matches an user from a livret.
     * </p>
     * 
     * @see HttpServletRequest
     * @see ELivret
     * 
     * @param req The request where to find the token of the authenticated user.
     * @param id The id of the livret to check from.
     * @return <i>true</i> if it matches, <i>false</i> otherwise.
     * @throws AuthentificationException
     * @throws EUserException
     * @throws ELivretException
     */
    public boolean isMeFromLivret(HttpServletRequest req, Integer id) throws AuthentificationException, EUserException, ELivretException {
        EUser ru = service.whoAmI(req);

        ELivret l = lr.findById(id)
            .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret's id not found."));

        int exception_counter = UserRole.values().length;
        for(UserRole role : UserRole.values()){

            try {
                EUser lu = ur.findById(l.getUserFromRole(role).getId())
                    .orElseThrow();

                if(ru.getId() == lu.getId())
                    return true;
            } catch (Exception e) {
                exception_counter-=1;
                if(exception_counter == 0)
                    throw new EUserException(HttpStatus.NO_CONTENT, "No users found for each of the livret's users value.");
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
     * @param req The request where to find the token of the authenticated user.
     * @param livret The livret to check from.
     * @return <i>true</i> if it matches, <i>false</i> otherwise.
     * @throws AuthentificationException
     * @throws EUserException
     * @throws ELivretException
     */
    public boolean isMeFromLivret(HttpServletRequest req, ELivret livret) throws AuthentificationException, EUserException, ELivretException {
        return isMeFromLivret(req, livret.getId());
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
     * @param id The id of the section to check.
     * @return <i>true</i> if it matches, <i>false</i> otherwise.
     * @throws AuthentificationException
     * @throws EUserException
     * @throws ELivretException
     * @throws ESectionException
     */
    public boolean isSectionMine(HttpServletRequest req, Integer id) throws AuthentificationException, EUserException, ELivretException, ESectionException {
        EUser ru = service.whoAmI(req);

        ESection s = sr.findById(id)
            .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section's id not found."));

        ELivret l = lr.findById(s.getLivret().getId())
            .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret's id not found."));

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
     * @param req The request where to find the token of the authenticated user.
     * @param section The section to check.
     * @return <i>true</i> if it matches, <i>false</i> otherwise.
     * @throws AuthentificationException
     * @throws EUserException
     * @throws ELivretException
     * @throws ESectionException
     */
    public boolean isSectionMine(HttpServletRequest req, ESection section) throws AuthentificationException, EUserException, ELivretException, ESectionException {
        return isSectionMine(req, section.getId());
    }

}
