package naukri.portal.service;

import naukri.portal.entity.Jobseeker;
import naukri.portal.entity.Users;
import naukri.portal.repository.JobseekerRepo;
import naukri.portal.repository.UserRepo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
private final JobseekerRepo jsr;
private final UserRepo uerepo;
    public JobSeekerProfileService(JobseekerRepo jsr, UserRepo uerepo) {
        this.jsr = jsr;
        this.uerepo = uerepo;
    }
    public Optional<Jobseeker> getOne(Integer id) {
        return jsr.findById(id);
    }

    public Jobseeker addNew(Jobseeker jobseeker) {
        return jsr.save(jobseeker);
    }

    public Jobseeker getCurrentSeekerProfile() {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)){
            String  currusername = auth.getName();
            Users users= uerepo.findByEmail(currusername).orElseThrow(()-> new UsernameNotFoundException("user not found"));
            Optional<Jobseeker> seekerprofile = getOne(users.getuserId());
            return seekerprofile.orElse(null);
        }else return null;
    }
}
