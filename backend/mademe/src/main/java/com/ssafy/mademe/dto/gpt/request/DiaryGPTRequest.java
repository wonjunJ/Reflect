package com.ssafy.mademe.dto.gpt.request;

import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.Preference;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryGPTRequest {
    private DailyData dailyData; // dailyData 키에 대응하는 객체
    private List<MBTIData> mbtiData; // mbtiData 키에 대응하는 리스트
    private List<EmotionData> emotionData; // emotionData 키에 대응하는 리스트

    // Nested class for dailyData
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyData {
        private List<InterestData> interestData;
        private List<String> keywords;
        private int daily_score;
        private String daily_emotion;
        private String content;
        // getters, setters, and other inner classes

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class InterestData {
            private String interest;
            private Preference preference; // LIKE or UNLIKE
            // getters and setters
        }
    }

    // Nested class for mbtiData
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MBTIData {
        private String mbti;
        private List<String> sentences;
        // getters and setters
    }

    // Nested class for emotionData
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmotionData {
        private String emotion;
        private List<String> sentences;
        // getters and setters
    }
}