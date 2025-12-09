package naukri.portal.service;

import naukri.portal.entity.Jobseeker;
import naukri.portal.entity.Recruiterprofile;
import naukri.portal.entity.Users;
import naukri.portal.repository.JobseekerRepo;
import naukri.portal.repository.RecruiterRepo;
import naukri.portal.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private final UserRepo userepo;
    private final JobseekerRepo jobseekerrepo;
    private final RecruiterRepo Recruiterrepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UserRepo userepo, JobseekerRepo jobseekerrepo, RecruiterRepo Recruiterrepo, PasswordEncoder passwordEncoder) {
        this.userepo = userepo;
        this.jobseekerrepo=jobseekerrepo;
        this.Recruiterrepo=Recruiterrepo;
        this.passwordEncoder = passwordEncoder;
    }
    public Users addNew(Users users){
        users.setActive(true);
        users.setRegistrationdate(new Date(System.currentTimeMillis()));
        Users saveduser = userepo.save(users);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
int usertypeid = users.getUserTypeId().getUserTypeId();

if(usertypeid==1){
    Recruiterrepo.save(new Recruiterprofile(saveduser));
}else{
    jobseekerrepo.save(new Jobseeker(saveduser));
}
 return saveduser;
    }
    public Object getCurrentUserprof() {
       Authentication autho=  SecurityContextHolder.getContext().getAuthentication();
    if(!(autho instanceof AnonymousAuthenticationToken)){
        String username= autho.getName();
        Users users = userepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("not found"));
        int userid = users.getuserId();
        if(autho.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
            Recruiterprofile recruiterprofile = Recruiterrepo.findById(userid).orElse(new Recruiterprofile());
            return recruiterprofile;
        }else{
            Jobseeker jobseeker = jobseekerrepo.findById(userid).orElse(new Jobseeker());
            return jobseeker;
        }
    }
    return null;
    }
    public Optional<Users> getUserByEmail(String email){
        return userepo.findByEmail(email);
    }

    public Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof AnonymousAuthenticationToken)){
            String username = auth.getName();
            Users user = userepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("could not found" + "user"));
            return user;
        }
        return null;
    }


    public Users findByEmail(String currentUsername) {
        return userepo.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("could not found"));

    }
}
