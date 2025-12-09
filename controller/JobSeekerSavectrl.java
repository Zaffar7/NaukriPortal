package naukri.portal.controller;

import naukri.portal.entity.JobPostActivity;
import naukri.portal.entity.JobSeekerSave;
import naukri.portal.entity.Jobseeker;
import naukri.portal.entity.Users;
import naukri.portal.service.JobPostActivityService;
import naukri.portal.service.JobSeekerProfileService;
import naukri.portal.service.JobSeekerSaveService;
import naukri.portal.service.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSavectrl {
private final UsersService usersevice;
private final JobSeekerProfileService jsps;
private final JobPostActivityService jpas;
private final JobSeekerSaveService jsss;

    public JobSeekerSavectrl(UsersService usersevice, JobSeekerProfileService jsps, JobPostActivityService jpas, JobSeekerSaveService jsss) {
        this.usersevice = usersevice;
        this.jsps = jsps;
        this.jpas = jpas;
        this.jsss = jsss;
    }
    @PostMapping("job-details/save/{id}")
    private String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersevice.findByEmail(currentUsername);
            Optional<Jobseeker> seekerProfile = jsps.getOne(user.getuserId());
            JobPostActivity jobPostActivity = jpas.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(seekerProfile.get());
            } else {
                throw new RuntimeException("User not found");
            }
            jsss.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }
    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {

        List<JobPostActivity> jobPost = new ArrayList<>();
        Object currentUserProfile = usersevice.getCurrentUserprof();

        List<JobSeekerSave> jobSeekerSaveList = jsss.getCandidatesJob((Jobseeker) currentUserProfile);
        for (JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
            jobPost.add(jobSeekerSave.getJob());
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", currentUserProfile);

        return "saved-jobs";
    }

}
