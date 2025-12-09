package naukri.portal.service;

import naukri.portal.entity.JobPostActivity;
import naukri.portal.entity.JobSeekerApply;
import naukri.portal.entity.Jobseeker;
import naukri.portal.repository.JobSeekerApplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {
private final JobSeekerApplyRepo jsar;
    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepo jsar) {
        this.jsar = jsar;
    }

    public List<JobSeekerApply> getCandidatesJobs(Jobseeker userAccountId){
        return  jsar.findByUserId(userAccountId);
    }
    public List<JobSeekerApply> getJobCandidates(JobPostActivity job){
        return jsar.findByJob(job);
    }

    public void addNew(JobSeekerApply jobSeekerApply) {
        jsar.save(jobSeekerApply);
    }
}
