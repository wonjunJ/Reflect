import { defineStore } from 'pinia';

export const useDiaryStore = defineStore('diary', () => {
  const state = {
    selectedDate: null,
    diaryEntries: [
        { date: '2024-01-01', title: '일기 제목 1', keywords: ['행복', '가족', '기쁨'], content: '오늘은 즐거웠다1' },
        { date: '2024-01-02', title: '일기 제목 2', keywords: ['사랑', '친구', '즐거움'], content: '오늘은 즐거웠다2' },
        { date: '2024-01-03', title: '일기 제목 3', keywords: ['운동', '건강', '활동'], content: '오늘은 즐거웠다3' },
        { date: '2024-01-04', title: '일기 제목 4', keywords: ['공부', '성취', '목표'], content: '오늘은 즐거웠다4' },
        { date: '2024-01-05', title: '일기 제목 5', keywords: ['여행', '경험', '새로움'], content: '오늘은 즐거웠다5' },
        { date: '2024-01-06', title: '일기 제목 6', keywords: ['미술', '창의성', '아름다움'], content: '오늘은 즐거웠다6' },
        { date: '2024-01-07', title: '일기 제목 7', keywords: ['음악', '휴식', '평화'], content: '오늘은 즐거웠다7' },
      ],
  };

  const actions = {
    selectDate(date) {
      state.selectedDate = date;
    },

    getDiaryContent() {
      const selectedEntry = state.diaryEntries.find(entry => entry.date === state.selectedDate);
      return selectedEntry ? selectedEntry.content : 'No content available for the selected date.';
    },
  };

  return {
    selectedDate: () => state.selectedDate,
    diaryEntries: () => state.diaryEntries,
    selectDate: actions.selectDate,
    getDiaryContent: actions.getDiaryContent,
  };
});;