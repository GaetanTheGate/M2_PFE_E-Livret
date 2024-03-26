package m2pfe.elivret.EModel;

import java.util.List;

import lombok.Data;

import m2pfe.elivret.ESection.ESection;

/**
 * <p>
 * DTO of an ESection used to represent its model.
 * </p>
 * 
 * @see ESection
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Data
public class ESectionModel {
    public String owner;
    public Integer location;
    public Boolean visibility;
    private String title;

    public List<EQuestionModel> questions;
}
