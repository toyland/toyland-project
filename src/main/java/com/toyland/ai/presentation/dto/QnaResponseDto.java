package com.toyland.ai.presentation.dto;

import com.toyland.ai.model.Qna;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnaResponseDto {

  private UUID qnaId;
  private String question;
  private String answer;

  public QnaResponseDto(UUID qnaId, String question, String answer) {
    this.qnaId = qnaId;
    this.question = question;
    this.answer = answer;
  }

  public static QnaResponseDto of(Qna qna) {
    return new QnaResponseDto(
        qna.getAiId(),
        qna.getQuestion(),
        qna.getAnswer()
    );
  }


}
