package com.ssafy.mademe.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    private Long questionId;
    private String answer;
}
