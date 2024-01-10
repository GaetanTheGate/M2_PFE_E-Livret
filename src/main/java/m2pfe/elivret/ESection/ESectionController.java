package m2pfe.elivret.ESection;

import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.EQuestion.AbstractEQuestion;
import m2pfe.elivret.EUser.EUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Authentification.EntityAccessAuthorization;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
/**
 * <p>
 * Rest controller for the ESection entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/sections".
 * </p>
 *
 * @see ESection
 *
 * @author Gaëtan PUPET
 * @version 1.1
 */
@RestController
@RequestMapping("/api/sections")
public class ESectionController {
    /**
     * The service used to check the authenticated user's rights.
     */
    // TODO : Supprimer
    // @Autowired
    // private EntityAccessAuthorization authorization;
    /**
     * The service to authenticate the user.
     */
    @Autowired
    private AuthentificationService service;
    /**
     * Respository for the ESections.
     */
    @Autowired
    private ESectionRepository s_repo;
    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @Deprecated
    @GetMapping("/getAll")
    public List<ESectionDTO.Out.AllPublic> getSections() {
        return s_repo.findAll().stream().map(s -> mapper.map(s, ESectionDTO.Out.AllPublic.class))
                .toList();
    }

    @GetMapping("/livret/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, #id)")
    public List<ESection> getLivretSection(@PathVariable Integer id, HttpServletRequest req) throws AuthentificationException, ESectionException {
        // TODO : faire que les sections du livrets soit renvoyer, si la requete est nécéssaire.
        return null;
    }

    // TODO : supprimer
    @Deprecated
    @GetMapping("")
    public List<ESectionDTO.Out.AllPublic> getMySections(HttpServletRequest req) throws AuthentificationException, ESectionException {
        EUser me = service.whoAmI(req);
        return s_repo.findByUser(me).stream().map(s -> mapper.map(s, ESectionDTO.Out.AllPublic.class)).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, @SectionRepository.getById(#id))")
    public ESectionDTO.Out.AllPublic getSection(@PathVariable int id, HttpServletRequest req) throws AuthentificationException, ESectionException {
        // if(!authorization.isMeFromLivret(req, s_repo.getById(id).getLivret().getId())) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        Optional<ESection> s = s_repo.findById(id);
        s.orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section not found."));

        return mapper.map(s.get(), ESectionDTO.Out.AllPublic.class);
    }


    /// PostMapping

    @PostMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req.getLivret(), #section)")
    public ESection postSection(@RequestBody ESection section, HttpServletRequest req) throws AuthentificationException, ESectionException {
        // if(!authorization.isMeFromLivret(req, section.getLivret().getId())) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        // EUser me = service.whoAmI(req);
        // if(me.getRole() != ELivret.UserRole.RESPONSABLE){
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        ESection s = mapper.map(section, ESection.class);

        Optional.ofNullable(s_repo.findById(s.getId()).isPresent() ? null : s)
            .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section already exist."));

        return s_repo.save(s);
    }


    /// PutMapping

    // TODO : Faire avec spring security toute la fonction
    @PutMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req.getLivret(), #section)")
    public ESection putSection(@RequestBody ESection section, HttpServletRequest req) throws AuthentificationException, ESectionException {
        // if(!authorization.isMeFromLivret(req, section.getLivret().getId())) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        EUser me = service.whoAmI(req);
        /** Tutor ne peux modifier que la visibilité d'une section **/
        if(me.getRole() == ELivret.UserRole.TUTOR){
            ESection sNew = mapper.map(section, ESection.class);

            ESection sOld = s_repo.findById(sNew.getId())
                    .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section not found."));
            sOld.setVisibility(sNew.getVisibility());

            return s_repo.save(sOld);
        }
        if(me.getRole() != ELivret.UserRole.RESPONSABLE){
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        ESection s = mapper.map(section, ESection.class);

        s_repo.findById(s.getId())
            .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section not found."));

        return s_repo.save(s);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req.getLivret(), @SectionRepository.getById(#id))")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable int id, HttpServletRequest req) throws AuthentificationException {
        // if(!authorization.isMeFromLivret(req, s_repo.getById(id).getLivret().getId())) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        // EUser me = service.whoAmI(req);
        // if(me.getRole() != ELivret.UserRole.RESPONSABLE){
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        s_repo.deleteById(id);
    }
}
