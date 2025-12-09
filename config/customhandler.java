package naukri.portal.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class customhandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userdetail= (UserDetails) authentication.getPrincipal();
        String username =userdetail.getUsername();
        System.out.println("name"+username+"login ");
        boolean hasjobseekerole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(("Job Seeker")));
        boolean hasrecruiterole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(("Recruiter")));
     if(hasjobseekerole || hasrecruiterole){
         response.sendRedirect("/dashboard/");
     }
    }
}
