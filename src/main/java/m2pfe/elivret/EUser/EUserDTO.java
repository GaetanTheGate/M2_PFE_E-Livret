package m2pfe.elivret.EUser;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ELivret.ELivret;

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

        /**
         * The DTO used for searching users.
         */
        @Data
        @NoArgsConstructor
        public static class searchInformation {
            private String email;
        }
    }


    /**
     * The DTOs the user will get from the application.
     */
    public static class Out {
        /**
         * The DTO to give to someone.
         */
        @Data
        @NoArgsConstructor
        public static class UserInformation {
            private Integer id;
            private String email;
            private ELivret.UserRole role;
        }
    }
}
