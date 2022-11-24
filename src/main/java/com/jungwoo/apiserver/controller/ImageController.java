package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.maria.Image;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.ImageService;
import com.jungwoo.apiserver.serviece.MemberService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * fileName     : FileLogController
 * author       : jungwoo
 * description  :
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

  private final ImageService imageService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  private final MemberService memberService;


  @Value("${spring.image.board.temp.absolute}")
  private String imageTempAbsoluteUri;
  @Value("${spring.image.board.temp.relative}")
  private String imageTempRelativeUri;


  @SneakyThrows
  @PostMapping("/image/upload")
  public ResponseEntity<? extends BasicResponse> imageUploadTest(MultipartHttpServletRequest multipartReq) throws IOException {

    MultipartFile mFile = multipartReq.getFile("upload");

    String fileFormatName = mFile.getContentType().substring(mFile.getContentType().lastIndexOf("/")+1);


    String origName = mFile.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String savedName = uuid + "-" +origName;
    String imageRelativePath = imageTempRelativeUri + savedName;
    String imageAbsolutePath = imageTempAbsoluteUri + savedName;

    mFile = imageService.resizeImage(savedName,fileFormatName,mFile, 600);

    Image temp = Image.builder().
    fileName(origName).
    isUse(false).
    fileUUID(uuid).
        fileRelativePath(imageRelativePath).
    fileAbsolutePath(imageAbsolutePath).
        loginId(memberService.getMemberByRequestJwt(multipartReq).getLoginId()).
    build();

    imageService.temporarySave(temp);
    String saveTempImagePath = imageTempAbsoluteUri + savedName;
    String tempImagePath = imageTempRelativeUri + savedName;

    mFile.transferTo(new File(imageAbsolutePath));
    ImageDto imageDto =  ImageDto.builder().uploaded(true).imageUrl(imageRelativePath).build();

    return ResponseEntity.ok().body(new CommonResponse<>(imageDto, "이미지 업로드 완료"));
  }

  @Getter
  @Builder
  public static class ImageDto {

    boolean uploaded;
    String imageUrl;
  }



  @PostMapping("/image/delete")
  public ResponseEntity<? extends BasicResponse> deleteTempImage(HttpServletRequest request) {

    String loginId = jwtAuthenticationProvider.getUserPk(jwtAuthenticationProvider.getTokenInRequestHeader(request,"Bearer"));

//    if(imageUtilService.deleteTempImageByLoginId(loginId)){
//      return ResponseEntity.status(201).body(new CommonResponse<>(loginId, "임시 이미지 삭제 완료"));
//    }
//    else{
//      return ResponseEntity.status(204).body(new ErrorResponse("이미지 삭제 실패했습니다."));
//    }
    imageService.deleteTempImageByLoginId(loginId);
      return ResponseEntity.status(201).body(new CommonResponse<>(loginId, "임시 이미지 삭제 완료"));

  }



}
