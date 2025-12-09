package naukri.portal.controller;
import naukri.portal.util.fileUploadutil;
import org.springframework.util.StringUtils;

import naukri.portal.entity.Recruiterprofile;
import naukri.portal.entity.Users;
import naukri.portal.repository.UserRepo;
import naukri.portal.service.RecruiterProfileService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileCtrl {
    private final UserRepo userepo;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileCtrl(UserRepo userepo, RecruiterProfileService recruiterProfileService) {
        this.userepo = userepo;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String currUser = auth.getName();
            Users users = userepo.findByEmail(currUser).orElseThrow(() -> new UsernameNotFoundException("Could not " + "found user"));
            Optional<Recruiterprofile> recruiterProfile = recruiterProfileService.getOne(users.getuserId());

            if (!recruiterProfile.isEmpty())
                model.addAttribute("profile", recruiterProfile.get());

        }

        return "recruiter_profile";
    }
   @PostMapping("/addNew")
    public String addNew(Recruiterprofile recruiterprofile, @RequestParam("image") MultipartFile multipartFile,
                         Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String currUser = auth.getName();
            Users users = userepo.findByEmail(currUser).orElseThrow(() -> new UsernameNotFoundException("Could not " + "found user"));
            recruiterprofile.setuserId( users);
            recruiterprofile.setUserAccountId(users.getuserId());
        }
        model.addAttribute("profile", recruiterprofile);
        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterprofile.setProfilePhoto(fileName);
        }
        Recruiterprofile saveduser = recruiterProfileService.addNew(recruiterprofile);
        String uploadDir = "photos/recruiter/" + saveduser.getUserAccountId();
        try {
            fileUploadutil.saveFile(uploadDir, fileName, multipartFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/dashboard/";
    }
}

