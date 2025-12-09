package naukri.portal.repository;

import naukri.portal.entity.JobPostActivity;
import naukri.portal.entity.JobSeekerSave;
import naukri.portal.entity.Jobseeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepo extends JpaRepository<JobSeekerSave , Integer> {
 List<JobSeekerSave> findByUserId(Jobseeker userAccountId);
  List<JobSeekerSave> findByJob(JobPostActivity job);

}
