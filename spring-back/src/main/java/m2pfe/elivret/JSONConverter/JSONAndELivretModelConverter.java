package m2pfe.elivret.JSONConverter;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import m2pfe.elivret.EModel.ELivretModel;

/**
 * <p>
 * Service used to convert an ELivretModel into a JSON string, and a JSON string
 * into an ELivretModel
 * </p>
 * 
 * @see ELivretModel
 * @see ObjectMapper
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Service
public class JSONAndELivretModelConverter {
    /**
     * Service used to map a JSON string into an object.
     */
    @Autowired
    private ObjectMapper mapper;

    /**
     * <p>
     * Initialize the service.
     * </p>
     * <p>
     * Makes so that the mapper do not fail on unknown properties.
     * </p>
     */
    @PostConstruct
    public void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * <p>
     * Convert a JSON string into an ELivretModel object.
     * </p>
     * 
     * @see ELivretModel
     * @see ObjectMapper
     * 
     * @param json
     * @return The created ELivretModel
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    public ELivretModel JSONStringToELivretModel(String json)
            throws JsonMappingException, JsonProcessingException {
        ELivretModel livret = mapper.readValue(json, ELivretModel.class);

        return livret;
    }

    /**
     * <p>
     * Convert an ELivretModel into a JSON string.
     * </p>
     * 
     * @see ELivretModel
     * @see ObjectMapper
     * 
     * @param livret
     * @return The JSON string
     */
    public String ELivretModelToJSONString(ELivretModel livret) {
        JSONObject t = new JSONObject(livret);

        return t.toString();
    }
}
