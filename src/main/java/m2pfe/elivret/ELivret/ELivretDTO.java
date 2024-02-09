package m2pfe.elivret.ELivret;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ESection.ESectionDTO;
import m2pfe.elivret.EUser.EUserDTO;

/**
 * <p>
 * A list of DTO to be mapped with an ELivret.
 * <p>
 * </p> 
 * Used to filter the informations of a given or taken ELivret.
 * </p>
 * 
 * @see ELivret
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class ELivretDTO {
    /**
     * The DTOs the user will give to the application.
     */
    public static class In {
        /**
         * The DTO used to specify the actors within a livret.
         */
        @Data
        @NoArgsConstructor
        public static class Actors {
            public Integer id;

            public Integer studentId;
            public Integer tutorId;
            public Integer masterId;
        }
    }

    /**
     * The DTOs the user will get from the application.
     */
    public static class Out {
        /**
         * The DTO used to give all the public information about a livret.
         */
        @Data
        @NoArgsConstructor
        public static class AllPublic {
            public Integer id;
            public String name;
            public EUserDTO.Out.UserInformation student;
            public EUserDTO.Out.UserInformation tutor;
            public EUserDTO.Out.UserInformation master;
            public EUserDTO.Out.UserInformation responsable;
            public List<ESectionDTO.Out.AllPublic> sections;
        }
    }
    
}
