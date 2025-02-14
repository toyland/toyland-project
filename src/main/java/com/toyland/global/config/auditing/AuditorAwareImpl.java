package com.toyland.global.config.auditing;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    /**
     * 자동으로 생성자, 수정자를 입력하기 위한 메서드
     * security 구현 완료시 인증 객체에서 생성하거나 수정한 User의 Id를 삽입 하도록 리팩토링 해야함
     * @return 인증 객체의 Id 값
     */
    @Override
    public Optional<Long> getCurrentAuditor() {

        return Optional.empty();
    }
}
