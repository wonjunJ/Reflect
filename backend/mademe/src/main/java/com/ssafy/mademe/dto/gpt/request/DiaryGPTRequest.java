package com.ssafy.mademe.dto.gpt.request;

import com.ssafy.mademe.entity.MBTICode;
import com.ssafy.mademe.entity.Preference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryGPTRequest {
    private DailyData dailyData; // dailyData 키에 대응하는 객체
    private List<MBTIData> mbtiData; // mbtiData 키에 대응하는 리스트
    private List<EmotionData> emotionData; // emotionData 키에 대응하는 리스트

    // Nested class for dailyData
    public static class DailyData {
        private List<InterestData> interestData;
        private List<String> keywords;
        private int dailyScore;
        private String dailyEmotion;
        private String content;
        // getters, setters, and other inner classes

        public static class InterestData {
            private String interest;
            private Preference preference; // LIKE or UNLIKE
            // getters and setters
        }
    }

    // Nested class for mbtiData
    public static class MBTIData {
        private MBTICode mbti;
        private List<String> sentences;
        // getters and setters
    }

    // Nested class for emotionData
    public static class EmotionData {
        private String emotion;
        private List<String> sentences;
        // getters and setters
    }
}