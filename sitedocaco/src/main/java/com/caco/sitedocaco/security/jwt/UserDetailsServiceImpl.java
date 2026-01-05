package com.caco.sitedocaco.security.jwt;

import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service; // <--- ESTA ANOTAÇÃO É CRUCIAL

import java.util.Collections;

@Service // Sem isso, o Spring não encontra o Bean e dá o erro que você viu
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        // Busca o usuário no banco pelo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Converte a Role do seu Enum (STUDENT) para o padrão do Spring (ROLE_STUDENT)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // Retorna o objeto User padrão do Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "", // Senha vazia (pois usamos OAuth2/JWT, não senha local)
                Collections.singletonList(authority)
        );
    }
}