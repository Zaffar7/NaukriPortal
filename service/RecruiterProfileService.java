package naukri.portal.service;

import naukri.portal.entity.Recruiterprofile;
import naukri.portal.entity.Users;
import naukri.portal.repository.RecruiterRepo;
import naukri.portal.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {
private final RecruiterRepo recruiterepo;
private final UserRepo userepo;
@Autowired
    public RecruiterProfileService(RecruiterRepo recruiterepo, UserRepo userepo) {
        this.recruiterepo = recruiterepo;
    this.userepo = userepo;
}
    public Optional<Recruiterprofile>getOne(Integer id){
        return recruiterepo.findById(id);
    }

    public Recruiterprofile addNew(Recruiterprofile recruiterprofile) {
        return recruiterepo.save(recruiterprofile);
    }

    public Recruiterprofile getCurrentRecruiterProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)){
            String curruser = auth.getName();
            Users users =userepo.findByEmail(curruser).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            Optional<Recruiterprofile> recruiterprofile=getOne(users.getuserId());
            return recruiterprofile.orElse(null);
        }
        else return null;
    }
}
