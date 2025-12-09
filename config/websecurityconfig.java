package naukri.portal.config;
import naukri.portal.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class websecurityconfig {
    private final CustomUserDetailService customuserdetail;
    private  final customhandler customhand;
@Autowired
    public websecurityconfig(CustomUserDetailService customuserdetail, customhandler customhand) {
        this.customuserdetail = customuserdetail;
        this.customhand=customhand;
    }

    private final String[] purl={ "/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error" };
    @Bean
    protected SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        http.authenticationProvider(authenticationProvider());
        http.authorizeHttpRequests(auth ->{auth.requestMatchers(purl).permitAll();
        auth.anyRequest().authenticated();});
        http.formLogin(form -> form.loginPage("/login").permitAll().successHandler(customhand)).logout(logout-> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                }).cors(Customizer.withDefaults()).csrf(csrf->csrf.disable());
    return http.build(); }
@Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider aupro = new DaoAuthenticationProvider();
        aupro.setPasswordEncoder(passwordEncoder());
        aupro.setUserDetailsService(customuserdetail);
        return aupro;
    }
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
}



