package com.jungwoo.apiserver.util.csv;

import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * fileName     : CsvUtil
 * author       : jungwoo
 * description  :
 */
@Component
public class CsvUtil {

  public String writeCSV(String title, List<String> contents) throws IOException {

    String fileName = title+"_"+getTime()+".csv";

    File csv = new File("./searchModule/data_dir/send_csv_list/"+fileName);

    BufferedWriter bw = null;

    bw = new BufferedWriter(new FileWriter(csv));

    bw.write("url");
    bw.newLine();
    for (String str : contents) {
      bw.write(str);
      bw.newLine();
    }

    bw.close();

    return fileName;
  }

  public String getTime() {
    long time = System.currentTimeMillis();
    SimpleDateFormat dayTime = new SimpleDateFormat("HHmmssSSS");
    return dayTime.format(new Date(time));
  }

}
