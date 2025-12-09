package naukri.portal.repository;

import naukri.portal.entity.JobPostActivity;
import naukri.portal.entity.JobSeekerApply;
import naukri.portal.entity.Jobseeker;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepo extends JpaRepository<JobSeekerApply , Integer> {

    List<JobSeekerApply> findByUserId(Jobseeker userId);

    List<JobSeekerApply> findByJob(JobPostActivity job);


}
