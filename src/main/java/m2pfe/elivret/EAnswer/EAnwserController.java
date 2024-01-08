package m2pfe.elivret.EAnswer;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Authentification.EntityAccessAuthorization;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Rest controller for the EAnswer entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/answers".
 * </p>
 * 
 * @see EAnswer
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
@RestController
@RequestMapping("/api/answers")
public class EAnwserController {
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
     * Repository for the EAnswer entities.
     **/
    @Autowired
    private EAnswerRepository a_repo;
    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();


    ///GetMapping

    // TODO: COMMENTS.
    @GetMapping("/getAll")
    public List<EAnswer> getAnswers() {
        return a_repo.findAll().stream().map(a -> mapper.map(a, EAnswer.class))
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, @AnswerRepository.getById(#id))")
    public EAnswer getAnswer(@PathVariable int id, HttpServletRequest req) throws AuthentificationException, EAnswerException {
        // if(!authorization.isMeFromLivret(req, a_repo.getById(id).getQuestion().getSection().getLivret().getId())) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        Optional<EAnswer> a = a_repo.findById(id);
        a.orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

        return mapper.map(a.get(), EAnswer.class);
    }


    /// PostMapping

    @PostMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #answer)")
    public EAnswer postAnswer(@RequestBody EAnswer answer, HttpServletRequest req) throws AuthentificationException, EAnswerException {
        // if(!authorization.isMeFromLivret(req, answer.getQuestion().getSection().getLivret().getId())) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }

        // if(!authorization.isSectionMine(req,answer.getQuestion().getSection().getId())){
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        EAnswer a = mapper.map(answer, EAnswer.class);

        Optional.ofNullable(a_repo.findById(a.getId()).isPresent() ? null : a)
                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer already exist."));

        return a_repo.save(a);
    }


    /// PutMapping

    @PutMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isAnswerMine(#req, #answer)")
    public EAnswer putAnswer(@RequestBody EAnswer answer, HttpServletRequest req) throws AuthentificationException, EAnswerException {
        // if(!authorization.isAnswerMine(req, answer)) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }

        EAnswer a = mapper.map(answer, EAnswer.class);

        a_repo.findById(a.getId())
                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

        return a_repo.save(a);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, @AnswerRepository.getById(#id))")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnswer(@PathVariable int id, HttpServletRequest req) throws AuthentificationException {
        // if(!authorization.isMeFromLivret(req, a_repo.getById(id).getQuestion().getSection().getLivret().getId())) {
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        // if(!authorization.isSectionMine(req,a_repo.getById(id).getQuestion().getSection().getId())){
        //     throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        // }
        a_repo.deleteById(id);
    }
    
}
