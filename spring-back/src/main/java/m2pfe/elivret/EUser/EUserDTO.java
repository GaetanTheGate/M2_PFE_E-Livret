package m2pfe.elivret.EUser;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EUser.EUser.Permission;

/**
 * <p>
 * A list of DTO to be mapped with an EUser.
 * <p>
 * </p>
 * Used to filter the informations of an EUser given or taken from an user of
 * the application.
 * </p>
 * 
 * @see EUser
 * 
 * @author Gaëtan PUPET
 * @version 1.2
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
         * The DTO used when the user wants to change its password.
         */
        @Data
        @NoArgsConstructor
        public static class ChangePassword {
            private String email;
            private String password;
            private String newpassword;
        }

        /**
         * The DTO used to create a EUser.
         */
        @Data
        @NoArgsConstructor
        public static class NewUser {
            private String email;
            private String firstName;
            private String lastName;
        }

        /**
         * The DTO used to create a password.
         */
        @Data
        @NoArgsConstructor
        public static class newPassword {
            private String password;
        }

        /**
         * The DTO used for searching users.
         */
        @Data
        @NoArgsConstructor
        public static class SearchInformation {
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

            private Permission permission;

            private String firstName;
            private String lastName;

            @Getter(AccessLevel.NONE)
            private String password;

            public Boolean getIsPasswordSet() {
                return !password.equals("\\");
            }
        }
    }
}
