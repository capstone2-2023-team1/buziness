package com.example.nerfbiz;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.io.ByteStreams;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.BlobId;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VideoController {

    @RequestMapping(value="video", method= RequestMethod.POST)
    public Map<String,Object> videoUpload (HttpServletRequest request,
                                             @RequestParam(value="file", required=false) MultipartFile[] files) throws SQLException {
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String filepath = "/videos/";
        String FileNames ="";
        System.out.println("paramMap =>"+files[0]);

        System.out.println(filepath);
        for (MultipartFile mf : files) {

            String originFileName = mf.getOriginalFilename(); // 원본 파일 명
            long fileSize = mf.getSize(); // 파일 사이즈
            System.out.println("originFileName : " + originFileName);
            System.out.println("fileSize : " + fileSize);
            String safeFile =System.currentTimeMillis()+"";
            FileNames = FileNames+","+safeFile;
            try {
                //String pathname = filepath+safeFile;
                //File f1 = new File(pathname);
                //mf.transferTo(f1);
                //System.out.println("f1.getAbsolutePath:"+f1.getAbsolutePath());

                //credential 객체 생성
                System.out.println("credential 객체 생성");
                String credentialsPath = Constant.FILE_PATH_RESOURCES+"protean-pager-386913-984d487862d2.json";
                GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));

                //storage 객체 생성
                System.out.println("storage 객체 생성");
                Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
                //Path path = Paths.get(f1.getAbsolutePath());
                //byte[] content = Files.readAllBytes(path);
                byte[] content = mf.getBytes();

                //cloud에 영상 전송
                System.out.println("cloud에 영상 전송");
                BlobId blobId = BlobId.of("nerf-video", "videos/"+safeFile);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("video/mp4").build();
                storage.create(blobInfo, content);

                //NeRF-server에 작업 요청
                System.out.println("NeRF-server에 작업 요청");
                RestTemplate restTemplate = new RestTemplate();
                String apiUrl = Constant.FUNCTIONAL_SERVER_PATH_VIDEO2TRD+"?video=https://storage.googleapis.com/nerf-video/videos/"+safeFile+"&identifier="+safeFile+"&mask_id=book";
                HttpHeaders headers = new HttpHeaders();
                String requestBody = "{\"video\": \"https://storage.googleapis.com/nerf-video/"+safeFile+"\", \"identifier\": \""+safeFile+"\"}"; //영상주소 전달
                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
                ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
                String responseBody = response.getBody();
                System.out.println(responseBody);
                resultMap.put("obj_url", Constant.RENDERING_SERVER_PATH+"?url="+responseBody);
            } catch (IOException e) {
                e.printStackTrace();

            }

            catch (IllegalStateException e) {
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
