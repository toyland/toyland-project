package com.toyland.global.common.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * @SQLRestriction("deleted_at IS NULL") 상속 받고 위 어노테이션 달아주세요! Pull Request에 있는 방법에 따라 Delete 부분
 * 구현해주세요
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

    public void addDeletedField(Long deletedBy) {
        this.delete(LocalDateTime.now(), deletedBy);
    }

    public void delete(LocalDateTime deleteAt, Long deletedBy) {
        this.deletedAt = deleteAt;
        this.deletedBy = deletedBy;
    }

}
