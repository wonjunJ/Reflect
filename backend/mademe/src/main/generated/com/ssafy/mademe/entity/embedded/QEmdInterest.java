package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdInterest is a Querydsl query type for EmdInterest
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdInterest extends BeanPath<EmdInterest> {

    private static final long serialVersionUID = 104202771L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdInterest emdInterest = new QEmdInterest("emdInterest");

    public final com.ssafy.mademe.entity.QInterestCode interestCode;

    public final EnumPath<com.ssafy.mademe.entity.Preference> preference = createEnum("preference", com.ssafy.mademe.entity.Preference.class);

    public QEmdInterest(String variable) {
        this(EmdInterest.class, forVariable(variable), INITS);
    }

    public QEmdInterest(Path<? extends EmdInterest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdInterest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdInterest(PathMetadata metadata, PathInits inits) {
        this(EmdInterest.class, metadata, inits);
    }

    public QEmdInterest(Class<? extends EmdInterest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.interestCode = inits.isInitialized("interestCode") ? new com.ssafy.mademe.entity.QInterestCode(forProperty("interestCode")) : null;
    }

}

