package com.toyland.global.common.auditing;

import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@Component
@RequiredArgsConstructor
public class SoftDeleteListener {



    private final AuditorAwareImpl auditorAware;

    @PreRemove
    public void setDeletedFields(BaseEntity entity) {
        auditorAware.getCurrentAuditor().ifPresent(userId -> entity.addDeletedBy(userId));
    }
}

