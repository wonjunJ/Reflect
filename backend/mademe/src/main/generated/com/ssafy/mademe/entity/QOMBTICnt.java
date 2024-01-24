package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOMBTICnt is a Querydsl query type for OMBTICnt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOMBTICnt extends EntityPathBase<OMBTICnt> {

    private static final long serialVersionUID = -1534168031L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOMBTICnt oMBTICnt = new QOMBTICnt("oMBTICnt");

    public final NumberPath<Integer> cnt = createNumber("cnt", Integer.class);

    public final com.ssafy.mademe.entity.embedded.QEmdOMBTI compositeKey;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QOMBTICnt(String variable) {
        this(OMBTICnt.class, forVariable(variable), INITS);
    }

    public QOMBTICnt(Path<? extends OMBTICnt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOMBTICnt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOMBTICnt(PathMetadata metadata, PathInits inits) {
        this(OMBTICnt.class, metadata, inits);
    }

    public QOMBTICnt(Class<? extends OMBTICnt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.compositeKey = inits.isInitialized("compositeKey") ? new com.ssafy.mademe.entity.embedded.QEmdOMBTI(forProperty("compositeKey"), inits.get("compositeKey")) : null;
    }

}

