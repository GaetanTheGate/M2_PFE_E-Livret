package m2pfe.elivret.Security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private EUserRepository ur;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EUser user = ur.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User's email \"" + email +"\" not found."));

        List<GrantedAuthority> authorities = List.of();

        return User.builder()
            .username(email)
            .password(user.getPassword())
            .authorities(authorities)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
    
}
