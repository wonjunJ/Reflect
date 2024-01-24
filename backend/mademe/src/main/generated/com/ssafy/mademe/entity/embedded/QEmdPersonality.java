package com.ssafy.mademe.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmdPersonality is a Querydsl query type for EmdPersonality
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QEmdPersonality extends BeanPath<EmdPersonality> {

    private static final long serialVersionUID = 941103845L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmdPersonality emdPersonality = new QEmdPersonality("emdPersonality");

    public final com.ssafy.mademe.entity.QPetPersonality petPersonality;

    public final com.ssafy.mademe.entity.QUser user;

    public QEmdPersonality(String variable) {
        this(EmdPersonality.class, forVariable(variable), INITS);
    }

    public QEmdPersonality(Path<? extends EmdPersonality> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmdPersonality(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmdPersonality(PathMetadata metadata, PathInits inits) {
        this(EmdPersonality.class, metadata, inits);
    }

    public QEmdPersonality(Class<? extends EmdPersonality> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.petPersonality = inits.isInitialized("petPersonality") ? new com.ssafy.mademe.entity.QPetPersonality(forProperty("petPersonality")) : null;
        this.user = inits.isInitialized("user") ? new com.ssafy.mademe.entity.QUser(forProperty("user")) : null;
    }

}

