package m2pfe.elivret.ELivret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EModel.ELivretModel;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserException;
import m2pfe.elivret.EUser.EUserRepository;
import m2pfe.elivret.Populate.EntityManager;

import org.modelmapper.ModelMapper;

import java.util.List;

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
 * @version 1.5
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
     * Service used to manage the entity in the Database.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    /**
     * <p>
     * Returns the list of all the livret of the current user.
     * </p>
     * 
     * @param req
     * @return The founds livrets
     * @throws AuthentificationException if wrong authentication
     */
    @GetMapping("/mine")
    public List<ELivretDTO.Out.AllPublic> getMyLivrets(HttpServletRequest req)
            throws AuthentificationException {
        EUser me = service.whoAmI(req);
        return l_repo.findByUser(me).stream().map(l -> mapper.map(l, ELivretDTO.Out.AllPublic.class)).toList();
    }

    /**
     * <p>
     * Returns the list of all the livret of the current user has to complete.
     * </p>
     * 
     * @param req
     * @return The founds livrets
     * @throws AuthentificationException if wrong authentication
     */
    @GetMapping("/mine/tocomplete")
    public List<ELivretDTO.Out.AllPublic> getMyLivretToComplete(HttpServletRequest req)
            throws AuthentificationException {
        EUser me = service.whoAmI(req);

        List<ELivret> list = l_repo.findAllLivretsUserHasToComplete(me).get();

        return list.stream().map(l -> mapper.map(l, ELivretDTO.Out.AllPublic.class)).toList();
    }

    /**
     * <p>
     * Fetch the livret from its id
     * </p>
     * <p>
     * Must be a member of the livret to access.
     * </p>
     * 
     * @see ELivret
     * @see ELivretDTO.Out.AllPublic
     * 
     * @param id The livret to find
     * @return The found livret
     * 
     * @throws ELivretException if no livret was found
     */
    @GetMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, #id)")
    public ELivretDTO.Out.AllPublic getLivret(@PathVariable int id, HttpServletRequest req)
            throws ELivretException {
        ELivret livret = l_repo.findById(id)
                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found."));

        return mapper.map(livret, ELivretDTO.Out.AllPublic.class);
    }

    /**
     * <p>
     * Fetch the model of a livret found with its id.
     * </p>
     * <p>
     * Must be the owner of the livret to access.
     * </p>
     * 
     * @param id
     * @param req
     * @return The model of the livret.
     * @throws ELivretException if no livret was found
     */
    @GetMapping("/{id}/model")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #id)")
    public ELivretModel getLivretModel(@PathVariable int id, HttpServletRequest req)
            throws ELivretException {
        ELivret livret = l_repo.findById(id)
                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found."));

        return mapper.map(livret, ELivretModel.class);
    }

    /// PostMapping

    /**
     * <p>
     * Create a new livret.
     * </p>
     * <p>
     * Must be a "REPONSABLE" to access.
     * </p>
     * 
     * @param livret The livret to create
     * @param req
     * @return The new livret
     * @throws AuthentificationException if authentication error.
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('RESPONSABLE')")
    public ELivretDTO.Out.AllPublic createLivret(@RequestBody ELivretDTO.In.Create livret, HttpServletRequest req)
            throws AuthentificationException {

        EUser me = service.whoAmI(req);
        ELivret l = mapper.map(livret, ELivret.class);
        l.setResponsable(me);

        return mapper.map(entityManager.saveLivret(l), ELivretDTO.Out.AllPublic.class);
    }

    /// PutMapping

    /**
     * <p>
     * Change the model of the livret
     * </p>
     * <p>
     * Must be the owner of the livret to access.
     * </p>
     * 
     * @param id    of the livret to change the model.
     * @param model the new model.
     * @param req
     * @return The new livret.
     * @throws ELivretException If no livret was found.
     */
    @PutMapping("/{id}/model")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #id)")
    public ELivretDTO.Out.AllPublic setModel(@PathVariable int id, @RequestBody ELivretModel model,
            HttpServletRequest req) throws ELivretException {
        ELivret livret = l_repo.findById(id)
                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found"));
        ELivret l_model = mapper.map(model, ELivret.class);

        for (ESection section : livret.getSections())
            entityManager.deleteSection(section);

        for (ESection section : l_model.getSections()) {
            section.setLivret(livret);
            entityManager.saveSection(section);
        }

        livret.setSections(l_model.getSections());

        return getLivret(id, req);
    }

    /**
     * <>
     * Set the actors of the livret
     * </p>
     * <p>
     * You can turn off or on the actors you want to set or no.
     * </p>
     * <p>
     * Must be the owner of the livret to access.
     * </p>
     * 
     * @param livret
     * @param req
     * @param setStudent
     * @param setMaster
     * @param setTutor
     * @return The new livret
     * @throws ELivretException if no livret was found.
     * @throws EUserException   if an actor do no match any one.
     */
    @PutMapping("/set-actors")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #livret.id)")
    public ELivretDTO.Out.AllPublic setActors(@RequestBody ELivretDTO.In.Actors livret, HttpServletRequest req,
            @RequestParam(required = false, defaultValue = "false") Boolean setStudent,
            @RequestParam(required = false, defaultValue = "false") Boolean setMaster,
            @RequestParam(required = false, defaultValue = "false") Boolean setTutor)
            throws ELivretException, EUserException {

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

    /// DeleteMapping

    /**
     * <p>
     * Delete a livret and anything it contains.
     * </p>
     * <p>
     * Must be the owner of the livret to access.
     * </p>
     * 
     * @param id
     * @param req
     * @throws ELivretException if no livret was found.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #id)")
    public void deleteLivret(@PathVariable int id, HttpServletRequest req) throws ELivretException {
        ELivret livret = l_repo.findById(id)
                .orElseThrow(() -> new ELivretException(HttpStatus.NO_CONTENT, "Livret not found."));
        entityManager.deleteLivret(livret);
    }
}
