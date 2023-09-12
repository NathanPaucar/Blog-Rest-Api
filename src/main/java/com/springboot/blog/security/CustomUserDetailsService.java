package com.springboot.blog.security;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // check if the user exits, if so convert user's roles into a Set of GrantedAuthority objects


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // retrieve user, if user is not found, throw exception - exception does not get saved to "user"
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));

        // retrieve the users Roles and stream each element through a stream, like opening a gateway to convert all role elements

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role)
                        // convert user roles into a set of authorities, spring can use to give user the access they have
                        -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        // this will return the email, password and the amount of access spring can give them
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }
}

// CustomUserDetailsService will go into the database and retrieve the users information by either the name or email,
// and then it's store that information along with how their role and return that information for the application to use.
//then comes AuthServiceImp class