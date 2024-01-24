package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTitleCode is a Querydsl query type for TitleCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTitleCode extends EntityPathBase<TitleCode> {

    private static final long serialVersionUID = -1364354732L;

    public static final QTitleCode titleCode = new QTitleCode("titleCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath titleName = createString("titleName");

    public QTitleCode(String variable) {
        super(TitleCode.class, forVariable(variable));
    }

    public QTitleCode(Path<? extends TitleCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTitleCode(PathMetadata metadata) {
        super(TitleCode.class, metadata);
    }

}

