package com.ufpb.sicred.authentication;

import com.ufpb.sicred.exceptions.UserNotFoundException;
import com.ufpb.sicred.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByNome(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

    }
}
