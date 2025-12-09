package naukri.portal.service;

import naukri.portal.entity.JobPostActivity;
import naukri.portal.entity.JobSeekerSave;
import naukri.portal.entity.Jobseeker;
import naukri.portal.repository.JobSeekerSaveRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {
private  final JobSeekerSaveRepo jssr;


    public JobSeekerSaveService( JobSeekerSaveRepo jssr) {
        this.jssr = jssr;
    }
    public List<JobSeekerSave> getCandidatesJob(Jobseeker userAccountId){
        return jssr.findByUserId(userAccountId);
    }
    public List<JobSeekerSave> getJobCandidates(JobPostActivity job){
        return jssr.findByJob(job);
    }

    public void addNew(JobSeekerSave jobSeekerSave) {
    jssr.save(jobSeekerSave);
    }

}
