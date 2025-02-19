package com.toyland.ai.presentation.dto;

import com.toyland.ai.model.Qna;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {

  private UUID qnaID;
  private String question;
  private String answer;

  public QnaResponseDto(Qna qna) {
    this.qnaID = qna.getAiId();
    this.question = qna.getQuestion();
    this.answer = qna.getAnswer();
  }

  public static QnaResponseDto of(Qna qna) {
    return new QnaResponseDto(
        qna.getAiId(),
        qna.getQuestion(),
        qna.getAnswer()
    );
  }


}
