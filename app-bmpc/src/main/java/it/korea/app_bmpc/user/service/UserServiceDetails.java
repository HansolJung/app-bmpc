package it.korea.app_bmpc.user.service;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.korea.app_bmpc.user.dto.UserSecureDTO;
import it.korea.app_bmpc.user.entity.UserEntity;
import it.korea.app_bmpc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceDetails implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(username)
            .orElseThrow(()-> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));

        if ("Y".equals(user.getDelYn())) {
            throw new DisabledException("해당 계정은 삭제됐습니다. 다른 계정으로 로그인하세요.");
        }

        return new UserSecureDTO(user);
    }
}
