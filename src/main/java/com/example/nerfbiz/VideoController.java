package com.example.nerfbiz;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class VideoController {

    @RequestMapping(value="video", method= RequestMethod.POST)
    public Map<String,Object> videoUpload (HttpServletRequest request,
                                             @RequestParam(value="file", required=false) MultipartFile[] files) throws SQLException {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String FileNames ="";
        System.out.println("paramMap =>"+files[0]);

        String filepath = Constant.FILE_PATH;
        for (MultipartFile mf : files) {

            String originFileName = mf.getOriginalFilename(); // 원본 파일 명
            long fileSize = mf.getSize(); // 파일 사이즈
            System.out.println("originFileName : " + originFileName);
            System.out.println("fileSize : " + fileSize);

            String safeFile =System.currentTimeMillis() + originFileName;

            FileNames = FileNames+","+safeFile;
            try {
                File f1 = new File(filepath+safeFile);
                mf.transferTo(f1);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        FileNames = FileNames.substring(1);
        System.out.println("FileNames =>"+ FileNames);
        resultMap.put("JavaData", paramMap);
        return resultMap;
    }

}
