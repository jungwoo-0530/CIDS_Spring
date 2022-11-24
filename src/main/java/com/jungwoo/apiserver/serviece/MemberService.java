package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.maria.member.MemberPageDto;
import com.jungwoo.apiserver.exception.CustomException;
import com.jungwoo.apiserver.exception.MemberErrorCode;
import com.jungwoo.apiserver.repository.maria.MemberRepository;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.Optional;
/**
 * fileName     : MemberService
 * author       : jungwoo
 * description  : Member Entity와 관련된 서비스 계층
 */
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  private final PasswordEncoder passwordEncoder;

  public boolean dupLoginIdCheck(String loginId) {
    return memberRepository.existsByLoginId(loginId);
  }

  public boolean dupEmailCheck(String email) {
    return memberRepository.existsByEmail(email);
  }

  @Transactional
  public Long save(Member member) {

    //Id중복검사
    if(dupLoginIdCheck(member.getLoginId())){
      throw new CustomException(MemberErrorCode.MEMBER_LOGINID_DUPLICATION);
    }

    //이메일 중복검사
    if(dupEmailCheck(member.getEmail())){
      throw new CustomException(MemberErrorCode.MEMBER_EMAIL_DUPLICATION);
    }

    String encodedPassword = passwordEncoder.encode(member.getPassword());
    member.setPassword(encodedPassword);
    memberRepository.save(member);
    return member.getId();

  }

  @Transactional(readOnly = true)
  public Member findByLoginId(String loginId) {
    return memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public Member getMemberByRequestJwt(HttpServletRequest request) {
    return memberRepository.findByLoginId(jwtAuthenticationProvider.getUserPk(jwtAuthenticationProvider.getTokenInRequestHeader(request, "Bearer"))).orElseThrow(()->new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
  }

  @Transactional
  public void updateMember(Member member) {
    Member findMember = memberRepository.findByLoginId(member.getLoginId()).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    findMember.change(member.getName(), member.getTelephone());
  }

  @Transactional
  public void updateRoleMember(Long memberId, String role, HttpServletRequest request) {
    isAdmin(request);
    Member member = memberRepository.getById(memberId);
    member.changeRole(role);
  }

  public void isAdmin(HttpServletRequest request) {
    Member member = getMemberByRequestJwt(request);
    if(!member.getRole().equals("ADMIN"))
      throw new CustomException(MemberErrorCode.MEMBER_NO_ACCESS);
  }

  public Page<MemberPageDto> findPageSort(Pageable pageable) {

    return memberRepository.findAllPageSort(pageable);
  }

  public Page<MemberPageDto> findPageSortBySearch(Pageable pageable, String searchWord) {
    return memberRepository.findAllPageSortBySearch(pageable, searchWord);
  }

  public void loginIdMatches(String requestPassword, String password) {
    if(!passwordEncoder.matches(requestPassword, password)){
      throw new CustomException(MemberErrorCode.MEMBER_PASSWORD_NOT_MATCH);
    }
  }

  @Transactional(readOnly = true)
  public Member getMemberByMultiPartRequestJwt(MultipartHttpServletRequest request) {
    return memberRepository.findByLoginId(jwtAuthenticationProvider.getUserPk(jwtAuthenticationProvider.getTokenInRequestHeader(request, "Bearer"))).orElseThrow(()->new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
  }
}
