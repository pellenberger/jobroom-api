package ch.admin.seco.jobroom.security;

import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthenticationService implements UserDetailsService {

    private static final int IS_ACTIVE = 1;
    private static final String ROLE = "ROLE_USER";

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        GrantedAuthority authority = new SimpleGrantedAuthority(ROLE);

        RestAccessKey restAccessKey = restAccessKeyRepository.findByOwnerNameAndActive(username, IS_ACTIVE);

        UserDetails userDetails = new User(restAccessKey.getOwnerName(), restAccessKey.getAccessKey(), Arrays.asList(authority));

        return userDetails;
    }
}
