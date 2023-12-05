package m2pfe.elivret.ELivret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Authentification.EntityAccessAuthorization;
import m2pfe.elivret.EUser.EUser;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/livrets")
public class ELivretController {
    @Autowired
    private EntityAccessAuthorization authorization;

    @Autowired
    private AuthentificationService service;

    @Autowired
    private ELivretRepository l_repo;

    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @Deprecated
    @GetMapping("/getAll")
    public List<ELivret> getLivrets() {
        return l_repo.findAll().stream().map(l -> mapper.map(l, ELivret.class)).toList();
    }

    @GetMapping("")
    public List<ELivret> getMyLivrets(HttpServletRequest req) throws AuthentificationException, ELivretException {
        EUser me = service.whoAmI(req);
        
        return null;
    }

    @GetMapping("/{id}")
    public ELivret getLivret(@PathVariable int id, HttpServletRequest req) throws AuthentificationException, ELivretException {
        if(!authorization.isMeFromLivret(req, id))
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");

        Optional<ELivret> l = l_repo.findById(id);
        l.orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found."));


        return mapper.map(l.get(), ELivret.class);
    }


    /// PostMapping

    @PostMapping("")
    public ELivret postLivret(@RequestBody ELivret livret, HttpServletRequest req) throws AuthentificationException, ELivretException {
        ELivret l = mapper.map(livret, ELivret.class);

        Optional.ofNullable(l_repo.findById(l.getId()).isPresent() ? null : l)
            .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret already exist."));
        
        return l_repo.save(l);
    }


    /// PutMapping

    @PutMapping("")
    public ELivret putLivret(@RequestBody ELivret livret, HttpServletRequest req) throws AuthentificationException, ELivretException {

        ELivret l = mapper.map(livret, ELivret.class);

        l_repo.findById(l.getId())
            .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found."));

        return l_repo.save(l);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLivret(@PathVariable int id, HttpServletRequest req) throws AuthentificationException {
        l_repo.deleteById(id);
    }
}
