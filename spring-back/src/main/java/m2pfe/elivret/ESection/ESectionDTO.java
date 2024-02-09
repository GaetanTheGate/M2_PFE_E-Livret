package m2pfe.elivret.ESection;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.EQuestionDTO;

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
            private Integer id;
            private Boolean visibility;
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
            private Integer id;
            private UserRole owner;
            private Boolean visibility;
            private String title;
            private Integer livretId;
            private List<EQuestionDTO.Out.AllPublic> questions;
        }
    }

}
