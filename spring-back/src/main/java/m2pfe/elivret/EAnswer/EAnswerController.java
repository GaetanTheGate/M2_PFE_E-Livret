package m2pfe.elivret.EAnswer;

import m2pfe.elivret.Authentification.AuthentificationException;
import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.Populate.EntityManager;

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
 * @version 1.3
 */
@RestController
@RequestMapping("/api/answers")
public class EAnswerController {
        /**
         * The service to authenticate the user.
         */
        @Autowired
        private AuthentificationService service;

        /**
         * Repository for the EAnswer entities.
         */
        @Autowired
        private EAnswerRepository a_repo;

        /**
         * Service used to manage the entity in the Database.
         */
        @Autowired
        private EntityManager entityManager;

        /**
         * Mapper for mapping an object to another.
         */
        public ModelMapper mapper = new ModelMapper();

        /// GetMapping

        /**
         * <p>
         * Fetch all the answers the current user has to complete.
         * </p>
         * 
         * @param req
         * @return The list of answers found.
         * @throws AuthentificationException if authentication wrong
         */
        @GetMapping("/mine/tocomplete")
        public List<EAnswerDTO.Out.AllPublic> getMyLivretToComplete(HttpServletRequest req)
                        throws AuthentificationException {
                EUser me = service.whoAmI(req);

                return a_repo.findAllAnswersUserHasToComplete(me).get()
                                .stream().map(l -> mapper.map(l, EAnswerDTO.Out.AllPublic.class)).toList();
        }

        /**
         * <p>
         * Fetch an answer from its id
         * </p>
         * <p>
         * Must be a member of the livret to access.
         * </p>
         * 
         * @param id  the id of the answer
         * @param req
         * @return The answer found
         * @throws EAnswerException if no answer was found
         */
        @GetMapping("/{id}")
        @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, @AnswerRepository.getById(#id))")
        public EAnswer getAnswer(@PathVariable int id, HttpServletRequest req)
                        throws EAnswerException {
                Optional<EAnswer> a = a_repo.findById(id);
                a.orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                return mapper.map(a.get(), EAnswer.class);
        }

        /// PostMapping

        /// PutMapping

        // For the owner
        /**
         * <p>
         * Save the proposition of the answer.
         * </p>
         * <p>
         * Must be the owner of the livret to access.
         * </p>
         * 
         * @param answer
         * @param req
         * @return The new answer.
         * @throws EAnswerException if no answer was found.
         */
        @PutMapping("saveProposition")
        @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, @AnswerRepository.getById(#answer.id))")
        public EAnswer saveWholeAnswer(@RequestBody EAnswerDTO.In.Proposition answer, HttpServletRequest req)
                        throws EAnswerException {
                EAnswer a = mapper.map(answer, EAnswer.class);

                EAnswer ar = a_repo.findById(a.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                ar.setProposition(a.getProposition());

                return a_repo.save(ar);
        }

        // For the actors
        /**
         * <p>
         * Save the value of the answer.
         * </p>
         * <p>
         * Must be the owner of the answer to access.
         * </p>
         * 
         * @param answer
         * @param req
         * @return The new answer.
         * @throws EAnswerException if no answer was found.
         */
        @PutMapping("saveValue")
        @PreAuthorize("@EntityAccessAuthorization.isAnswerMine(#req, #answer.id)")
        public EAnswer saveValueAsActor(@RequestBody EAnswerDTO.In.Value answer, HttpServletRequest req)
                        throws EAnswerException {
                EAnswer a = mapper.map(answer, EAnswer.class);

                EAnswer ar = a_repo.findById(a.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                ar.setValue(a.getValue());

                return a_repo.save(ar);
        }

        /// DeleteMapping

        /**
         * <p>
         * Delete an answer
         * </p>
         * <p>
         * Must be the owner of the livret to access.
         * </p>
         * 
         * @param id  The id of the answer
         * @param req
         * @throws EAnswerException if no answer was found.
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, @AnswerRepository.getById(#id))")
        public void deleteAnswer(@PathVariable int id, HttpServletRequest req) throws EAnswerException {
                EAnswer answer = a_repo.findById(id)
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                entityManager.deleteAnswer(answer);
        }

}
