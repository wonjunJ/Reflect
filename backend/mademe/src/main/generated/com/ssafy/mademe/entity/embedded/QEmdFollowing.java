package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdFollowing is a Querydsl query type for EmdFollowing
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdFollowing extends BeanPath<EmdFollowing> {

    private static final long serialVersionUID = -801377080L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdFollowing emdFollowing = new QEmdFollowing("emdFollowing");

    public final com.ssafy.mademe.entity.QUser followId;

    public final com.ssafy.mademe.entity.QUser userId;

    public QEmdFollowing(String variable) {
        this(EmdFollowing.class, forVariable(variable), INITS);
    }

    public QEmdFollowing(Path<? extends EmdFollowing> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdFollowing(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdFollowing(PathMetadata metadata, PathInits inits) {
        this(EmdFollowing.class, metadata, inits);
    }

    public QEmdFollowing(Class<? extends EmdFollowing> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.followId = inits.isInitialized("followId") ? new com.ssafy.mademe.entity.QUser(forProperty("followId")) : null;
        this.userId = inits.isInitialized("userId") ? new com.ssafy.mademe.entity.QUser(forProperty("userId")) : null;
    }

}

