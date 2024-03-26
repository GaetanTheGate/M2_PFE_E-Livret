package m2pfe.elivret.EModel;

import java.util.List;

import lombok.Data;

import m2pfe.elivret.ELivret.ELivret;

/**
 * <p>
 * DTO of ae ELivret used to represent its model.
 * </p>
 * 
 * @see ELivret
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Data
public class ELivretModel {
    public List<ESectionModel> sections;
}
