package m2pfe.elivret.EModel;

import java.util.List;

import lombok.Data;

@Data
public class ESectionModel {
    public String owner;
    public Integer location;
    public Boolean visibility;
    private String title;

    public List<EQuestionModel> questions;
}
