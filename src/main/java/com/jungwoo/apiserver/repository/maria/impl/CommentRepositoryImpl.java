package com.jungwoo.apiserver.repository.maria.impl;

import com.jungwoo.apiserver.domain.maria.Comment;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.maria.comment.CommentPageDto;
import com.jungwoo.apiserver.repository.maria.CommentRepositoryCustom;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.jungwoo.apiserver.domain.maria.QComment.comment;

/**
 * fileName     : CommentRepositoryImpl
 * author       : jungwoo
 * description  :
 */
//@RequiredArgsConstructor
@Slf4j
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public void adjustHierarchyOrders(Comment newComment) {
    jpaQueryFactory.update(comment)
        .set(comment.leftNode, comment.leftNode.add(2))
        .where(
            comment.leftNode.goe(newComment.getRightNode())//새로운 코멘트의 오른쪽노드값보다 왼쪽노드값이 큰 것들
            .and(comment.rootComment.id.eq(newComment.getRootComment().getId()))
        )
        .execute();

    jpaQueryFactory.update(comment)
        .set(comment.rightNode, comment.rightNode.add(2))
        .where(
            comment.rightNode.goe(newComment.getLeftNode())
            .and(comment.rootComment.id.eq(newComment.getRootComment().getId()))
        )
        .execute();
  }


  @Override
  public Page<CommentPageDto> findCommentsOrderByHierarchy(Pageable pageable, Long boardId, Member member) {


    JPAQuery<CommentPageDto> query =  jpaQueryFactory
        .select(
            Projections.fields(CommentPageDto.class,
                comment.id.as("id"),
                comment.available.as("available"),
                getEditableEq(member).as("editable"),
                comment.member.loginId.as("author"),
                Expressions.asString(comment.member.imgUri).as("memberImgUri"),
                comment.content.as("content"),
                comment.parentComment.id.as("parentId"),
                comment.depth.as("deep"),
                comment.createDate.as("createDate"),
                comment.updateDate.as("updateDate"))
        )
        .where
            (
                ExpressionUtils.and(
                        (
                            comment.board.id.eq(boardId)
                        ),
                    (
                        comment.isLastComment.eq(true).
                            and(comment.available.eq(false))
                        ).not()
                )


            )
        .from(comment)
        .orderBy(comment.rootComment.id.asc(),
            comment.leftNode.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());


    long total = jpaQueryFactory
        .select(comment)
        .from(comment)
        .where
            (
                ExpressionUtils.and(
                        (
                            comment.board.id.eq(boardId)
                        ),
                        (
                            comment.isLastComment.eq(true).
                                and(comment.available.eq(false))
                        ).not()

            )
            )
        .fetch().size();

    List<CommentPageDto> content = query.fetch();

    return new PageImpl<>(content, pageable, total);
  }

//  @Override
//  public Page<CommentPageDto> findCommentsOrderByHierarchy(Pageable pageable, Long boardId, Member member) {
//    QCommentPageDto qCommentPageDto = new QCommentPageDto(
//        comment.id.as("id"),
//        comment.available.as("available"),
//        getEditableEq(member).as("editable"),
//        comment.member.loginId.as("author"),
//        comment.content.as("content"),
//        comment.parentComment.id.as("parentId"),
//        comment.depth.as("deep"),
//        comment.createDate.as("createDate"),
//        comment.updateDate.as("updateDate"));
//
//
//    JPAQuery<CommentPageDto> query =  jpaQueryFactory
//        .select(qCommentPageDto)
//        .where
//            (
//                ExpressionUtils.and(
//                        (
//                            comment.board.id.eq(boardId)
//                        ),
//                    (
//                        comment.isLastComment.eq(true).
//                            and(comment.available.eq(false))
//                        ).not()
//                )
//
//
//            )
//        .from(comment)
//        .orderBy(comment.rootComment.id.asc(),
//            comment.leftNode.asc())
//        .offset(pageable.getOffset())
//        .limit(pageable.getPageSize());
//
//
//    long total = jpaQueryFactory
//        .select(comment)
//        .from(comment)
//        .where
//            (
//                ExpressionUtils.and(
//                        (
//                            comment.board.id.eq(boardId)
//                        ),
//                        (
//                            comment.isLastComment.eq(true).
//                                and(comment.available.eq(false))
//                        ).not()
//
//            )
//            )
//        .fetch().size();
//
//    List<CommentPageDto> content = query.fetch();
//
//    return new PageImpl<>(content, pageable, total);
//  }

  private static BooleanExpression getEditableEq(Member member) {

    BooleanExpression alwaysTrue = Expressions.asBoolean(true).isTrue();
    if (member.getRole().equals("ADMIN")) {
      return alwaysTrue;
    } else {
      return comment.member.eq(member);
    }
  }



}
