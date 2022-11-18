package com.jungwoo.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * fileName     : CommonResponse
 * author       : jungwoo
 * description  :
 */
@Getter
@Setter
public class CommonResponse<T> extends BasicResponse{

  private int count;
  private T data;
  private String message;

  public CommonResponse(T data) {

    this.data = data;
    if(data instanceof List){
      this.count = ((List<?>)data).size();
    }else{
      this.count = 1;
    }

  }

  public CommonResponse(T data, String message) {
    this.message = message;
    this.data = data;
    if (data instanceof List) {
      this.count = ((List<?>) data).size();
    } else {
      this.count = 1;
    }
  }
  public CommonResponse(String message){
    this.message = message;
  }



}
