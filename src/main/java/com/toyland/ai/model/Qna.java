package com.toyland.ai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity(name="q_aiqna")
@Getter
@Setter
public class Qna {

    @Id
    private UUID ai_id = UUID.randomUUID();

    @Column(name="ai_name")
    private String aiName;

    @Column
    private String question;

    @Column
    private String answer;

    @Column(name = "store_id")
    private UUID storeId;


    public Qna() {
        this.aiName = "OpenAI";
    }

}

