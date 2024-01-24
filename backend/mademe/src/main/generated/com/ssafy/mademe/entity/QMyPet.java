package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyPet is a Querydsl query type for MyPet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyPet extends EntityPathBase<MyPet> {

    private static final long serialVersionUID = -1280947134L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyPet myPet = new QMyPet("myPet");

    public final com.ssafy.mademe.entity.embedded.QEmdPet id;

    public final StringPath mypetName = createString("mypetName");

    public QMyPet(String variable) {
        this(MyPet.class, forVariable(variable), INITS);
    }

    public QMyPet(Path<? extends MyPet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyPet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyPet(PathMetadata metadata, PathInits inits) {
        this(MyPet.class, metadata, inits);
    }

    public QMyPet(Class<? extends MyPet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.ssafy.mademe.entity.embedded.QEmdPet(forProperty("id"), inits.get("id")) : null;
    }

}

