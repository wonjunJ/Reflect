package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdTitle is a Querydsl query type for EmdTitle
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdTitle extends BeanPath<EmdTitle> {

    private static final long serialVersionUID = 980998159L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdTitle emdTitle = new QEmdTitle("emdTitle");

    public final com.ssafy.mademe.entity.QTitleCode tCode;

    public final com.ssafy.mademe.entity.QUser user;

    public QEmdTitle(String variable) {
        this(EmdTitle.class, forVariable(variable), INITS);
    }

    public QEmdTitle(Path<? extends EmdTitle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdTitle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdTitle(PathMetadata metadata, PathInits inits) {
        this(EmdTitle.class, metadata, inits);
    }

    public QEmdTitle(Class<? extends EmdTitle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tCode = inits.isInitialized("tCode") ? new com.ssafy.mademe.entity.QTitleCode(forProperty("tCode")) : null;
        this.user = inits.isInitialized("user") ? new com.ssafy.mademe.entity.QUser(forProperty("user")) : null;
    }

}

