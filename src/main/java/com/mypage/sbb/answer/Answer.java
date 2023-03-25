package com.mypage.sbb.answer;

import com.mypage.sbb.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// answer 테이블이 생김
@Getter
@Setter
@Entity
public class Answer {
    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Integer id; // INT id

    @Column(columnDefinition = "TEXT") // TEXT
    private String content;

    private LocalDateTime createDate; // DATETIME

    @ManyToOne
    private Question question;
    // `public class Answer` @ManyToOne `private Question question`
}