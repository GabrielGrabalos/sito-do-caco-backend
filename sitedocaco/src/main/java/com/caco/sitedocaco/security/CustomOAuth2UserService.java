package com.caco.sitedocaco.security.oauth2;

import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatarUrl = oAuth2User.getAttribute("picture");

        // REGRA DE NEGÓCIO: Validação de Domínio
        // if (!email.endsWith("@dominio.da.faculdade.br")) {
        //    throw new OAuth2AuthenticationException("E-mail não pertence à organização.");
        // }

        // Salva ou atualiza o usuário
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setAvatarUrl(avatarUrl); // Atualiza foto se mudou
            userRepository.save(user);
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAvatarUrl(avatarUrl);
            user.setRole(Role.STUDENT); // Padrão
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }

        return oAuth2User;
    }
}