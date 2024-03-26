package m2pfe.elivret.ESection;

import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.Populate.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;

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
 * @version 1.2
 */
@RestController
@RequestMapping("/api/sections")
public class ESectionController {
    /**
     * The service to authenticate the user.
     */
    @Autowired
    public AuthentificationService service;

    /**
     * Respository for the ESections.
     */
    @Autowired
    private ESectionRepository s_repo;

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
     * Fetch all the section of the current user.
     * </p>
     * 
     * @param req
     * @return The list of all sections found.
     * @throws AuthentificationException if authentication wrong.
     */
    @GetMapping("/mine")
    public List<ESectionDTO.Out.AllPublic> getMySections(HttpServletRequest req)
            throws AuthentificationException {
        EUser me = service.whoAmI(req);
        return s_repo.findByUser(me).stream().map(s -> mapper.map(s, ESectionDTO.Out.AllPublic.class)).toList();
    }

    /**
     * <p>
     * Fetch all the section the current user has to complete.
     * </p>
     * 
     * @param req
     * @return The list of sections found.
     * @throws AuthentificationException if authentication wrong.
     */
    @GetMapping("/mine/tocomplete")
    public List<ESectionDTO.Out.AllPublic> getMyLivretToComplete(HttpServletRequest req)
            throws AuthentificationException {
        EUser me = service.whoAmI(req);

        return s_repo.findAllSectionsUserHasToComplete(me).get()
                .stream().map(l -> mapper.map(l, ESectionDTO.Out.AllPublic.class)).toList();
    }

    /**
     * <p>
     * Fetch a section from its id.
     * </p>
     * <p>
     * Must be a member of the livret to access.
     * </p>
     * 
     * @param id  the id of the section
     * @param req
     * @return The found section
     * @throws ESectionException if no section was found.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, @SectionRepository.getById(#id))")
    public ESectionDTO.Out.AllPublic getSection(@PathVariable int id, HttpServletRequest req)
            throws ESectionException {
        Optional<ESection> s = s_repo.findById(id);
        s.orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section not found."));

        return mapper.map(s.get(), ESectionDTO.Out.AllPublic.class);
    }

    /// PostMapping

    /// PutMapping

    /**
     * <p>
     * Change the visibility of a section
     * </p>
     * <p>
     * Must be the tutor of the livret to access.
     * </p>
     * 
     * @param section
     * @param req
     * @return The new section
     * @throws ESectionException if no section was found
     */
    @PutMapping("saveVisibility")
    @PreAuthorize("@EntityAccessAuthorization.amITutorFromSection(#req, #section.id)")
    public ESectionDTO.Out.AllPublic putVisibilitySection(@RequestBody ESectionDTO.In.Visibility section,
            HttpServletRequest req) throws ESectionException {
        ESection sec = mapper.map(section, ESection.class);

        ESection s = s_repo.findById(sec.getId())
                .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section not found."));

        s.setVisibility(sec.getVisibility());

        return mapper.map(s_repo.save(s), ESectionDTO.Out.AllPublic.class);
    }

    /// DeleteMapping

    /**
     * <p>
     * Delete a section from its id and anything it contains.
     * </p>
     * <p>
     * Must be the owner of the livret to access.
     * </p>
     * 
     * @param id
     * @param req
     * @throws ESectionException if authentication wrong
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, @SectionRepository.getById(#id))")
    public void deleteSection(@PathVariable int id, HttpServletRequest req) throws ESectionException {
        ESection section = s_repo.findById(id)
                .orElseThrow(() -> new ESectionException(HttpStatus.NO_CONTENT, "Section not found."));

        entityManager.deleteSection(section);
    }
}
