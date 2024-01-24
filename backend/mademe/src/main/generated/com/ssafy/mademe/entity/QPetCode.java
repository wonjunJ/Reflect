package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPetCode is a Querydsl query type for PetCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetCode extends EntityPathBase<PetCode> {

    private static final long serialVersionUID = -507388837L;

    public static final QPetCode petCode = new QPetCode("petCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath petName = createString("petName");

    public QPetCode(String variable) {
        super(PetCode.class, forVariable(variable));
    }

    public QPetCode(Path<? extends PetCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPetCode(PathMetadata metadata) {
        super(PetCode.class, metadata);
    }

}

