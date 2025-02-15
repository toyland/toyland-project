package com.toyland.global.config.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE {h-schema}#{#entityName} SET deleted_at = NOW(), deleted_by = ? WHERE id = ?")
@Where(clause = "deleted_at IS NULL") // 삭제된 데이터는 조회에서 제외
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

    @Column(updatable = false)
    private LocalDateTime deletedAt;


    /**
     * 삭제 식별자와 삭제 일자를 넣기 위한 메서드 같은 패키지 외부에서 불러야 하기 떄문에 Protected로 설정
     * @param userId 인증 객체에서 반환 받은 Id
     */
    protected void updateDeleted(Long userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

}
