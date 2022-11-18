package com.jungwoo.apiserver.domain.mongo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKeyword is a Querydsl query type for Keyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeyword extends EntityPathBase<Keyword> {

    private static final long serialVersionUID = 1810674462L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKeyword keyword1 = new QKeyword("keyword1");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final org.bson.types.QObjectId id;

    public final StringPath keyword = createString("keyword");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final StringPath userId = createString("userId");

    public QKeyword(String variable) {
        this(Keyword.class, forVariable(variable), INITS);
    }

    public QKeyword(Path<? extends Keyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKeyword(PathMetadata metadata, PathInits inits) {
        this(Keyword.class, metadata, inits);
    }

    public QKeyword(Class<? extends Keyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new org.bson.types.QObjectId(forProperty("id")) : null;
    }

}

