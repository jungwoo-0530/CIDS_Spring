package com.jungwoo.apiserver;

import com.jungwoo.apiserver.repository.maria.MemberRepository;
import com.jungwoo.apiserver.repository.maria.BoardRepository;
import com.jungwoo.apiserver.serviece.CommentService;
import com.jungwoo.apiserver.serviece.MemberService;
import com.jungwoo.apiserver.serviece.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * fileName     : InitDb
 * author       : jungwoo
 * description  :
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InitDb {

//  @PostConstruct
//  @Transactional
//  public void init(){
//            for(int i = 20; i<50; i++)
//    {
//      for(int j = 0; j<10; j++){
//        Member member = Member.builder().loginId("member"+i+j).password("asdf1234!!").email("member"+i+j+"@naver.com")
//            .role("member").name("김철수").telephone("010-1234-91"+i+j).build();
//        memberService.save(member);
//      }
//
//    }
//
//  }

//    Member admin = Member.builder().loginId("aa").password("aa").email("admin@naver.com")
//        .role("admin").name("김정우").telephone("010-8541-9497").build();
//    memberService.save(admin);
//
//    Member member = Member.builder().loginId("bb").password("bb").email("member@naver.com")
//        .role("member").name("김선우").telephone("010-8541-9497").build();

//        for(int i = 0; i<20; i++)
//    {
//      Member member = Member.builder().loginId("member" + i).password("asdf1234!!").email("member"+i+"@naver.com")
//          .role("member").name("홍길"+"A").telephone("010-1234-949"+i).build();
//      memberService.save(member);
//    }

//
//    for(int i = 0; i<20; i++)
//    {
//      Board board = Board.builder().
//          title("제묵"+i).content("테스트입니다"+ i).type("qna").hit(1).member(memberRepository.getById(0L)).available(true).build();
//      boardService.saveWithMember(board,admin);
//    }
//
//    Board commentBoard = Board.builder().
//        title("CommentTest").content("ComentTest").type("qna").hit(1).member(memberRepository.getById(0L)).available(true).build();
//    boardService.saveWithMember(commentBoard, admin);
//
//    for(int j = 0; j<11; j++){
//      Comment comment = Comment.builder().
//        content("댓글" + j).available(true).member(admin).board(commentBoard).build();
//
//      commentService.createComment(comment);
//    }

//    log.info("DB INIT PASS");
//  }

//  @PostConstruct
//  @Transactional
//  public void adminInit() {
//    Member admin = Member.builder().loginId("admin").email("admin@gmail.com").telephone("010-8541-9497")
//        .name("김정우").password("admin").role("admin").build();
//
//    memberService.save(admin);
//  }


//  @PostConstruct
//  @Transactional
//  public void init() {
//    Board board = boardRepository.findById(1L).orElseThrow(NullPointerException::new);
//


//    Comment comment1 = Comment.builder().content("1").ref(1L).step(0L).refOrder(0L).answerNum(0L).parentNum(null).board(board).build();
//
//    Comment comment2 = Comment.builder().content("2").ref(2L).step(0L).refOrder(0L).answerNum(3L).parentNum(null).board(board).build();
//    Comment comment3 = Comment.builder().content("3").ref(3L).step(0L).refOrder(0L).answerNum(0L).parentNum(null).board(board).build();
//    Comment comment4 = Comment.builder().content("2-1").ref(2L).step(1L).refOrder(1L).answerNum(1L).parentNum(2L).board(board).build();
//    Comment comment5 = Comment.builder().content("2-2").ref(2L).step(1L).refOrder(5L).answerNum(0L).parentNum(2L).board(board).build();
//    Comment comment6 = Comment.builder().content("2-1-1").ref(2L).step(2L).refOrder(2L).answerNum(1L).parentNum(4L).board(board).build();
//    Comment comment7 = Comment.builder().content("2-3").ref(2L).step(1L).refOrder(6L).answerNum(1L).parentNum(2L).board(board).build();
//    Comment comment8 = Comment.builder().content("2-3-1").ref(2L).step(2L).refOrder(7L).answerNum(1L).parentNum(7L).board(board).build();
//    Comment comment9 = Comment.builder().content("2-3-1-1").ref(2L).step(3L).refOrder(8L).answerNum(0L).parentNum(8L).board(board).build();
//    Comment comment10 = Comment.builder().content("2-1-1-1").ref(2L).step(3L).refOrder(3L).answerNum(1L).parentNum(6L).board(board).build();
//    Comment comment11 = Comment.builder().content("2-1-1-1-1").ref(2L).step(4L).refOrder(4L).answerNum(0L).parentNum(10L).board(board).build();
//   Comment comment1 = Comment.builder().content("1").board(board).build();
//    Comment comment2 = Comment.builder().content("2").board(board).build();
//    Comment comment3 = Comment.builder().content("3").board(board).build();
//
//    Comment comment4 = Comment.builder().content("2-1").board(board).build();


//    Comment comment5 = Comment.builder().content("2-2").ref(2L).step(1L).refOrder(5L).answerNum(0L).parentNum(2L).board(board).build();
//    Comment comment6 = Comment.builder().content("2-1-1").ref(2L).step(2L).refOrder(2L).answerNum(1L).parentNum(4L).board(board).build();
//    Comment comment7 = Comment.builder().content("2-3").ref(2L).step(1L).refOrder(6L).answerNum(1L).parentNum(2L).board(board).build();
//    Comment comment8 = Comment.builder().content("2-3-1").ref(2L).step(2L).refOrder(7L).answerNum(1L).parentNum(7L).board(board).build();
//    Comment comment9 = Comment.builder().content("2-3-1-1").ref(2L).step(3L).refOrder(8L).answerNum(0L).parentNum(8L).board(board).build();
//    Comment comment10 = Comment.builder().content("2-1-1-1").ref(2L).step(3L).refOrder(3L).answerNum(1L).parentNum(6L).board(board).build();
//    Comment comment11 = Comment.builder().content("2-1-1-1-1").ref(2L).step(4L).refOrder(4L).answerNum(0L).parentNum(10L).board(board).build();

//    commentService.initSave(comment1);
//    commentService.initSave(comment2);
//    commentService.initSave(comment3);
//    commentService.initSave(comment4);
//    commentService.initSave(comment5);
//    commentService.initSave(comment6);
//    commentService.initSave(comment7);
//    commentService.initSave(comment8);
//    commentService.initSave(comment9);
//    commentService.initSave(comment10);
//    commentService.initSave(comment11);

//    commentService.saveHierarchyOrders(comment1);
//    commentService.saveHierarchyOrders(comment2);
//    commentService.saveHierarchyOrders(comment3);
//    commentService.saveHierarchyOrders(comment4);
//  }

}
