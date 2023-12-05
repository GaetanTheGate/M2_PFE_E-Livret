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

// TODO: COMMENTS.
@Service
public class EntityAccessAuthorization {
    @Autowired
    private AuthentificationService service;

    @Autowired
    private EUserRepository ur;

    @Autowired
    private ELivretRepository lr;

    @Autowired
    private ESectionRepository sr;

    public boolean isUserMe(HttpServletRequest req, Integer id) throws AuthentificationException, EUserException {
        EUser ru = service.whoAmI(req);
        EUser iu = ur.findById(id)
            .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT, "User's id not found."));

        return ru.getId() == iu.getId();
    }

    public boolean isUserMe(HttpServletRequest req, EUser user) throws EUserException{
        return isUserMe(req, user.getId());
    }

    // public boolean isLivretMine(HttpServletRequest req, Integer id) throws AuthentificationException, {
    //     EUser ru = service.whoAmI(req);

    //     return ru.getId() == iu.getId();
    // }

    // public boolean isLivretMine(HttpServletRequest req, ELivret livret) throws AuthentificationException, {

    //     return isLivretMine(req, livret.getId());
    // }

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

    public boolean isMeFromLivret(HttpServletRequest req, ELivret livret) throws AuthentificationException, EUserException, ELivretException {
        return isMeFromLivret(req, livret.getId());
    }

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

    public boolean isSectionMine(HttpServletRequest req, ESection section) throws AuthentificationException, EUserException, ELivretException, ESectionException {
        return isSectionMine(req, section.getId());
    }

}
