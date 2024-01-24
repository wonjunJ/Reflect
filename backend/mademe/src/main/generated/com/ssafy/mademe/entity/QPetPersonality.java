package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPetPersonality is a Querydsl query type for PetPersonality
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetPersonality extends EntityPathBase<PetPersonality> {

    private static final long serialVersionUID = -1030151104L;

    public static final QPetPersonality petPersonality = new QPetPersonality("petPersonality");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath perType = createString("perType");

    public QPetPersonality(String variable) {
        super(PetPersonality.class, forVariable(variable));
    }

    public QPetPersonality(Path<? extends PetPersonality> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPetPersonality(PathMetadata metadata) {
        super(PetPersonality.class, metadata);
    }

}

