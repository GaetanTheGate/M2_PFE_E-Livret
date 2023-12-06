package m2pfe.elivret.ESection;

import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.EUser.EUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Authentification.EntityAccessAuthorization;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/sections")
public class ESectionController {
    @Autowired
    private EntityAccessAuthorization authorization;

    @Autowired
    private AuthentificationService service;

    @Autowired
    private ESectionRepository s_repo;

    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @Deprecated
    @GetMapping("/getAll")
    public List<ESection> getSections() {
        return s_repo.findAll().stream().map(s -> mapper.map(s, ESection.class))
                .toList();
    }

    @GetMapping("/livret/{id}")
    public List<ESection> getLivretSection(@PathVariable Integer id, HttpServletRequest req) throws AuthentificationException, ESectionException {

        return null;
    }

    @GetMapping("")
    public List<ESection> getMySections(HttpServletRequest req) throws AuthentificationException, ESectionException {
        EUser me = service.whoAmI(req);
        return s_repo.findByUser(me).stream().map(s -> mapper.map(s, ESection.class)).toList();
    }

    @GetMapping("/{id}")
    public ESection getSection(@PathVariable int id, HttpServletRequest req) throws AuthentificationException, ESectionException {
        if(!authorization.isMeFromLivret(req, s_repo.getById(id).getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        Optional<ESection> s = s_repo.findById(id);
        s.orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section not found."));

        return mapper.map(s.get(), ESection.class);
    }


    /// PostMapping

    @PostMapping("")
    public ESection postSection(@RequestBody ESection section, HttpServletRequest req) throws AuthentificationException, ESectionException {
        if(!authorization.isMeFromLivret(req, section.getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        EUser me = service.whoAmI(req);
        if(me.getRole() != ELivret.UserRole.RESPONSABLE){
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        ESection s = mapper.map(section, ESection.class);

        Optional.ofNullable(s_repo.findById(s.getId()).isPresent() ? null : s)
            .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section already exist."));

        return s_repo.save(s);
    }


    /// PutMapping

    @PutMapping("")
    public ESection putSection(@RequestBody ESection section, HttpServletRequest req) throws AuthentificationException, ESectionException {
        if(!authorization.isMeFromLivret(req, section.getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable int id, HttpServletRequest req) throws AuthentificationException {
        if(!authorization.isMeFromLivret(req, s_repo.getById(id).getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        EUser me = service.whoAmI(req);
        if(me.getRole() != ELivret.UserRole.RESPONSABLE){
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        s_repo.deleteById(id);
    }
}
