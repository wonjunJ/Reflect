package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHashCode is a Querydsl query type for HashCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHashCode extends EntityPathBase<HashCode> {

    private static final long serialVersionUID = 1142427980L;

    public static final QHashCode hashCode = new QHashCode("hashCode");

    public final StringPath hashName = createString("hashName");

    public final ListPath<HashTag, QHashTag> hashTagRooms = this.<HashTag, QHashTag>createList("hashTagRooms", HashTag.class, QHashTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QHashCode(String variable) {
        super(HashCode.class, forVariable(variable));
    }

    public QHashCode(Path<? extends HashCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHashCode(PathMetadata metadata) {
        super(HashCode.class, metadata);
    }

}

