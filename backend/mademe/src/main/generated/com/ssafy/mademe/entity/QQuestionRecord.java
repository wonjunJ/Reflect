package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionRecord is a Querydsl query type for QuestionRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionRecord extends EntityPathBase<QuestionRecord> {

    private static final long serialVersionUID = -1972728760L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionRecord questionRecord = new QQuestionRecord("questionRecord");

    public final StringPath answer = createString("answer");

    public final com.ssafy.mademe.entity.embedded.QEmdQuestion compositeKey;

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updateDate = createDateTime("updateDate", java.time.LocalDateTime.class);

    public QQuestionRecord(String variable) {
        this(QuestionRecord.class, forVariable(variable), INITS);
    }

    public QQuestionRecord(Path<? extends QuestionRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionRecord(PathMetadata metadata, PathInits inits) {
        this(QuestionRecord.class, metadata, inits);
    }

    public QQuestionRecord(Class<? extends QuestionRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.compositeKey = inits.isInitialized("compositeKey") ? new com.ssafy.mademe.entity.embedded.QEmdQuestion(forProperty("compositeKey"), inits.get("compositeKey")) : null;
    }

}

