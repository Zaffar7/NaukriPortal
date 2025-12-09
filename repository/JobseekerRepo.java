package naukri.portal.repository;

import naukri.portal.entity.Jobseeker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobseekerRepo extends JpaRepository<Jobseeker,Integer> {
}
