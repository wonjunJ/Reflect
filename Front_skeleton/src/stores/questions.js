import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useQuestionsStore = defineStore('questions', () => {
  const questions = [
    { id: 1, question: '내 이름의 뜻은?' },
    { id: 2, question: '내 생일은?' },
    { id: 3, question: '내 키는?' },
    { id: 4, question: '내 최근 관심사는?' },
    { id: 5, question: '집에서 심심할 때 하는 일은?' },
    { id: 6, question: '아침에 일어나서 제일 먼저 하는 일은?' },
    { id: 7, question: '내가 좋아하는 가수는?' },
    { id: 8, question: '내가 좋아하는 배우는?' },
    { id: 9, question: '요즘 자주 듣는 노래는?' },
    { id: 10, question: '내 인생 노래는?' },
    { id: 11, question: '내 인생 드라마는?' },
    { id: 12, question: '넷플릭스에서 추천하는 것은?' },
    { id: 13, question: '내가 좋아하는 음식은?' },
    { id: 14, question: '내가 싫어하는 음식은?' },
    { id: 15, question: '내 이상형은?' },
    { id: 16, question: '나랑 친해지는 방법은?' },
    { id: 17, question: '요즘 가장 하고 싶은 것은?' },
    { id: 18, question: '연상 vs 동갑 vs 연하 중 선호하는 것은?' },
    { id: 19, question: '내 목표 또는 꿈은?' },
    { id: 20, question: '자기 전에 마지막으로 하는 일은?' },
    { id: 21, question: '내 MBTI는?' },
    { id: 22, question: '내 습관이나 버릇은?' },
    { id: 23, question: '내 취미는?' },
    { id: 24, question: '내 특기는?' },
    { id: 25, question: '내가 좋아하는 계절은?' },
    { id: 26, question: '낮과 밤 중 내가 가장 활발한 시간은?' },
    { id: 27, question: '즐겨보는 유튜버는?' },
    { id: 28, question: '내가 좋아하는 색깔은?' },
    { id: 29, question: '내가 좋아하는 과일은?' },
    { id: 30, question: '내가 좋아하는 반찬은?' },
    { id: 31, question: '지금 카톡 프로필 사진은?' },
    { id: 32, question: '내 성격을 한 마디로 표현하면?' },
    { id: 33, question: '가장 오래 연애한 기간은?' },
    { id: 34, question: '가장 짧게 연애한 기간은?' },
    { id: 35, question: '잠이 안 올 때 하는 것은?' },
    { id: 36, question: '내가 좋아하는 라면은?' },
    { id: 37, question: '답장 평균 시간은?' },
    { id: 38, question: '아침식사 하는지 여부는?' },
    { id: 39, question: '낯가림이 있는지?' },
    { id: 40, question: '단 음식의 호, 불호는?' },
    { id: 41, question: '매운 음식의 호, 불호는?' },
    { id: 42, question: '노래방에서 항상 부르는 노래는?' },
    { id: 43, question: '가장 무서운 것은?' },
    { id: 44, question: '민트초코의 호, 불호는?' },
    { id: 45, question: '가장 좋아하는 배라 맛은?' },
    { id: 46, question: '전화 vs 문자는?' },
    { id: 47, question: '스트레스 해소법은?' },
    { id: 48, question: '인생 좌우명은?' },
    { id: 49, question: '가장 많이 사용하는 카메라 어플은?' },
    { id: 50, question: '최근 웃긴 말이나 짤은?' },
    { id: 51, question: '내가 가진 것 중 가장 비싼 물건은?' },
    { id: 52, question: '내가 가진 것 중 가장 오래된 물건은?' },
    { id: 53, question: '내가 가진 것 중 가장 예쁜 물건은?' },
    { id: 54, question: '내가 가진 것 중 가장 쓸모 없는 물건은?' },
    { id: 55, question: '가장 아끼는 물건은?' },
    { id: 56, question: '넷플릭스에서 추천하는 프로그램은?' },
    { id: 57, question: '내가 좋아하는 옷 브랜드는?' },
    { id: 58, question: '내 추억이 담긴 물건은?' },
    { id: 59, question: '내가 사용하는 SNS는?' },
    { id: 60, question: '내 게임 닉네임은?' },
    { id: 61, question: '내 게임 닉네임의 뜻은?' },
    { id: 62, question: '돈을 얻으면 가장 많이 쓰고 싶은 곳은?' },
    { id: 63, question: '죽을 때 남기고 싶은 유언은?' },
    { id: 64, question: '내 묘비명은?' },
    { id: 65, question: '친구가 죽으면 내가 가장 먼저 할 행동은?' },
    { id: 66, question: '물냉 vs 비냉은?' },
    { id: 67, question: '내가 잘하는 음식은?' },
    { id: 68, question: '가장 좋아하는 영화는?' },
    { id: 69, question: '내 별명은?' },
    { id: 70, question: '싫어하는 색깔은?' },
    { id: 71, question: '가족 관계는?' },
    { id: 72, question: '내 혈액형은?' },
    { id: 73, question: '내 발사이즈는?' },
    { id: 74, question: '요즘 갖고 싶은 물건은?' },
    { id: 75, question: '내가 결혼하고 싶은 나이는?' },
    { id: 76, question: '내가 좋아하는 꽃은?' },
    { id: 77, question: '요즘 가장 심각한 고민은?' },
    { id: 78, question: '내가 가장 아팠을 때는?' },
    { id: 79, question: '친구가 약속 시간에 1시간 늦었을 때 나는?' },
    { id: 80, question: '가장 좋아하는 동물은?' },
    { id: 81, question: '내가 가진 기억 중 가장 어릴 때의 기억은?' },
    { id: 82, question: '내가 가진 기억 중 가장 무서웠던 기억은?' },
    { id: 83, question: '내가 가진 기억 중 가장 신기한 기억은?' },
    { id: 84, question: '내가 가진 기억 중 가장 웃긴 기억은?' },
    { id: 85, question: '남들에 비해 잘하는 것은?' },
    { id: 86, question: '남들에 비해 못하는 것은?' },
    { id: 87, question: '주량은?' },
    { id: 88, question: '나의 TMI는?' },
    { id: 89, question: '백만원 생기면 하고 싶은 것은?' },
    { id: 90, question: '하루 식사 횟수는?' },
    { id: 91, question: '나의 종교는?' },
    { id: 92, question: '좋아하는 단어는?' },
    { id: 93, question: '내가 자주 쓰는 말은?' },
    { id: 94, question: '내가 좋아하는 공간은?' },
    { id: 95, question: '여행 간다면 가고 싶은 지역 또는 나라는?' },
    { id: 96, question: '무인도에 가져갈 3가지는?' },
    { id: 97, question: '내가 갖고 싶은 초능력은?' },
    { id: 98, question: '죽어서 영혼이 되면 가장 먼저 하고 싶은 일은?' },
    { id: 99, question: '바라는 인간상은?' },
    { id: 100, question: '항상 갖고 다니는 소지품은?' },
  ]
  return { questions }
})