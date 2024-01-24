package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyPetPersonality is a Querydsl query type for MyPetPersonality
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyPetPersonality extends EntityPathBase<MyPetPersonality> {

    private static final long serialVersionUID = -1415362356L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyPetPersonality myPetPersonality = new QMyPetPersonality("myPetPersonality");

    public final com.ssafy.mademe.entity.embedded.QEmdPersonality id;

    public final BooleanPath selected = createBoolean("selected");

    public QMyPetPersonality(String variable) {
        this(MyPetPersonality.class, forVariable(variable), INITS);
    }

    public QMyPetPersonality(Path<? extends MyPetPersonality> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyPetPersonality(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyPetPersonality(PathMetadata metadata, PathInits inits) {
        this(MyPetPersonality.class, metadata, inits);
    }

    public QMyPetPersonality(Class<? extends MyPetPersonality> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.ssafy.mademe.entity.embedded.QEmdPersonality(forProperty("id"), inits.get("id")) : null;
    }

}

