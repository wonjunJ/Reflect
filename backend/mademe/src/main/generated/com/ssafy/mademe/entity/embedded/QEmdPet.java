package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdPet is a Querydsl query type for EmdPet
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdPet extends BeanPath<EmdPet> {

    private static final long serialVersionUID = -298424170L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdPet emdPet = new QEmdPet("emdPet");

    public final com.ssafy.mademe.entity.QPetCode petCode;

    public final com.ssafy.mademe.entity.QUser user;

    public QEmdPet(String variable) {
        this(EmdPet.class, forVariable(variable), INITS);
    }

    public QEmdPet(Path<? extends EmdPet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdPet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdPet(PathMetadata metadata, PathInits inits) {
        this(EmdPet.class, metadata, inits);
    }

    public QEmdPet(Class<? extends EmdPet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.petCode = inits.isInitialized("petCode") ? new com.ssafy.mademe.entity.QPetCode(forProperty("petCode")) : null;
        this.user = inits.isInitialized("user") ? new com.ssafy.mademe.entity.QUser(forProperty("user")) : null;
    }

}

