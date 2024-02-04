package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdQuestion is a Querydsl query type for EmdQuestion
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdQuestion extends BeanPath<EmdQuestion> {

    private static final long serialVersionUID = -1632069937L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdQuestion emdQuestion = new QEmdQuestion("emdQuestion");

    public final com.ssafy.mademe.entity.QQuestionCode qcode;

    public final com.ssafy.mademe.entity.QUser user;

    public QEmdQuestion(String variable) {
        this(EmdQuestion.class, forVariable(variable), INITS);
    }

    public QEmdQuestion(Path<? extends EmdQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdQuestion(PathMetadata metadata, PathInits inits) {
        this(EmdQuestion.class, metadata, inits);
    }

    public QEmdQuestion(Class<? extends EmdQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.qcode = inits.isInitialized("qcode") ? new com.ssafy.mademe.entity.QQuestionCode(forProperty("qcode")) : null;
        this.user = inits.isInitialized("user") ? new com.ssafy.mademe.entity.QUser(forProperty("user")) : null;
    }

}

