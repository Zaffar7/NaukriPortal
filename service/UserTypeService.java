package naukri.portal.service;

import naukri.portal.entity.UsersType;
import naukri.portal.repository.UserTypeRepo;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserTypeService {
    private final UserTypeRepo usertyperepo;

    public UserTypeService(UserTypeRepo usertyperepo){
        this.usertyperepo= usertyperepo;
    }
    public List<UsersType> getall(){
        return usertyperepo.findAll();
    }
}
