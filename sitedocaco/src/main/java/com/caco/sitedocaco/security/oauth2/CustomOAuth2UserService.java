package com.caco.sitedocaco.security.oauth2;

import com.caco.sitedocaco.entity.User;
import com.caco.sitedocaco.entity.enums.Role;
import com.caco.sitedocaco.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(">>> CustomOAuth2UserService: Recebido do Google: " + oAuth2User.getAttributes()); // LOG DE DEBUG

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatarUrl = oAuth2User.getAttribute("picture");

        // TODO: Descomentar para restringir acesso por domínio de e-mail
        // REGRA DE NEGÓCIO: Validação de Domínio
//        assert email != null;
//        if (!email.endsWith("@dac.unicamp.br")) {
//            OAuth2Error oauth2Error = new OAuth2Error(
//                    "invalid_domain",
//                    "O e-mail não pertence ao domínio institucional permitido.",
//                    null
//            );
//
//            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
//        }

        // Salva ou atualiza o usuário
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        // Atualiza se existir, senão cria novo
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setAvatarUrl(avatarUrl);
            user.setUsername(name);
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(name);
            user.setAvatarUrl(avatarUrl);
            user.setRole(Role.STUDENT); // Padrão
            user.setCreatedAt(LocalDateTime.now());
        }

        userRepository.save(user);

        return oAuth2User;
    }
}