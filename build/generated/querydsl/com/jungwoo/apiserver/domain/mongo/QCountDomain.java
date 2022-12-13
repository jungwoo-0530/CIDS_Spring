package com.jungwoo.apiserver.domain.mongo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCountDomain is a Querydsl query type for CountDomain
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCountDomain extends EntityPathBase<CountDomain> {

    private static final long serialVersionUID = 411876712L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCountDomain countDomain = new QCountDomain("countDomain");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath domain = createString("domain");

    public final NumberPath<Long> hit = createNumber("hit", Long.class);

    public final org.bson.types.QObjectId id;

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public QCountDomain(String variable) {
        this(CountDomain.class, forVariable(variable), INITS);
    }

    public QCountDomain(Path<? extends CountDomain> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCountDomain(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCountDomain(PathMetadata metadata, PathInits inits) {
        this(CountDomain.class, metadata, inits);
    }

    public QCountDomain(Class<? extends CountDomain> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new org.bson.types.QObjectId(forProperty("id")) : null;
    }

}

