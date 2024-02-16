package com.ssafy.mademe.service;

import com.ssafy.mademe.dto.request.QuestionRequest;
import com.ssafy.mademe.dto.response.QuestionResponse;
import com.ssafy.mademe.entity.QuestionCode;
import com.ssafy.mademe.entity.QuestionRecord;
import com.ssafy.mademe.entity.User;
import com.ssafy.mademe.entity.embedded.EmdQuestion;
import com.ssafy.mademe.exception.question.QAnswerNotFoundException;
import com.ssafy.mademe.repository.QuestionCodeRepository;
import com.ssafy.mademe.repository.QuestionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRecordRepository questionRecordRepository;
    private final QuestionCodeRepository questionCodeRepository;

    public List<QuestionResponse> getMyQRecords(User user){
        //답변이 null이 아닌 경우만 찾아서 리턴
        List<QuestionRecord> recordList = questionRecordRepository.findByCompositeKey_UserAndAnswerIsNotNullOrderByCompositeKeyQcodeIdAsc(user);
        //mapping함수를 통해서 QuestionRecord 타입의 리스트를 QuestionResponse 타입의 리스트로 바꿔준다.
        return recordList
                .stream()
                .map(this::mapToQResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse getMyQAnswer(Long id){
        Optional<QuestionRecord> record = questionRecordRepository.findById(id);
        if(record.isPresent() && record.get().getAnswer() != null){ //답변을 삭제했을때는 null처리가 되므로
            return mapToQResponse(record.get());
        }
        throw new QAnswerNotFoundException("해당하는 답변을 찾을 수 없습니다.");
    }

    public QuestionResponse addMyQAnswer(User user, QuestionRequest request){
        Optional<QuestionCode> code = questionCodeRepository.findById(request.getQuestionId());
        if(code.isPresent()){ //백문백답 리스트에 있는 질문이다.
            //이 질문에 과거 내가 답했던 이력이 있는지 확인하기 위해(삭제를 해도 답변이 null이 되지 내역에는 남기 때문)
            QuestionRecord qrecord = checkRecord(user, code.get());
            qrecord.setAnswer(request.getAnswer());
            QuestionRecord rst = questionRecordRepository.save(qrecord);
            return mapToQResponse(rst);
        }
        throw new QAnswerNotFoundException("해당하는 질문을 찾을 수 없습니다.");
    }

    public QuestionResponse modifyMyAnswer(User user, QuestionRequest request){
//        QuestionCode qCode = new QuestionCode();
//        qCode.setId(request.getQuestionId());
        QuestionRecord record = questionRecordRepository
                .findFirstByCompositeKey_UserAndCompositeKey_Qcode_Id(user, request.getQuestionId());
        if(record != null){
            record.setAnswer(request.getAnswer());
            QuestionRecord rst = questionRecordRepository.save(record);
            return mapToQResponse(rst);
        }
        throw new QAnswerNotFoundException("수정할 데이터가 없습니다.");
    }

    public void deleteMyAnswer(User user, QuestionRequest request){
        request.setAnswer(null);
        modifyMyAnswer(user, request);
    }

    private QuestionResponse mapToQResponse(QuestionRecord qr){
        return QuestionResponse.builder()
                .answerId(qr.getId())
                .questionId(qr.getCompositeKey().getQcode().getId())
                .updateDate(qr.getUpdateDate().toLocalDate())
                .answer(qr.getAnswer())
                .build();
    }

    private QuestionRecord checkRecord(User user, QuestionCode code){
        QuestionRecord record = questionRecordRepository
                .findFirstByCompositeKey_UserAndCompositeKey_Qcode_Id(user, code.getId());
        if(record == null){ //QuestionRecord에 이 질문에 대한 기록이 전혀 없다.(삭제를 한적도 없다.)
            QuestionRecord qr = new QuestionRecord();
            qr.setCompositeKey(new EmdQuestion(user, code));
            return qr;
        }
        return record;
    }
}
