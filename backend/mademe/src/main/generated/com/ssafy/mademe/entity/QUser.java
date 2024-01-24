package com.ssafy.mademe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1067291004L;

    public static final QUser user = new QUser("user");

    public final ListPath<Alram, QAlram> alrams = this.<Alram, QAlram>createList("alrams", Alram.class, QAlram.class, PathInits.DIRECT2);

    public final StringPath birthday = createString("birthday");

    public final ListPath<Diary, QDiary> diaries = this.<Diary, QDiary>createList("diaries", Diary.class, QDiary.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<Following, QFollowing> followings = this.<Following, QFollowing>createList("followings", Following.class, QFollowing.class, PathInits.DIRECT2);

    public final NumberPath<Integer> gender = createNumber("gender", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Interest, QInterest> interests = this.<Interest, QInterest>createList("interests", Interest.class, QInterest.class, PathInits.DIRECT2);

    public final BooleanPath isWithdrawal = createBoolean("isWithdrawal");

    public final DateTimePath<java.time.LocalDateTime> joinDate = createDateTime("joinDate", java.time.LocalDateTime.class);

    public final ListPath<OMBTICnt, QOMBTICnt> mbtiEval = this.<OMBTICnt, QOMBTICnt>createList("mbtiEval", OMBTICnt.class, QOMBTICnt.class, PathInits.DIRECT2);

    public final ListPath<MBTILog, QMBTILog> mbtiLogs = this.<MBTILog, QMBTILog>createList("mbtiLogs", MBTILog.class, QMBTILog.class, PathInits.DIRECT2);

    public final StringPath myIntro = createString("myIntro");

    public final ListPath<MyPet, QMyPet> myPets = this.<MyPet, QMyPet>createList("myPets", MyPet.class, QMyPet.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath nickName = createString("nickName");

    public final ListPath<MyPetPersonality, QMyPetPersonality> personalities = this.<MyPetPersonality, QMyPetPersonality>createList("personalities", MyPetPersonality.class, QMyPetPersonality.class, PathInits.DIRECT2);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath profileImg = createString("profileImg");

    public final StringPath pw = createString("pw");

    public final ListPath<QuestionRecord, QQuestionRecord> qRecords = this.<QuestionRecord, QQuestionRecord>createList("qRecords", QuestionRecord.class, QQuestionRecord.class, PathInits.DIRECT2);

    public final NumberPath<Integer> recordTime = createNumber("recordTime", Integer.class);

    public final ListPath<MyRoom, QMyRoom> roomList = this.<MyRoom, QMyRoom>createList("roomList", MyRoom.class, QMyRoom.class, PathInits.DIRECT2);

    public final StringPath siteId = createString("siteId");

    public final ListPath<UserTitle, QUserTitle> userTitles = this.<UserTitle, QUserTitle>createList("userTitles", UserTitle.class, QUserTitle.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

