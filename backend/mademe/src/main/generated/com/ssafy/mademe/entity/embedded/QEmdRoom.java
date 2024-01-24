package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdRoom is a Querydsl query type for EmdRoom
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdRoom extends BeanPath<EmdRoom> {

    private static final long serialVersionUID = -661145532L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdRoom emdRoom = new QEmdRoom("emdRoom");

    public final com.ssafy.mademe.entity.QRoom room;

    public final com.ssafy.mademe.entity.QUser user;

    public QEmdRoom(String variable) {
        this(EmdRoom.class, forVariable(variable), INITS);
    }

    public QEmdRoom(Path<? extends EmdRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdRoom(PathMetadata metadata, PathInits inits) {
        this(EmdRoom.class, metadata, inits);
    }

    public QEmdRoom(Class<? extends EmdRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new com.ssafy.mademe.entity.QRoom(forProperty("room"), inits.get("room")) : null;
        this.user = inits.isInitialized("user") ? new com.ssafy.mademe.entity.QUser(forProperty("user")) : null;
    }

}

