//package com.toyland.global.config.auditing;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.NoRepositoryBean;
//
//@NoRepositoryBean
//public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
//
//    @Autowired
//    AuditorAware<Long> auditorAware;
//
//    /**
//     * soft delete를 위해 상속 받은 엔티티에 대해 BaseEntity의 SQLDelete와 Where를 실행하기 위한 메서드
//     * 인증 객체를 통해 반환 받은 유저의 아이디와 삭제 일시를 저장해서 entity에 필드 데이터 저장
//     * @param entity 상속 받아 Delete를 실행한 엔티티
//     */
//    default void deleteWithAudit(T entity) {
//        if (entity instanceof BaseEntity) {
//            auditorAware.getCurrentAuditor().ifPresent(entity::updateDeleted);
//            save(entity); // Soft Delete 적용
//        }
//    }
//}
