package com.toyland.ai.model;

import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.global.common.auditing.BaseEntity;
import com.toyland.store.model.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;


@SQLRestriction("deleted_at IS NULL")
@Entity(name = "p_aiqna")
@Getter
@NoArgsConstructor
public class Qna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID aiId;

    @Column
    private String aiName;

    @Column
    private String question;

    @Column
    private String answer;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;


    public Qna(String question, String answer, Store store) {
        this.question = question;
        this.answer = answer;
        this.aiName = "OpenAI"; // 기본값 설정
        this.store = store;
    }


  public static Qna from(QnaRequestDto qnaRequestDto, Store store) {
    return new Qna(
        qnaRequestDto.getQuestion(),
        (qnaRequestDto.getAnswer() != null) ? qnaRequestDto.getAnswer() : "No answer provided",
        store
    );
  }


  public void update(QnaRequestDto request) {
    this.question = request.getQuestion();
    this.answer = request.getAnswer();
  }


}

