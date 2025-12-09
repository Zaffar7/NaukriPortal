package naukri.portal.service;

import naukri.portal.entity.*;
import naukri.portal.repository.JobPostActivityRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class JobPostActivityService {
private final JobPostActivityRepo jpar;

    public JobPostActivityService(JobPostActivityRepo jpar) {
        this.jpar = jpar;
    }
    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jpar.save(jobPostActivity);
    }
    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
        List<IRecruiterJobs> recruiterJobsDtos= jpar.getRecruiterJobs(recruiter);
        List<RecruiterJobsDto> recruiterJobsDtoList= new ArrayList<>();
        for(IRecruiterJobs rec: recruiterJobsDtos){
            JobLocation loc = new JobLocation(rec.getLocationId(), rec.getCity(), rec.getState(), rec.getCountry());
            JobCompany com =new JobCompany(rec.getCompanyId(),rec.getName(),"");
          recruiterJobsDtoList.add(new RecruiterJobsDto(rec.getTotalCandidates(),rec.getJob_post_id(), rec.getJob_title(),loc,com));

        }
        return  recruiterJobsDtoList;
    }

    public JobPostActivity getOne(int id) {
       return  jpar.findById(id).orElseThrow(()-> new RuntimeException("Job not found"));
    }

    public List<JobPostActivity> getAll() {
    return jpar.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {
    return Objects.isNull(searchDate)?jpar.searchWithoutDate(job,location,remote,type):jpar.search(job,location,remote,type,searchDate);
    }
}
