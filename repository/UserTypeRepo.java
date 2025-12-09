package naukri.portal.repository;


import naukri.portal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepo extends JpaRepository<UsersType,Integer> {
}
