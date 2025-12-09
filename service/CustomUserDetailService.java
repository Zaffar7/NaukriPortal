package naukri.portal.service;

import naukri.portal.entity.Users;
import naukri.portal.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import naukri.portal.util.CustomUserDetail;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private  final UserRepo userepo;
   @Autowired
    public CustomUserDetailService(UserRepo userepo) {
        this.userepo = userepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  Users users= userepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("not found user"));
        return new CustomUserDetail(users);
    }
}
