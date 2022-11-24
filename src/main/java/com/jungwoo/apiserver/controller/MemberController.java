package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.maria.Image;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.dto.maria.member.MemberCreateDto;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.maria.member.MemberDto;
import com.jungwoo.apiserver.dto.maria.member.MemberPageDto;
import com.jungwoo.apiserver.dto.maria.member.MemberUpdateDto;
import com.jungwoo.apiserver.exception.validation.ValidationGroup;
import com.jungwoo.apiserver.exception.validation.ValidationSequence;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.ImageService;
import com.jungwoo.apiserver.serviece.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * fileName     : MemberController
 * author       : jungwoo
 * description  : Member Entity와 관련된 컨트롤러
 */
@Api(tags = "회원 API Controller")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;

  private final ImageService imageService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @Value("${spring.image.member.absolute}")
  private String imageMemberAbsoluteUri;
  @Value("${spring.image.member.relative}")
  private String imageMemberRelativeUri;

  @ApiOperation(value = "회원가입")
  @PostMapping("/register")
  public ResponseEntity<? extends BasicResponse> registerMember(@Validated(value = ValidationSequence.class) @RequestBody MemberCreateDto createMemberRequest) {

      Long id = memberService.save(createMemberRequest);

      return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(id, "회원가입이 완료되었습니다."));
  }


  @ApiOperation(value = "로그인")
  @PostMapping("/login")
  public ResponseEntity<? extends BasicResponse> login(@Validated @RequestBody loginRequest loginReq) {

    Member member = memberService.findByLoginId(loginReq.getLoginId());

    memberService.loginPasswordMatches(loginReq.getPassword(), member.getPassword());

    String token = jwtAuthenticationProvider.createToken(member.getLoginId());
    Date date = jwtAuthenticationProvider.getTokenExpired(token);

    return ResponseEntity.ok().body(new CommonResponse<>(TokenResponse.builder().
        accessToken(token).
        tokenExpired(date).
        tokenType("Bearer").build()));
  }

  @Getter
  @Builder
  public static class TokenResponse {
    private String accessToken;
    private Date tokenExpired;
    private String tokenType;
  }

  @Getter
  @Builder
  public static class loginRequest {

    @NotBlank(message = "로그인ID는 필수입니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
  }

  @ApiOperation(value = "로그아웃")
  @PostMapping("/logout")
  public ResponseEntity<? extends BasicResponse> logout() {

    return ResponseEntity.ok().body(new CommonResponse<>("로그아웃에 성공했습니다."));
  }



  // 즉, jwt토큰으로 현재 로그인한 사용자의 정보(loginId, role)을 가져올 수 있음.
  @ApiOperation(value = "회원 인증", notes = "jwt토큰으로 로그인ID와 권한을 조회합니다.")
  @GetMapping("/auth")
  public ResponseEntity<? extends BasicResponse> getRoleAndLoingId(HttpServletRequest req) {
    String token = jwtAuthenticationProvider.getTokenInRequestHeader(req, "Bearer");
    String role = jwtAuthenticationProvider.getRole(token);
    String loginId = jwtAuthenticationProvider.getUserPk(token);
    return ResponseEntity.ok().body(new CommonResponse<>(AuthResponse.builder().
        role(role).
        loginId(loginId).build()));
  }

  @Getter
  @Builder
  public static class AuthResponse {
    private String loginId;
    private String role;
  }

  @ApiOperation(value = "회원 마이페이지", notes = "jwt토큰으로 해당 회원을 조회합니다.")
  @GetMapping("/members/me")
  public ResponseEntity<? extends BasicResponse> memberDetail(HttpServletRequest request) {
    log.info("memberDetail in MemberController");

    Member member = memberService.getMemberByRequestJwt(request);


    return ResponseEntity.ok().body(new CommonResponse<>(MemberDto.builder().loginId(member.getLoginId()).
        email(member.getEmail()).
        name(member.getName()).
        telephone(member.getTelephone()).
        role(member.getRole())
        .imgUri(member.getImgUri()).build(), "정상적으로 회원을 찾았습니다."));
  }


  @ApiOperation(value = "회원 마이페이지 수정(업데이트)")
  @PutMapping("/members/me/update")
  public ResponseEntity<? extends BasicResponse> updateMe(@Validated(value = ValidationSequence.class) @RequestBody MemberUpdateDto memberUpdateDto, HttpServletRequest request) {

    //OSIV 영속성 컨텍스트. 더티체크.
    Member member = memberService.getMemberByRequestJwt(request);

    memberService.updateMember(member, memberUpdateDto);

    return ResponseEntity.ok().body(new CommonResponse<>("회원 업데이트를 성공했습니다."));
  }

  @ApiOperation(value = "회원 비밀번호 수정")
  @PutMapping("/members/me/password")
  public ResponseEntity<? extends BasicResponse> updatePassword(@Validated(value = ValidationSequence.class) updatePasswordDto passwordDto, HttpServletRequest request){

    Member member = memberService.getMemberByRequestJwt(request);

    memberService.updatePassword(member, passwordDto.getNewPassword());

    return ResponseEntity.ok().body(new CommonResponse<>("비밀번호 업데이트 성공했습니다."));
  }


    @Getter
    private static class updatePasswordDto{
      @NotBlank(message = "패스워드는 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
      @Size(min = 8, max = 16, message = "패스워드를 8~16자리 입력해주세요.", groups = ValidationGroup.SizeGroup.class)
      private String newPassword;
    }

  @ApiOperation(value = "회원들 리스트 조회", notes = "회원들 리스트를 페이징하여 조회합니다.")
  @GetMapping("/members")
  public Page<MemberPageDto> listMember(@PageableDefault(size = 30, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request) {

      memberService.isAdmin(request);

      return memberService.findPageSort(pageable);

  }

  @ApiOperation(value = "회원 검색", notes = "admin이 searchWord로 회원 로그인ID로 조회합니다.")
  @GetMapping("/members/search")
  public Page<MemberPageDto> searchMember(@PageableDefault(size = 30, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestBody String searchWord) {


    return memberService.findPageSortBySearch(pageable, searchWord);

  }



  @ApiOperation(value = "회원 권한 수정", notes = "admin이 memberId를 이용하여 회원을 조회 후 권한을 변경합니다.")
  @PutMapping("/members/{memberId}")
  public ResponseEntity<? extends BasicResponse> updateMemberByAdmin(@PathVariable(name = "memberId") Long memberId, @RequestBody MemberDto memberDto, HttpServletRequest request) {

    memberService.updateRoleMember(memberId, memberDto.getRole(), request);

    return ResponseEntity.ok().body(new CommonResponse<>("유저가 수정되었습니다."));
  }


  @ApiOperation(value = "회원 이미지 생성", notes = "회원의 이미지를 생성합니다.")
  @PostMapping("/members/image")
  public ResponseEntity<? extends BasicResponse> uploadMemberImage(MultipartHttpServletRequest multipartReq) throws IOException {

    Member member = memberService.getMemberByMultiPartRequestJwt(multipartReq);

    MultipartFile mFile = multipartReq.getFile("upload");

    String fileFormatName = mFile.getContentType().substring(mFile.getContentType().lastIndexOf("/")+1);


    String origName = mFile.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String savedName = uuid + "-" +origName;
    String imageRelativePath = imageMemberRelativeUri + savedName;
    String imageAbsolutePath = imageMemberAbsoluteUri + savedName;

    mFile = imageService.resizeImage(savedName,fileFormatName,mFile, 280);

    Image image = Image.builder().
        fileName(origName).
        isUse(true).
        fileUUID(uuid).
        fileRelativePath(imageRelativePath).
        fileAbsolutePath(imageAbsolutePath).
        loginId(member.getLoginId()).
        build();

    //OSIV를 이용한 더티체킹.member.
    imageService.memberImageSave(image, member);

    mFile.transferTo(new File(imageAbsolutePath));
    ImageController.ImageDto imageDto =  ImageController.ImageDto.builder().uploaded(true).imageUrl(imageRelativePath).build();

    return ResponseEntity.ok().body(new CommonResponse<>(imageDto, "유저 이미지 업로드 완료"));

  }

}
