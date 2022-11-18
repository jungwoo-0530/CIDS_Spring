package com.jungwoo.apiserver.domain.maria;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImageUtil is a Querydsl query type for ImageUtil
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImageUtil extends EntityPathBase<ImageUtil> {

    private static final long serialVersionUID = 1935448948L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QImageUtil imageUtil = new QImageUtil("imageUtil");

    public final com.jungwoo.apiserver.domain.QBaseTimeEntity _super = new com.jungwoo.apiserver.domain.QBaseTimeEntity(this);

    public final QBoard board;

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createDate = _super.createDate;

    public final StringPath fileAbsolutePath = createString("fileAbsolutePath");

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final StringPath fileUUID = createString("fileUUID");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> updateDate = _super.updateDate;

    public final BooleanPath useFlag = createBoolean("useFlag");

    public final StringPath userPk = createString("userPk");

    public QImageUtil(String variable) {
        this(ImageUtil.class, forVariable(variable), INITS);
    }

    public QImageUtil(Path<? extends ImageUtil> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImageUtil(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImageUtil(PathMetadata metadata, PathInits inits) {
        this(ImageUtil.class, metadata, inits);
    }

    public QImageUtil(Class<? extends ImageUtil> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

