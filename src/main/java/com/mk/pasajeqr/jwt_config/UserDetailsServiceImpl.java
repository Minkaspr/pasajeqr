package com.mk.pasajeqr.jwt_config;

import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository; // Asegúrate de tener este repo

    @Override
    public UserDetails loadUserByUsername(String idOrEmail) throws UsernameNotFoundException {
        // Normalmente usas el `email`, pero como guardas el ID en el token:
        Long id = Long.parseLong(idOrEmail);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id: " + id));
        return UserDetailsImpl.build(user);
    }
}
