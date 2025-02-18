package com.toyland.ai.presentation.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class QnaRequestDto {

  UUID qnaId;
  String question;
  String answer;
  String storeId;

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public QnaRequestDto(UUID qnaId, String question, String answer, String storeId) {
    this.qnaId = qnaId;
    this.question = question;
    this.answer = answer;
    this.storeId = storeId;
  }

}
