package com.toyland.ai.model;

import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.store.model.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "q_aiqna")
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

    @ManyToOne
    @JoinColumn(name = "store_Id")
    private Store store;


    public Qna(String question, String answer, Store store) {
        this.question = question;
        this.answer = answer;
        this.aiName = "OpenAI"; // 기본값 설정
        this.store = store;
    }

    public static Qna from(QnaRequestDto qnaRequestDto) {
        return new Qna(
            qnaRequestDto.getQuestion(),
            (qnaRequestDto.getAnswer() != null) ? qnaRequestDto.getAnswer() : "No answer provided",
            qnaRequestDto.getStoreId()
        );
    }

}

