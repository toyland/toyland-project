package com.toyland.ai.presentation.dto;

import com.toyland.ai.model.Qna;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {

  String question;
  String answer;

  public QnaResponseDto(Qna qna) {
    this.question = qna.getQuestion();
    this.answer = qna.getAnswer();
  }
}
