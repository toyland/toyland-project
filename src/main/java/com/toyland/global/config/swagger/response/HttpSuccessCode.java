package com.toyland.global.config.swagger.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 20.
 */
@Getter
@RequiredArgsConstructor
public enum HttpSuccessCode {
    // user
    // user
    USER_SIGNUP(HttpStatus.CREATED, "회원가입이 성공적으로 완료됐습니다.", "USER_201"),
    USER_LOGIN(HttpStatus.OK, "로그인에 성공했습니다.", "USER_200"),
    USER_FIND_ONE(HttpStatus.OK, "찾으신 Id를 가진 회원을 검색했습니다.", "USER_200"),
    USER_SEARCH(HttpStatus.OK, "검색 조건에 따라 회원들을 검색했습니다.", "USER_200"),
    USER_UPDATE(HttpStatus.OK, "유저의 정보를 수정했습니다.", "USER_200"),
    USER_DELETE(HttpStatus.NO_CONTENT, "유저가 삭제되었습니다.", "USER_204"),

    // address
    ADDRESS_CREATE(HttpStatus.CREATED, "주소 생성에 성공했습니다.", "ADDRESS_201"),
    ADDRESS_FIND_ONE(HttpStatus.OK, "주소를 하나 찾았습니다.", "ADDRESS_200"),
    ADDRESS_SEARCH(HttpStatus.OK, "주소를 조건에 따라 검색했습니다.", "ADDRESS_200"),
    ADDRESS_UPDATE(HttpStatus.OK, "주소를 수정했습니다.", "ADDRESS_200"),
    ADDRESS_DELETE(HttpStatus.NO_CONTENT, "주소를 삭제했습니다.", "ADDRESS_204"),

    // category (카테고리)
    CATEGORY_CREATE(HttpStatus.CREATED, "카테고리 생성에 성공했습니다.", "CATEGORY_201"),
    CATEGORY_FIND_ONE(HttpStatus.OK, "카테고리를 하나 찾았습니다.", "CATEGORY_200"),
    CATEGORY_SEARCH(HttpStatus.OK, "카테고리를 조건에 따라 검색했습니다.", "CATEGORY_200"),
    CATEGORY_UPDATE(HttpStatus.OK, "카테고리를 수정했습니다.", "CATEGORY_200"),
    CATEGORY_DELETE(HttpStatus.NO_CONTENT, "카테고리를 삭제했습니다.", "CATEGORY_204"),

    // order (주문)
    ORDER_CREATE(HttpStatus.CREATED, "주문 생성에 성공했습니다.", "ORDER_201"),
    ORDER_FIND_ONE(HttpStatus.OK, "주문을 하나 찾았습니다.", "ORDER_200"),
    ORDER_SEARCH(HttpStatus.OK, "주문을 조건에 따라 검색했습니다.", "ORDER_200"),
    ORDER_UPDATE(HttpStatus.OK, "주문을 수정했습니다.", "ORDER_200"),
    ORDER_DELETE(HttpStatus.NO_CONTENT, "주문을 삭제했습니다.", "ORDER_204"),

    // payment (결제)
    PAYMENT_CREATE(HttpStatus.CREATED, "결제 생성에 성공했습니다.", "PAYMENT_201"),
    PAYMENT_FIND_ONE(HttpStatus.OK, "결제를 하나 찾았습니다.", "PAYMENT_200"),
    PAYMENT_SEARCH(HttpStatus.OK, "결제를 조건에 따라 검색했습니다.", "PAYMENT_200"),
    PAYMENT_UPDATE(HttpStatus.OK, "결제를 수정했습니다.", "PAYMENT_200"),
    PAYMENT_DELETE(HttpStatus.NO_CONTENT, "결제를 삭제했습니다.", "PAYMENT_204"),

    // product (상품)
    PRODUCT_CREATE(HttpStatus.CREATED, "상품 생성에 성공했습니다.", "PRODUCT_201"),
    PRODUCT_FIND_ONE(HttpStatus.OK, "상품을 하나 찾았습니다.", "PRODUCT_200"),
    PRODUCT_SEARCH(HttpStatus.OK, "상품을 조건에 따라 검색했습니다.", "PRODUCT_200"),
    PRODUCT_UPDATE(HttpStatus.OK, "상품을 수정했습니다.", "PRODUCT_200"),
    PRODUCT_DELETE(HttpStatus.NO_CONTENT, "상품을 삭제했습니다.", "PRODUCT_204"),

    // region (지역)
    REGION_CREATE(HttpStatus.CREATED, "지역 생성에 성공했습니다.", "REGION_201"),
    REGION_FIND_ONE(HttpStatus.OK, "지역을 하나 찾았습니다.", "REGION_200"),
    REGION_SEARCH(HttpStatus.OK, "지역을 조건에 따라 검색했습니다.", "REGION_200"),
    REGION_UPDATE(HttpStatus.OK, "지역을 수정했습니다.", "REGION_200"),
    REGION_DELETE(HttpStatus.NO_CONTENT, "지역을 삭제했습니다.", "REGION_204"),

    // review (리뷰)
    REVIEW_CREATE(HttpStatus.CREATED, "리뷰 생성에 성공했습니다.", "REVIEW_201"),
    REVIEW_FIND_ONE(HttpStatus.OK, "리뷰를 하나 찾았습니다.", "REVIEW_200"),
    REVIEW_SEARCH(HttpStatus.OK, "리뷰를 조건에 따라 검색했습니다.", "REVIEW_200"),
    REVIEW_UPDATE(HttpStatus.OK, "리뷰를 수정했습니다.", "REVIEW_200"),
    REVIEW_DELETE(HttpStatus.NO_CONTENT, "리뷰를 삭제했습니다.", "REVIEW_204"),

    // store (음식점)
    STORE_CREATE(HttpStatus.CREATED, "음식점 생성에 성공했습니다.", "STORE_201"),
    STORE_FIND_ONE(HttpStatus.OK, "음식점을 하나 찾았습니다.", "STORE_200"),
    STORE_SEARCH(HttpStatus.OK, "음식점을 조건에 따라 검색했습니다.", "STORE_200"),
    STORE_UPDATE(HttpStatus.OK, "음식점을 수정했습니다.", "STORE_200"),
    STORE_DELETE(HttpStatus.NO_CONTENT, "음식점을 삭제했습니다.", "STORE_204");


    private final HttpStatus statusCode;
    private final String message;
    private final String code;
}
