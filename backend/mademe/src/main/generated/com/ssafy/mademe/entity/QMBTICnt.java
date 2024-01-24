package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMBTICnt is a Querydsl query type for MBTICnt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMBTICnt extends EntityPathBase<MBTICnt> {

    private static final long serialVersionUID = 93631246L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMBTICnt mBTICnt = new QMBTICnt("mBTICnt");

    public final NumberPath<Integer> cnt = createNumber("cnt", Integer.class);

    public final com.ssafy.mademe.entity.embedded.QEmdMBTI compositeKey;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QMBTICnt(String variable) {
        this(MBTICnt.class, forVariable(variable), INITS);
    }

    public QMBTICnt(Path<? extends MBTICnt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMBTICnt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMBTICnt(PathMetadata metadata, PathInits inits) {
        this(MBTICnt.class, metadata, inits);
    }

    public QMBTICnt(Class<? extends MBTICnt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.compositeKey = inits.isInitialized("compositeKey") ? new com.ssafy.mademe.entity.embedded.QEmdMBTI(forProperty("compositeKey"), inits.get("compositeKey")) : null;
    }

}

