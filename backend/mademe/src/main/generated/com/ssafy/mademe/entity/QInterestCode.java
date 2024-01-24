package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInterestCode is a Querydsl query type for InterestCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterestCode extends EntityPathBase<InterestCode> {

    private static final long serialVersionUID = 1451657448L;

    public static final QInterestCode interestCode = new QInterestCode("interestCode");

    public final StringPath hobbyName = createString("hobbyName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QInterestCode(String variable) {
        super(InterestCode.class, forVariable(variable));
    }

    public QInterestCode(Path<? extends InterestCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInterestCode(PathMetadata metadata) {
        super(InterestCode.class, metadata);
    }

}

