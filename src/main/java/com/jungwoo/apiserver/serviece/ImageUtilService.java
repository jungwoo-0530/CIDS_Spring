package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.maria.Board;
import com.jungwoo.apiserver.domain.maria.ImageUtil;
import com.jungwoo.apiserver.repository.maria.ImageUtilRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.core.io.Resource;
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
import java.util.List;

/**
 * fileName     : FileLogService
 * author       : jungwoo
 * description  :q
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUtilService {

  private final ImageUtilRepository imageUtilRepository;


  @Transactional
  public void temporarySave(ImageUtil temp) {
    imageUtilRepository.save(temp);
  }

  @Transactional
  public void permanentSaveImage(String loginId, Board board) throws IOException {

    //영구저장할 새로운 절대 경로 디렉토리를 생성.
    String newAbsoluteDirPath = makeDirWithBoardId(board);

    //ImageTrace dirty checking
    List<ImageUtil> imageUtilList = imageUtilRepository.findAllByLoginId(loginId);
    for (ImageUtil i : imageUtilList) {

      i.changeBoard(board);
      i.changeUseFlag(true);
      i.changeFilePath("/img/boardImage/" + board.getId() + "/" + i.getFileUUID());


      String newFilePath = newAbsoluteDirPath + "/" + i.getFileUUID();
      moveImageFile(i.getFileAbsolutePath(), newFilePath);
      i.changeFileAbsolutePath(newFilePath);
    }



  }

  //현재 파일 전체 Path, 이동할 파일 전체 Path.
  public void moveImageFile(String currentFilePath, String newFilePath) throws IOException {

    log.info(currentFilePath);
    log.info(newFilePath);

    Path oldFile = Paths.get(currentFilePath);
    Path newFile = Paths.get(newFilePath);
    Path newPath = Files.move(oldFile, newFile);
  }

  public String makeDirWithBoardId(Board board) {
    String path = "/Users/jungwoo/Desktop/img/";
    path = path + board.getId();

    File folder = new File(path);
    if(!folder.exists()){
      boolean result = folder.mkdir();
      if(result){
        log.info("디렉토리 생성");
      }else{
        log.info("디렉토리 생성 실패");
      }
    }

    return path;
  }


  @Transactional
  public boolean deleteTempImageByLoginId(String loginId) {


    List<ImageUtil> allByLoginId = imageUtilRepository.findAllByLoginId(loginId);
    for (ImageUtil i : allByLoginId) {

      //파일삭제
      File file = new File(i.getFileAbsolutePath());
      boolean isDeleted = file.delete();

      if (isDeleted) {
        //db row 삭제
        imageUtilRepository.deleteById(i.getId());
        return true;
      }else{
        return false;
      }

    }

    return true;
  }


  public MultipartFile resizeImage(MultipartFile originalImage, int targetWidth) throws IOException {

    BufferedImage image = ImageIO.read(originalImage.getInputStream());


    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();
    log.info(String.valueOf(originalHeight));
    log.info(String.valueOf(originalWidth));

    if (originalWidth < targetWidth) {
      return originalImage;
    }

    MarvinImage marvinImage = new MarvinImage(image);

    Scale scale = new Scale();
    scale.load();
    scale.setAttribute("newWitdh", targetWidth);
    scale.setAttribute("newHeight", targetWidth * originalHeight / originalWidth);
    scale.process(marvinImage.clone(), marvinImage, null, null, false);

    BufferedImage imageNoAlpha = marvinImage.getBufferedImageNoAlpha();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(imageNoAlpha, originalImage.getContentType(), baos);
    baos.flush();

    return new MultipartImage(baos.toByteArray());


  }

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
