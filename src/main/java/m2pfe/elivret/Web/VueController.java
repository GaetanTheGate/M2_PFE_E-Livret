package m2pfe.elivret.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("/")
public class VueController {

    @RequestMapping("")
    public ModelAndView mainPage() {
        return new ModelAndView("home") ;
    }

    @RequestMapping("livret/{id}")
    public ModelAndView consultLivret(@PathVariable Integer id) {
        return new ModelAndView("livret") ;
    }
    
}
