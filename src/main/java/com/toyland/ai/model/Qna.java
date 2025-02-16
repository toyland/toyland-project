package com.toyland.ai.model;

import com.toyland.ai.presentation.dto.QnaRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity(name="p_aiqna")
@Getter
@NoArgsConstructor
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID aiId;

    @Column
    private String aiName;

    @Column
    private String question;

    @Column
    private String answer;

    @Column(name = "store_id")
    private UUID storeId;

    public Qna(String question, String answer, UUID storeId) {
        this.question = question;
        this.answer = answer;
        this.aiName = "OpenAI"; // 기본값 설정
        this.storeId = storeId;
    }

    public static Qna of(QnaRequestDto qnaRequestDto) {
        return new Qna(
                qnaRequestDto.getQuestion(),
                (qnaRequestDto.getAnswer() != null) ? qnaRequestDto.getAnswer() : "No answer provided",
                qnaRequestDto.getStoreId()
        );
    }

}

