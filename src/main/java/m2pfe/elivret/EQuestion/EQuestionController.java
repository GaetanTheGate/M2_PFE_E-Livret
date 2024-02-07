package m2pfe.elivret.EQuestion;

import java.util.List;
import java.util.Optional;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EUser.EUser;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Rest controller for the EQuestion entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/questions".
 * </p>
 * 
 * @see EQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.
 */
@RestController
@RequestMapping("/api/questions")
public class EQuestionController {
    /**
     * The service to authenticate the user.
     */
    @Autowired
    public AuthentificationService service;

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
    public List<EQuestion> getQuestions() {
        return q_repo.findAll().stream().map(q -> mapper.map(q, EQuestion.class))
                .toList();
    }


    @GetMapping("/mine/tocomplete")
    public List<EQuestionDTO.Out.AllPublic> getMyLivretToComplete(HttpServletRequest req){
        EUser me = service.whoAmI(req);

        return q_repo.findAllQuestionsUserHasToComplete(me).get()
            .stream().map(l -> mapper.map(l, EQuestionDTO.Out.AllPublic.class)).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, @QuestionRepository.getById(#id))")
    public EQuestion getQuestion(@PathVariable int id, HttpServletRequest req) throws AuthentificationException, EQuestionException {
        Optional<EQuestion> q = q_repo.findById(id);
        q.orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "EQuestion not found."));

        return mapper.map(q.get(), EQuestion.class);
    }


    /// PostMapping

    @PostMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #question)")
    public EQuestion postQuestion(@RequestBody EQuestion question, HttpServletRequest req) throws AuthentificationException, EQuestionException {
        EQuestion q = mapper.map(question, EQuestion.class);

        Optional.ofNullable(q_repo.findById(q.getId()).isPresent() ? null : q)
                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "EQuestion already exist."));

        return q_repo.save(q);
    }


    /// PutMapping

    @PutMapping("")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #question)")
    public EQuestion putQuestion(@RequestBody EQuestion question, HttpServletRequest req) throws AuthentificationException, EQuestionException {
        EQuestion q = mapper.map(question, EQuestion.class);

        q_repo.findById(q.getId())
                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "EQuestion not found."));

        return q_repo.save(q);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req,  @QuestionRepository.getById(#id))")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable int id, HttpServletRequest req) throws AuthentificationException {
        q_repo.deleteById(id);
    }

}
