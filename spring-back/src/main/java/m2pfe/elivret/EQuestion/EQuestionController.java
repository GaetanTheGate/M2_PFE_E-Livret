package m2pfe.elivret.EQuestion;

import java.util.List;
import java.util.Optional;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.Populate.EntityManager;

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
 * @version 1.1
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
     * Fetch all the questions the current user has to complete.
     * </p>
     * 
     * @param req
     * @return The list of questions found.
     * @throws AuthentificationException if authentication wrong
     */
    @GetMapping("/mine/tocomplete")
    public List<EQuestionDTO.Out.AllPublic> getMyLivretToComplete(HttpServletRequest req)
            throws AuthentificationException {
        EUser me = service.whoAmI(req);

        return q_repo.findAllQuestionsUserHasToComplete(me).get()
                .stream().map(l -> mapper.map(l, EQuestionDTO.Out.AllPublic.class)).toList();
    }

    /**
     * <p>
     * Fetch a question from its id.
     * </p>
     * <p>
     * Must be a member of the livret to access.
     * </p>
     * 
     * @param id  the id of the question
     * @param req
     * @return the found question
     * @throws EQuestionException if no question was found.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, @QuestionRepository.getById(#id))")
    public EQuestion getQuestion(@PathVariable int id, HttpServletRequest req)
            throws EQuestionException {
        Optional<EQuestion> q = q_repo.findById(id);
        q.orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "EQuestion not found."));

        return mapper.map(q.get(), EQuestion.class);
    }

    /// PostMapping

    /// PutMapping

    /// DeleteMapping

    /**
     * <p>
     * Delete a question from its id and anything it contains.
     * </p>
     * <p>
     * Must be the owner of the livret to access.
     * </p>
     * 
     * @param id  The id of the question to delete
     * @param req
     * @throws EQuestionException if no question was found.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req,  @QuestionRepository.getById(#id))")
    public void deleteQuestion(@PathVariable int id, HttpServletRequest req) throws EQuestionException {
        EQuestion question = q_repo.findById(id)
                .orElseThrow(() -> new EQuestionException(HttpStatus.NO_CONTENT, "EQuestion not found."));

        entityManager.deleteQuestion(question);
    }

}
