package com.toyland.global.common.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;




/**
 * @EntityListeners(SoftDeleteListener.class)
 * @SQLDelete(sql = "UPDATE ** SET deleted_at = NOW(), deleted_by = ? WHERE id = ?")
 * @Where(clause = "deleted_at IS NULL")
 * 상속 받고 위 어노테이션 달아주세요! ** 부분은 엔티티에 맞게 맞춰주세요.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    private Long updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Long deletedBy;


    private LocalDateTime deletedAt;

    public void addDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

}
