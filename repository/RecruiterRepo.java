package naukri.portal.repository;

import naukri.portal.entity.Recruiterprofile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterRepo extends JpaRepository<Recruiterprofile,Integer> {
}
