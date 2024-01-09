package m2pfe.elivret.EAnswer;

import lombok.Data;
import lombok.NoArgsConstructor;

public class EAnswerDTO {
    public static class In {

        @Data
        @NoArgsConstructor
        public static class Proposition {
            public Integer id;
            public String proposition;    
        }

        @Data
        @NoArgsConstructor
        public static class Value {
            public Integer id;
            public String value;    
        }
    }

    public static class Out {

    }
    
}
