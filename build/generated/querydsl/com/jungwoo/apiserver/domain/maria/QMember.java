package com.jungwoo.apiserver.domain.maria;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 351625859L;

    public static final QMember member = new QMember("member1");

    public final com.jungwoo.apiserver.domain.QBaseTimeEntity _super = new com.jungwoo.apiserver.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgUri = createString("imgUri");

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath role = createString("role");

    public final StringPath telephone = createString("telephone");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> updateDate = _super.updateDate;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

