package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdMBTI is a Querydsl query type for EmdMBTI
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdMBTI extends BeanPath<EmdMBTI> {

    private static final long serialVersionUID = -661338605L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdMBTI emdMBTI = new QEmdMBTI("emdMBTI");

    public final EnumPath<com.ssafy.mademe.entity.MBTICode> mbtiCode = createEnum("mbtiCode", com.ssafy.mademe.entity.MBTICode.class);

    public final com.ssafy.mademe.entity.QUser user;

    public QEmdMBTI(String variable) {
        this(EmdMBTI.class, forVariable(variable), INITS);
    }

    public QEmdMBTI(Path<? extends EmdMBTI> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdMBTI(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdMBTI(PathMetadata metadata, PathInits inits) {
        this(EmdMBTI.class, metadata, inits);
    }

    public QEmdMBTI(Class<? extends EmdMBTI> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.ssafy.mademe.entity.QUser(forProperty("user")) : null;
    }

}

