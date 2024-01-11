package m2pfe.elivret.ESection;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.AbstractEQuestion;

public class ESectionDTO {
    public static class In {
        @Data
        @NoArgsConstructor
        public static class Visibility {
            public Integer id;
            public Boolean visibility;
        }
    }

    public static class Out {

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
