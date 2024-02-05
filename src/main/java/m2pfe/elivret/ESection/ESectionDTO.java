package m2pfe.elivret.ESection;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.AbstractEQuestion;

/**
 * <p>
 * A list of DTO to be mapped with an ESection.
 * <p>
 * </p> 
 * Used to filter the informations of a given or taken ESection.
 * </p>
 * 
 * @see ESection
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class ESectionDTO {
    /**
     * The DTOs the user will give to the application.
     */
    public static class In {
        @Data
        @NoArgsConstructor
        public static class Visibility {
            public Integer id;
            public Boolean visibility;
        }
    }

    /**
     * The DTOs the user will get from the application.
     */
    public static class Out {
        /**
         * The DTO used to give all the public information about a section.
         */
        @Data
        @NoArgsConstructor
        public static class AllPublic {
            public Integer id;
            
            public UserRole owner;
            public Boolean visibility;
            public String title;
            public Integer livretId;
            public List<AbstractEQuestion> questions;
        }
    }
    
}
