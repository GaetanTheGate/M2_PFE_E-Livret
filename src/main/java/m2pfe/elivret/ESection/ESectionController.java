package m2pfe.elivret.ESection;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sections")
public class ESectionController {

    ESectionRepository s_repo;

    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @GetMapping("")
    public List<ESection> getSections() {
        return s_repo.findAll().stream().map(p -> mapper.map(p, ESection.class))
                .toList();
    }

    @GetMapping("/{id}")
    public ESection getSection(@PathVariable int id) throws ESectionNotFoundException {
        Optional<ESection> s = s_repo.findById(id);
        s.orElseThrow(ESectionNotFoundException::new);

        return mapper.map(s.get(), ESection.class);
    }


    /// PostMapping

    @PostMapping("")
    public ESection postSection(@RequestBody ESection section) throws ESectionNotFoundException {
        ESection s = mapper.map(section, ESection.class);

        Optional.ofNullable(s_repo.findById(s.getId()).isPresent() ? null : s).orElseThrow(ESectionNotFoundException::new);
        return s_repo.save(s);
    }


    /// PutMapping

    @PutMapping("")
    public ESection putSection(@RequestBody ESection section) throws ESectionNotFoundException {

        ESection s = mapper.map(section, ESection.class);

        s_repo.findById(s.getId()).orElseThrow(ESectionNotFoundException::new);

        return s_repo.save(s);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable int id) {
        s_repo.deleteById(id);
    }


    /// Exception

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ESectionNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }


}
