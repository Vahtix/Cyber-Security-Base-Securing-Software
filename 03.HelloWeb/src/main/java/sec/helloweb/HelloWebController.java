package sec.helloweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWebController {

    @RequestMapping("*")
    @ResponseBody
    public String home() {
        return "Hello Web!";
    }
}
