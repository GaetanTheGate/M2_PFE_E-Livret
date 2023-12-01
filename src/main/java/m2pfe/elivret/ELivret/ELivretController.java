package m2pfe.elivret.ELivret;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livrets")
public class ELivretController {
    ELivretRepository l_repo;

    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @GetMapping("")
    public List<ELivret> getLivrets() {
        return l_repo.findAll().stream().map(l -> mapper.map(l, ELivret.class)).toList();
    }

    @GetMapping("/{id}")
    public ELivret getLivret(@PathVariable int id) throws ELivretNotFoundException {
        Optional<ELivret> l = l_repo.findById(id);
        l.orElseThrow(ELivretNotFoundException::new);
        return mapper.map(l.get(), ELivret.class);
    }


    /// PostMapping

    @PostMapping("")
    public ELivret postLivret(@RequestBody ELivret livret) throws ELivretNotFoundException {
        ELivret l = mapper.map(livret, ELivret.class);

        Optional.ofNullable(l_repo.findById(l.getId()).isPresent() ? null : l).orElseThrow(ELivretNotFoundException::new);
        return l_repo.save(l);
    }


    /// PutMapping

    @PutMapping("")
    public ELivret putLivret(@RequestBody ELivret livret) throws ELivretNotFoundException {

        ELivret l = mapper.map(livret, ELivret.class);

        l_repo.findById(l.getId()).orElseThrow(ELivretNotFoundException::new);

        return l_repo.save(l);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLivret(@PathVariable int id) {
        l_repo.deleteById(id);
    }

    /// Exception
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ELivretNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
