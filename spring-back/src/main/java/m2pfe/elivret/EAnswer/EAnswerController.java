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
 * @version 1.2
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
         **/
        @Autowired
        private EAnswerRepository a_repo;

        @Autowired
        private EntityManager entityManager;

        /**
         * Mapper for mapping an object to another.
         */
        public ModelMapper mapper = new ModelMapper();

        /// GetMapping

        // TODO: COMMENTS.
        @GetMapping("/getAll")
        public List<EAnswer> getAnswers() {
                return a_repo.findAll().stream().map(a -> mapper.map(a, EAnswer.class))
                                .toList();
        }

        @GetMapping("/mine/tocomplete")
        public List<EAnswerDTO.Out.AllPublic> getMyLivretToComplete(HttpServletRequest req) {
                EUser me = service.whoAmI(req);

                return a_repo.findAllAnswersUserHasToComplete(me).get()
                                .stream().map(l -> mapper.map(l, EAnswerDTO.Out.AllPublic.class)).toList();
        }

        @GetMapping("/{id}")
        @PreAuthorize("@EntityAccessAuthorization.isMeFromLivret(#req, @AnswerRepository.getById(#id))")
        public EAnswer getAnswer(@PathVariable int id, HttpServletRequest req)
                        throws AuthentificationException, EAnswerException {
                Optional<EAnswer> a = a_repo.findById(id);
                a.orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                return mapper.map(a.get(), EAnswer.class);
        }

        /// PostMapping

        @Deprecated
        @PostMapping("")
        @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, #answer)")
        public EAnswer postAnswer(@RequestBody EAnswer answer, HttpServletRequest req)
                        throws AuthentificationException, EAnswerException {
                EAnswer a = mapper.map(answer, EAnswer.class);

                Optional.ofNullable(a_repo.findById(a.getId()).isPresent() ? null : a)
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT,
                                                "EAnswer already exist."));

                return a_repo.save(a);
        }

        /// PutMapping

        // For the owner
        @PutMapping("saveProposition")
        @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, @AnswerRepository.getById(#answer.id))")
        public EAnswer saveWholeAnswer(@RequestBody EAnswerDTO.In.Proposition answer, HttpServletRequest req)
                        throws AuthentificationException, EAnswerException {
                EAnswer a = mapper.map(answer, EAnswer.class);

                EAnswer ar = a_repo.findById(a.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                ar.setProposition(a.getProposition());

                return a_repo.save(ar);
        }

        // For the actors

        @PutMapping("saveValue")
        @PreAuthorize("@EntityAccessAuthorization.isAnswerMine(#req, #answer.id)")
        public EAnswer saveValueAsActor(@RequestBody EAnswerDTO.In.Value answer, HttpServletRequest req)
                        throws AuthentificationException, EAnswerException {
                EAnswer a = mapper.map(answer, EAnswer.class);

                EAnswer ar = a_repo.findById(a.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                ar.setValue(a.getValue());

                return a_repo.save(ar);
        }

        @Deprecated
        @PutMapping("")
        @PreAuthorize("@EntityAccessAuthorization.isAnswerMine(#req, #answer)")
        public EAnswer putAnswer(@RequestBody EAnswer answer, HttpServletRequest req)
                        throws AuthentificationException, EAnswerException {
                EAnswer a = mapper.map(answer, EAnswer.class);

                a_repo.findById(a.getId())
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                return a_repo.save(a);
        }

        /// DeleteMapping

        @DeleteMapping("/{id}")
        @PreAuthorize("@EntityAccessAuthorization.isLivretMine(#req, @AnswerRepository.getById(#id))")
        public void deleteAnswer(@PathVariable int id, HttpServletRequest req) throws AuthentificationException,
                        EAnswerException {
                EAnswer answer = a_repo.findById(id)
                                .orElseThrow(() -> new EAnswerException(HttpStatus.NO_CONTENT, "EAnswer not found."));

                entityManager.deleteAnswer(answer);
        }

}
