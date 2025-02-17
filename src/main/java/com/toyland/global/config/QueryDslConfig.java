package com.toyland.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 17.
 */
@Component
public class QueryDslConfig {

    @Bean
    private JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

}
