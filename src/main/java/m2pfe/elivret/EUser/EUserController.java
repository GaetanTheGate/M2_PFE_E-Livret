package m2pfe.elivret.EUser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/{id}")
    public EUser getUser(@PathVariable Integer id){

        return new EUser();
    }

    @PostMapping("")
    public void addUser(){

    }

    @PutMapping("/edit")
    public void editUser(){

    }

    @PutMapping("")
    public void changePassword(){

    }
}
