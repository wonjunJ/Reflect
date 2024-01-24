package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdHash is a Querydsl query type for EmdHash
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdHash extends BeanPath<EmdHash> {

    private static final long serialVersionUID = -661456777L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdHash emdHash = new QEmdHash("emdHash");

    public final com.ssafy.mademe.entity.QHashCode hashCode;

    public final com.ssafy.mademe.entity.QRoom room;

    public QEmdHash(String variable) {
        this(EmdHash.class, forVariable(variable), INITS);
    }

    public QEmdHash(Path<? extends EmdHash> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdHash(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdHash(PathMetadata metadata, PathInits inits) {
        this(EmdHash.class, metadata, inits);
    }

    public QEmdHash(Class<? extends EmdHash> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hashCode = inits.isInitialized("hashCode") ? new com.ssafy.mademe.entity.QHashCode(forProperty("hashCode")) : null;
        this.room = inits.isInitialized("room") ? new com.ssafy.mademe.entity.QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

