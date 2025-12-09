package naukri.portal.controller;

import naukri.portal.entity.Jobseeker;
import naukri.portal.entity.Skills;
import naukri.portal.entity.Users;
import naukri.portal.repository.UserRepo;
import naukri.portal.service.JobSeekerProfileService;
import naukri.portal.util.FileDownloadUtil;
import naukri.portal.util.fileUploadutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfilectrl {

    private JobSeekerProfileService jobSeekerProfileService;

    private UserRepo usersRepository;

    @Autowired
    public JobSeekerProfilectrl(JobSeekerProfileService jobSeekerProfileService, UserRepo usersRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String jobSeekerProfile(Model model) {
        Jobseeker jobSeeker = new Jobseeker();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
            Optional<Jobseeker> seekerProfile = jobSeekerProfileService.getOne (user.getuserId());
            if (seekerProfile.isPresent()) {
                jobSeeker = seekerProfile.get();
                if (jobSeeker.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeeker.setSkills(skills);
                }
            }

            model.addAttribute("skills", skills);
            model.addAttribute("profile", jobSeeker);
        }

        return "job-seeker-profile";
    }
    @PostMapping("/addNew")
  public String addNew(Jobseeker jobseeker, @RequestParam("image")MultipartFile image,
                       @RequestParam("pdf") MultipartFile pdf, Model model){
     Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
      if (!(authentication instanceof AnonymousAuthenticationToken)) {
          Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
          jobseeker.setuserId(user);
          jobseeker.setUserAccountId(user.getuserId());
      }
      List<Skills> skillsList = new ArrayList<>();
      model.addAttribute("profile",jobseeker);
      model.addAttribute("skill",skillsList);
      for (Skills skills :jobseeker.getSkills()){
          skills.setJobseeker(jobseeker);
      }
      String imagename="";
      String resumename ="";

      if (!Objects.equals(image.getOriginalFilename(), "")) {
          imagename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
          jobseeker.setProfilePhoto(imagename);
      }

      if (!Objects.equals(pdf.getOriginalFilename(), "")) {
          resumename = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
          jobseeker.setResume(resumename);
      }
       Jobseeker seeker=jobSeekerProfileService.addNew(jobseeker);
      try {
          String uploadDir = "photos/candidate/" + jobseeker.getUserAccountId();
          if (!Objects.equals(image.getOriginalFilename(), "")) {

              fileUploadutil.saveFile(uploadDir, imagename, image);
          }
          if (!Objects.equals(pdf.getOriginalFilename(), "")) {
              fileUploadutil.saveFile(uploadDir, resumename, pdf);
          }
      }
      catch (IOException ex) {
          throw new RuntimeException(ex);
      }

     return "redirect:/dashboard/";
  }
    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model) {

        Optional<Jobseeker> seekerProfile = jobSeekerProfileService.getOne(id);
        model.addAttribute("profile", seekerProfile.get());
        return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "userID") String userId) {

        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        Resource resource = null;

        try {
            resource = downloadUtil.getFileAsResourse("photos/candidate/"+ userId,fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename()+ "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);

    }
}
