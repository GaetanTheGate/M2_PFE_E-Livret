package m2pfe.elivret.Json;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import m2pfe.elivret.EModel.ELivretModel;

@Deprecated
@Service
public class JSONAndELivretModelConverter {
    @Autowired
    ObjectMapper mapper;

    @PostConstruct
    public void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ELivretModel JSONStringToELivretModel(String json)
            throws JsonMappingException, JsonProcessingException {
        ELivretModel livret = mapper.readValue(json, ELivretModel.class);

        return livret;
    }

    public String ELivretModelToJSONString(ELivretModel livret) {
        JSONObject t = new JSONObject(livret);

        return t.toString();
    }
}
