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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionCode questionCode = new QQuestionCode("questionCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath questionName = createString("questionName");

    public final QRoom room;

    public QQuestionCode(String variable) {
        this(QuestionCode.class, forVariable(variable), INITS);
    }

    public QQuestionCode(Path<? extends QuestionCode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionCode(PathMetadata metadata, PathInits inits) {
        this(QuestionCode.class, metadata, inits);
    }

    public QQuestionCode(Class<? extends QuestionCode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

