package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMBTILog is a Querydsl query type for MBTILog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMBTILog extends EntityPathBase<MBTILog> {

    private static final long serialVersionUID = 93639913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMBTILog mBTILog = new QMBTILog("mBTILog");

    public final QDiary diary;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<MBTICode> mbtiCode = createEnum("mbtiCode", MBTICode.class);

    public final StringPath sentence = createString("sentence");

    public final QUser user;

    public QMBTILog(String variable) {
        this(MBTILog.class, forVariable(variable), INITS);
    }

    public QMBTILog(Path<? extends MBTILog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMBTILog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMBTILog(PathMetadata metadata, PathInits inits) {
        this(MBTILog.class, metadata, inits);
    }

    public QMBTILog(Class<? extends MBTILog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.diary = inits.isInitialized("diary") ? new QDiary(forProperty("diary"), inits.get("diary")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

