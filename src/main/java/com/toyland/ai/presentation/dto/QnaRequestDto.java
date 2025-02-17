package com.toyland.ai.presentation.dto;

import com.toyland.store.model.entity.Store;
import lombok.Getter;


@Getter
public class QnaRequestDto {

  String question;
  String answer;
  Store storeId;

  public void setAnswer(String answer) {
    this.answer = answer;
  }

}
