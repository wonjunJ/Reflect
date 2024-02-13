package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoom is a Querydsl query type for Room
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoom extends EntityPathBase<Room> {

    private static final long serialVersionUID = 1067198092L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoom room = new QRoom("room");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> curNum = createNumber("curNum", Integer.class);

    public final ListPath<HashTag, QHashTag> hashTags = this.<HashTag, QHashTag>createList("hashTags", HashTag.class, QHashTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath intro = createString("intro");

    public final QUser ownerId;

    public final ListPath<MyRoom, QMyRoom> participants = this.<MyRoom, QMyRoom>createList("participants", MyRoom.class, QMyRoom.class, PathInits.DIRECT2);

    public final QQuestionCode question;

    public final ListPath<RoomModeList, QRoomModeList> roomModes = this.<RoomModeList, QRoomModeList>createList("roomModes", RoomModeList.class, QRoomModeList.class, PathInits.DIRECT2);

    public final BooleanPath status = createBoolean("status");

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    public final NumberPath<Integer> totalNum = createNumber("totalNum", Integer.class);

    public final EnumPath<RoomType> type = createEnum("type", RoomType.class);

    public QRoom(String variable) {
        this(Room.class, forVariable(variable), INITS);
    }

    public QRoom(Path<? extends Room> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoom(PathMetadata metadata, PathInits inits) {
        this(Room.class, metadata, inits);
    }

    public QRoom(Class<? extends Room> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ownerId = inits.isInitialized("ownerId") ? new QUser(forProperty("ownerId")) : null;
        this.question = inits.isInitialized("question") ? new QQuestionCode(forProperty("question")) : null;
    }

}

