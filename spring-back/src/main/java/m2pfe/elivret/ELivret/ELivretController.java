package m2pfe.elivret.ELivret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserException;
import m2pfe.elivret.EUser.EUserRepository;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Rest controller for the ELivret entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/livrets".
 * </p>
 *
 * @see ELivret
 *
 * @author Gaëtan PUPET
 * @version 1.1
 */
@RestController
@RequestMapping("/api/livrets")
public class ELivretController {
    /**
     * The service to authenticate the user.
     */
    @Autowired
    private AuthentificationService service;

    /**
     * Respository for the ELivrets.
     */
    @Autowired
    private ELivretRepository l_repo;

    /**
     * Respository for the Eusers.
     */
    @Autowired
    private EUserRepository u_repo;

    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @Deprecated
    @GetMapping("/getAll")
    public List<ELivretDTO.Out.AllPublic> getLivrets() {
        return l_repo.findAll().stream().map(l -> mapper.map(l, ELivretDTO.Out.AllPublic.class)).toList();
    }

    @GetMapping("/mine")
    public List<ELivretDTO.Out.AllPublic> getMyLivrets(HttpServletRequest req)
            throws AuthentificationException, ELivretException {
        EUser me = service.whoAmI(req);
        return l_repo.findByUser(me).stream().map(l -> mapper.map(l, ELivretDTO.Out.AllPublic.class)).toList();
    }

    @GetMapping("/mine/tocomplete")
    public List<ELivretDTO.Out.AllPublic> getMyLivretToComplete(HttpServletRequest req) {
        EUser me = service.whoAmI(req);

        return l_repo.findAllLivretsUserHasToComplete(me).get()
                .stream().map(l -> mapper.map(l, ELivretDTO.Out.AllPublic.class)).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, #id)")
    public ELivretDTO.Out.AllPublic getLivret(@PathVariable int id, HttpServletRequest req)
            throws AuthentificationException, ELivretException {
        Optional<ELivret> l = l_repo.findById(id);
        l.orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found."));

        return mapper.map(l.get(), ELivretDTO.Out.AllPublic.class);
    }

    /// PostMapping

    @PostMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #livret)")
    public ELivret postLivret(@RequestBody ELivret livret, HttpServletRequest req)
            throws AuthentificationException, ELivretException {
        ELivret l = mapper.map(livret, ELivret.class);

        Optional.ofNullable(l_repo.findById(l.getId()).isPresent() ? null : l)
                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret already exist."));

        return l_repo.save(l);
    }

    /// PostMapping

    @PostMapping("/create")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #livret)")
    public ELivretDTO.Out.Mimimum createLivret(@RequestBody ELivretDTO.In.Basics livret, HttpServletRequest req)
            throws AuthentificationException, ELivretException {
                
        EUser me = service.whoAmI(req);
        ELivret l = mapper.map(livret, ELivret.class);
        l.setResponsable(me);

        return mapper.map(l_repo.save(l), ELivretDTO.Out.Mimimum.class);
    }

    /// PutMapping

    @PutMapping("/set-actors")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #livret.id)")
    public ELivretDTO.Out.AllPublic setActors(@RequestBody ELivretDTO.In.Actors livret, HttpServletRequest req,
            @RequestParam(required = false, defaultValue = "false") Boolean setStudent,
            @RequestParam(required = false, defaultValue = "false") Boolean setMaster,
            @RequestParam(required = false, defaultValue = "false") Boolean setTutor) {

        ELivret l = l_repo.findById(livret.getId())
                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found"));

        if (setStudent)
            l.setStudent(u_repo.findById(livret.getStudentId())
                    .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT, "Student not found")));

        if (setMaster)
            l.setMaster(u_repo.findById(livret.getMasterId())
                    .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT, "Master not found")));

        if (setTutor)
            l.setTutor(u_repo.findById(livret.getTutorId())
                    .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT, "Tutor not found")));

        return mapper.map(l_repo.save(l), ELivretDTO.Out.AllPublic.class);
    }

    @PutMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #livret)")
    public ELivret putLivret(@RequestBody ELivret livret, HttpServletRequest req)
            throws AuthentificationException, ELivretException {
        ELivret l = mapper.map(livret, ELivret.class);

        l_repo.findById(l.getId())
                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found."));

        return l_repo.save(l);
    }

    /// DeleteMapping

    @DeleteMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLivret(@PathVariable int id, HttpServletRequest req) throws AuthentificationException {
        l_repo.deleteById(id);
    }
}
