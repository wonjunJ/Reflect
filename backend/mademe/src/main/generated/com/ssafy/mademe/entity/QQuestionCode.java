package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionCode is a Querydsl query type for QuestionCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionCode extends EntityPathBase<QuestionCode> {

    private static final long serialVersionUID = 234381220L;

    public static final QQuestionCode questionCode = new QQuestionCode("questionCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath questionName = createString("questionName");

    public final ListPath<Room, QRoom> rooms = this.<Room, QRoom>createList("rooms", Room.class, QRoom.class, PathInits.DIRECT2);

    public QQuestionCode(String variable) {
        super(QuestionCode.class, forVariable(variable));
    }

    public QQuestionCode(Path<? extends QuestionCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionCode(PathMetadata metadata) {
        super(QuestionCode.class, metadata);
    }

}

