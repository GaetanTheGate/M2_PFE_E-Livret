package m2pfe.elivret.EUser;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Autowired
    private EUserRepository u_repo;

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
    public List<EUserDTO.Out.UserInformation> searchSimilarUsers(@RequestBody EUserDTO.In.searchInformation user) {
        List<EUser> users = u_repo.findSimilarTo(user.getEmail())
                .get();

        return users.stream().map(u -> mapper.map(u, EUserDTO.Out.UserInformation.class)).toList();
    }

}
