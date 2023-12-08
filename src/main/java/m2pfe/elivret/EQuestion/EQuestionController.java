package m2pfe.elivret.EQuestion;

import java.util.List;
import java.util.Optional;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.ELivret.ELivret;

import m2pfe.elivret.EUser.EUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Authentification.EntityAccessAuthorization;


import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Rest controller for the AbstractEQuestion entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/questions".
 * </p>
 * 
 * @see AbstractEQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@RestController
@RequestMapping("/api/questions")
public class EQuestionController {
    /**
     * The service used to check the authenticated user's rights.
     */
    @Autowired
    private EntityAccessAuthorization authorization;

    /**
     * The service to authenticate the user.
     */
    @Autowired
    private AuthentificationService service;

    /**
     * Respository for the EQuestions.
     */
    @Autowired
    private EQestionRepository q_repo;

    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();
    

    /// GetMapping

    // TODO: Comments.
    @Deprecated
    @GetMapping("/getAll")
    public List<AbstractEQuestion> getQuestions() {
        return q_repo.findAll().stream().map(q -> mapper.map(q, AbstractEQuestion.class))
                .toList();
    }

    @GetMapping("/{id}")
    public AbstractEQuestion getQuestion(@PathVariable int id, HttpServletRequest req) throws AuthentificationException, EQuestionException {
        if(false&&!authorization.isMeFromLivret(req, q_repo.getById(id).getSection().getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        Optional<AbstractEQuestion> q = q_repo.findById(id);
        q.orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "AbstractEQuestion not found."));

        return mapper.map(q.get(), AbstractEQuestion.class);
    }


    /// PostMapping

    @PostMapping("")
    public AbstractEQuestion postQuestion(@RequestBody AbstractEQuestion question, HttpServletRequest req) throws AuthentificationException, EQuestionException {
        if(!authorization.isMeFromLivret(req, question.getSection().getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        EUser me = service.whoAmI(req);
        if(me.getRole() != ELivret.UserRole.RESPONSABLE){
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        AbstractEQuestion q = mapper.map(question, AbstractEQuestion.class);

        Optional.ofNullable(q_repo.findById(q.getId()).isPresent() ? null : q)
                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "AbstractEQuestion already exist."));

        return q_repo.save(q);
    }


    /// PutMapping

    @PutMapping("")
    public AbstractEQuestion putQuestion(@RequestBody AbstractEQuestion question, HttpServletRequest req) throws AuthentificationException, EQuestionException {
        if(!authorization.isMeFromLivret(req, question.getSection().getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        EUser me = service.whoAmI(req);

        if(me.getRole() != ELivret.UserRole.RESPONSABLE){
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        AbstractEQuestion q = mapper.map(question, AbstractEQuestion.class);

        q_repo.findById(q.getId())
                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "AbstractEQuestion not found."));

        return q_repo.save(q);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable int id, HttpServletRequest req) throws AuthentificationException {
        if(!authorization.isMeFromLivret(req, q_repo.getById(id).getSection().getLivret().getId())) {
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        EUser me = service.whoAmI(req);
        if(me.getRole() != ELivret.UserRole.RESPONSABLE){
            throw new AuthentificationException(HttpStatus.FORBIDDEN, "Not allowed to access this entity.");
        }
        q_repo.deleteById(id);
    }

}
