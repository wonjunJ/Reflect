package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmotionCode is a Querydsl query type for EmotionCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmotionCode extends EntityPathBase<EmotionCode> {

    private static final long serialVersionUID = 1236295191L;

    public static final QEmotionCode emotionCode = new QEmotionCode("emotionCode");

    public final ListPath<Diary, QDiary> diaries = this.<Diary, QDiary>createList("diaries", Diary.class, QDiary.class, PathInits.DIRECT2);

    public final ListPath<EmotionLog, QEmotionLog> emotionLogs = this.<EmotionLog, QEmotionLog>createList("emotionLogs", EmotionLog.class, QEmotionLog.class, PathInits.DIRECT2);

    public final StringPath emotionName = createString("emotionName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QEmotionCode(String variable) {
        super(EmotionCode.class, forVariable(variable));
    }

    public QEmotionCode(Path<? extends EmotionCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmotionCode(PathMetadata metadata) {
        super(EmotionCode.class, metadata);
    }

}

