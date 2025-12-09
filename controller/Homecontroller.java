package naukri.portal.controller;

import org.springframework.stereotype.Controller;


@Controller
public class Homecontroller {

    public String home(){
        return "index";
    }
}
