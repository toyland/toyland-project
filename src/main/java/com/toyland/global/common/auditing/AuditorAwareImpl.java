package com.toyland.global.common.auditing;

import com.toyland.global.config.security.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    /**
     * 자동으로 생성자, 수정자를 입력하기 위한 메서드
     * @return 인증 객체의 Id 값
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //spring security에서는 인증되지 않은 사용자를 anonymousUser로 처리
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails.getUser().getId());
        }

        return Optional.empty();
    }
}
