package naukri.portal.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import naukri.portal.entity.Users;
import naukri.portal.entity.UsersType;
import naukri.portal.service.UsersService;
import naukri.portal.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class userctrl {
    private final UserTypeService usertypeser;
    private final UsersService userser;
@Autowired
    public userctrl(UserTypeService usertypeser, UsersService userser ) {
        this.usertypeser = usertypeser;
        this.userser= userser;
    }
    @GetMapping("/register")
    public String register (Model model){
        List<UsersType> usertypes= usertypeser.getall();
        model.addAttribute("getAllTypes",usertypes);
        model.addAttribute("user",new Users());
        return "register";
    }
    @PostMapping("/register/new")
    public String userreg(@Valid Users users,Model model) {
        // System.out.println("user registered");
        Optional<Users> optionalUsers = userser.getUserByEmail(users.getEmail());
        if (optionalUsers.isPresent()) {
            model.addAttribute("error", "email already exist, try new mail");
            List<UsersType> usertypes = usertypeser.getall();
            model.addAttribute("getAllTypes", usertypes);
            model.addAttribute("user", new Users());
            return "register";
        }
        userser.addNew(users);
        return "redirect:/dashboard/";
    }
        @GetMapping("/login")
        public String login(){ return "login";
        }
        @GetMapping("/logout")
        public String logout(HttpServletRequest request, HttpServletResponse response){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }

            return "redirect:/";
        }
}
