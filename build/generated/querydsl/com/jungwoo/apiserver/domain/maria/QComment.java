package com.jungwoo.apiserver.domain.maria;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = -1982983274L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment = new QComment("comment");

    public final com.jungwoo.apiserver.domain.QBaseTimeEntity _super = new com.jungwoo.apiserver.domain.QBaseTimeEntity(this);

    public final BooleanPath available = createBoolean("available");

    public final QBoard board;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createDate = _super.createDate;

    public final NumberPath<Long> depth = createNumber("depth", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isLastComment = createBoolean("isLastComment");

    public final NumberPath<Long> leftNode = createNumber("leftNode", Long.class);

    public final QMember member;

    public final QComment parentComment;

    public final NumberPath<Long> rightNode = createNumber("rightNode", Long.class);

    public final QComment rootComment;

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> updateDate = _super.updateDate;

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.parentComment = inits.isInitialized("parentComment") ? new QComment(forProperty("parentComment"), inits.get("parentComment")) : null;
        this.rootComment = inits.isInitialized("rootComment") ? new QComment(forProperty("rootComment"), inits.get("rootComment")) : null;
    }

}

