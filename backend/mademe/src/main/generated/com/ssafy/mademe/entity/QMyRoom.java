package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyRoom is a Querydsl query type for MyRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyRoom extends EntityPathBase<MyRoom> {

    private static final long serialVersionUID = -1054586344L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyRoom myRoom = new QMyRoom("myRoom");

    public final BooleanPath bmark = createBoolean("bmark");

    public final com.ssafy.mademe.entity.embedded.QEmdRoom id;

    public final DateTimePath<java.time.LocalDateTime> inDate = createDateTime("inDate", java.time.LocalDateTime.class);

    public final BooleanPath isExit = createBoolean("isExit");

    public QMyRoom(String variable) {
        this(MyRoom.class, forVariable(variable), INITS);
    }

    public QMyRoom(Path<? extends MyRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyRoom(PathMetadata metadata, PathInits inits) {
        this(MyRoom.class, metadata, inits);
    }

    public QMyRoom(Class<? extends MyRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.ssafy.mademe.entity.embedded.QEmdRoom(forProperty("id"), inits.get("id")) : null;
    }

}

