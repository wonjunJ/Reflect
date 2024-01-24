package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdOMBTI is a Querydsl query type for EmdOMBTI
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdOMBTI extends BeanPath<EmdOMBTI> {

    private static final long serialVersionUID = 975497584L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdOMBTI emdOMBTI = new QEmdOMBTI("emdOMBTI");

    public final EnumPath<com.ssafy.mademe.entity.MBTI> mbti = createEnum("mbti", com.ssafy.mademe.entity.MBTI.class);

    public final com.ssafy.mademe.entity.QUser user;

    public QEmdOMBTI(String variable) {
        this(EmdOMBTI.class, forVariable(variable), INITS);
    }

    public QEmdOMBTI(Path<? extends EmdOMBTI> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdOMBTI(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdOMBTI(PathMetadata metadata, PathInits inits) {
        this(EmdOMBTI.class, metadata, inits);
    }

    public QEmdOMBTI(Class<? extends EmdOMBTI> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.ssafy.mademe.entity.QUser(forProperty("user")) : null;
    }

}

