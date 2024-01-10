package m2pfe.elivret.ELivret;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ESection.ESectionDTO;

public class ELivretDTO {
    public static class In {

    }

    public static class Out {
        @Data
        @NoArgsConstructor
        public static class AllPublic {
            public Integer id;
            public String name;
            public Integer studentId;
            public Integer tutorId;
            public Integer masterId;
            public Integer responsableId;
            public List<ESectionDTO.Out.AllPublic> sections;
        }
    }
    
}
