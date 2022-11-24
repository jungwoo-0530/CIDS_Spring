package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.maria.Board;
import com.jungwoo.apiserver.domain.maria.Image;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.exception.CustomException;
import com.jungwoo.apiserver.exception.ImageErrorCode;
import com.jungwoo.apiserver.repository.maria.ImageRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * fileName     : FileLogService
 * author       : jungwoo
 * description  :q
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

  private final ImageRepository imageRepository;

  @Value("${spring.image.board.temp.absolute}")
  private String imageTempAbsoluteUri;
  @Value("${spring.image.board.temp.relative}")
  private String imageTempRelativeUri;
  @Value("${spring.image.board.permanent.absolute}")
  private String imageBoardAbsoluteUri;
  @Value("${spring.image.board.permanent.relative}")
  private String imageBoardRelativeUri;

  @Transactional
  public void temporarySave(Image temp) {
    imageRepository.save(temp);
  }

  @Transactional
  public void permanentSaveImage(Board board) throws IOException {

    //영구 저장할 새로운 절대 경로 디렉토리를 생성.
    makeDirWithBoardId(board);

    log.info(board.getContent());

    List<String> imageUris = findImageUriInBoardContent(board);

    List<Image> imageList = new ArrayList<>();
    for(String uri :imageUris){
      log.info(uri);
      imageList.add(imageRepository.findOneByLoginIdAndRelativePath(board.getMember().getLoginId(), uri));
    }

    for (Image i : imageList) {
      log.info(i.getFileRelativePath());
      String changedAbsoluteUri = imageBoardAbsoluteUri + board.getId() + "/" + i.getFileUUID()+"-"+i.getFileName();
      String changedRelativeUri = imageBoardRelativeUri + board.getId() + "/" + i.getFileUUID()+"-"+i.getFileName();

      //이미지 파일 이동
      moveImageFile(i.getFileAbsolutePath(), changedAbsoluteUri);

      //더티체크,
      i.tempToPersistBoardImage(board, true, changedAbsoluteUri, changedRelativeUri);

      changeTempImageToImage(board, imageBoardRelativeUri +board.getId()+"/");

    }


  }

    //Board가 저장될 때, board의 content에 담겨져 있는 image uri를 temp에서 최종 디렉토리로 변경해준다.
    @Transactional
    public void changeTempImageToImage(Board board, String imageBoardRelativeUrl){

      String boardHtml = board.getContent();

      boardHtml = boardHtml.replace(imageTempRelativeUri, imageBoardRelativeUrl);

      board.changeHtml(boardHtml);

    }


  //Board가 저장될 때, 해당 Board ID에 맞는 디렉토리로 파일을 이동.
  public void moveImageFile(String currentFilePath, String newFilePath) throws IOException {

    Path oldFile = Paths.get(currentFilePath);
    Path newFile = Paths.get(newFilePath);
    Files.move(oldFile, newFile);
  }

  public void makeDirWithBoardId(Board board) {
    String path = imageBoardAbsoluteUri + board.getId();

    File folder = new File(path);
    if(!folder.exists()){
      if(folder.mkdir()){
        log.info("디렉토리 생성");
      }else{
        throw new CustomException(ImageErrorCode.IMAGE_DIRECTORY_CREATE_FAILED);
      }
    }
  }


  @Transactional
  public boolean deleteTempImageByLoginId(String loginId) {


    List<Image> allByLoginId = imageRepository.findAllByLoginId(loginId);
    for (Image i : allByLoginId) {

      //파일삭제
      File file = new File(i.getFileAbsolutePath());
      boolean isDeleted = file.delete();

      if (isDeleted) {
        //db row 삭제
        imageRepository.deleteById(i.getId());
        return true;
      }else{
        return false;
      }

    }

    return true;
  }

  public MultipartFile resizeImage(String fileName, String fileFormatName, MultipartFile originalImage, int targetWidth) throws IOException {
    BufferedImage image = ImageIO.read(originalImage.getInputStream());


    int originWidth = image.getWidth();
    int originHeight = image.getHeight();

    if (originWidth < targetWidth) {
      return originalImage;
    }

    MarvinImage imageMarvin = new MarvinImage(image);

    Scale scale = new Scale();
    scale.load();
    scale.setAttribute("newWidth", targetWidth);
    scale.setAttribute("newHeight", targetWidth * originHeight / originWidth);
    scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

    BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(imageNoAlpha, fileFormatName, baos);
    baos.flush();

    return new MockMultipartFile(fileName, baos.toByteArray());
  }

  //Board Content에서 <img src= "~"> 태그의 src값을 정규식을 사용해 리스트에 담아주는 함수.
  List<String> findImageUriInBoardContent(Board board){
    String html = board.getContent();


    Pattern pattern = Pattern.compile("<*<img src=[\\\"']?([^>\\\"']+)[\\\"']?[^>]*>");

    Matcher matcher = pattern.matcher(html);
    List<String> imageUri = new ArrayList<>();

    while(matcher.find()){
      imageUri.add(matcher.group(1));
      if(matcher.group(1) == null)break;
    }

    return imageUri;
  }

  @Transactional
  public void memberImageSave(Image image, Member member){
    imageRepository.save(image);
    member.changeImgUri(image.getFileRelativePath());
  }



  private

  @Getter
  @Setter
  static class MultipartImage implements MultipartFile{

    private byte[] bytes;
    String name;
    String originalFilename;
    String contentType;
    boolean isEmpty;
    long size;

    public MultipartImage(byte[] bytes) {
      this.bytes = bytes;
    }

    public byte[] getBytes() {
      return bytes;
    }

    public String getName() {
      return name;
    }

    public String getOriginalFilename() {
      return originalFilename;
    }

    public String getContentType() {
      return contentType;
    }

    public boolean isEmpty() {
      return isEmpty;
    }

    public long getSize() {
      return size;
    }

    @Override
    public Resource getResource() {
      return MultipartFile.super.getResource();
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
      MultipartFile.super.transferTo(dest);
    }

    @Override
    public InputStream getInputStream() throws IOException {
      return null;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }
  }
}
