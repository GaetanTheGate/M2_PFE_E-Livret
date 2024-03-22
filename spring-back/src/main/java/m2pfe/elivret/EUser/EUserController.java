package m2pfe.elivret.EUser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Populate.EntityManager;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * Rest controller for the EUser entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/users".
 * </p>
 * 
 * @see EUser
 * 
 * @author Gaëtan PUPET
 * @version 0.1
 */
@RestController
@RequestMapping("/api/users")
public class EUserController {
    /**
     * Service used to do the authentification in the application.
     * 
     * @see AuthentificationService
     */
    @Autowired
    private AuthentificationService service;

    @Autowired
    private EUserRepository u_repo;

    @Autowired
    private EntityManager entityManager;

    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();

    @GetMapping("/{id}")
    public EUserDTO.Out.UserInformation getUser(@PathVariable Integer id) {
        EUser user = u_repo.findById(id)
                .orElseThrow(() -> new EUserException(HttpStatus.NO_CONTENT, "User not found."));

        return mapper.map(user, EUserDTO.Out.UserInformation.class);
    }

    @PostMapping("/search")
    public List<EUserDTO.Out.UserInformation> searchSimilarUsers(@RequestBody EUserDTO.In.SearchInformation user) {
        List<EUser> users = u_repo.findSimilarTo(user.getEmail())
                .get();

        return users.stream().map(u -> mapper.map(u, EUserDTO.Out.UserInformation.class)).toList();
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasAuthority('RESPONSABLE')")
    public ResponseEntity<String> createUser(@RequestBody EUserDTO.In.NewUser user, HttpServletRequest req) {
        EUser u = service.whoAmI(req);
        System.out.println(u.getPermission());

        EUser newuser = mapper.map(user, EUser.class);
        newuser.setPassword("\\");

        newuser = entityManager.saveUser(newuser);

        return ResponseEntity.ok(service.getTokenForUser(newuser));
    }
}
