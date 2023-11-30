package m2pfe.elivret.EUser;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * A list of DTO to be mapped with an EUser.
 * <p>
 * </p> 
 * Used to filter the informations of an EUser given or taken from an user of the application.
 * </p>
 * 
 * @see EUser
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class EUserDTO {
    /**
     * The DTOs the user will give to the application.
     */
    public static class In {
        /**
         * The DTO used when the user wants to authentify.
         */
        @Data
        @NoArgsConstructor
        public static class AuthentificationInformation {
            private String email;
            private String password;
        }
    }


    /**
     * The DTOs the user will get from the application.
     */
    public static class Out {
        
    }
}
