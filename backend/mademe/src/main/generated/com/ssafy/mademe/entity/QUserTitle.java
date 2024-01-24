package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserTitle is a Querydsl query type for UserTitle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserTitle extends EntityPathBase<UserTitle> {

    private static final long serialVersionUID = 1100951100L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserTitle userTitle = new QUserTitle("userTitle");

    public final com.ssafy.mademe.entity.embedded.QEmdTitle id;

    public QUserTitle(String variable) {
        this(UserTitle.class, forVariable(variable), INITS);
    }

    public QUserTitle(Path<? extends UserTitle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserTitle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserTitle(PathMetadata metadata, PathInits inits) {
        this(UserTitle.class, metadata, inits);
    }

    public QUserTitle(Class<? extends UserTitle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.ssafy.mademe.entity.embedded.QEmdTitle(forProperty("id"), inits.get("id")) : null;
    }

}

