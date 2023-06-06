package com.example.nerfbiz.src.convert;

import com.example.nerfbiz.config.BaseException;
import com.example.nerfbiz.config.Constant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static com.example.nerfbiz.config.BaseResponseStatus.*;
import static com.example.nerfbiz.config.Constant.*;

@Service
public class ConvertService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ConvertDao convertDao;
    private final ConvertProvider convertProvider;

    public ConvertService(ConvertDao convertDao, ConvertProvider convertProvider){
        this.convertDao = convertDao;
        this.convertProvider = convertProvider;
    }

    public String uploadVideo(int userIdx, String objectID, MultipartFile multipartFile) throws BaseException {

        try {
            //credential 객체 생성
            System.out.println("credential 객체 생성");
            String credentialsPath = FILE_PATH_RESOURCES + GCS_KEY_JSON;
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));

            //storage 객체 생성
            System.out.println("storage 객체 생성");
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            byte[] content = multipartFile.getBytes();

            //cloud에 영상 전송
            System.out.println("cloud에 영상 전송");
            BlobId blobId = BlobId.of(BUCKET_NAME, BUCKET_VIDEO_DIR+"/"+objectID);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("video/mp4").build();
            storage.create(blobInfo, content);

            String videoUrl = "https://storage.googleapis.com"+"/"+BUCKET_NAME+"/"+BUCKET_VIDEO_DIR+"/"+objectID;
            convertDao.createObject(objectID, userIdx, videoUrl);
            return videoUrl;

        }catch (Exception exception){
            throw new BaseException(VIDEO_UPLOAD_ERROR);
        }

    }

    public String convert(String category, String objectID, String videoUrl) throws BaseException {

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = Constant.FUNCTIONAL_SERVER_PATH_VIDEO2TRD + "?video="+videoUrl + "&identifier=" + objectID + "&mask_id="+category;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        System.out.println("요청 보냄");
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
        if(response.getStatusCode().isError()) throw new BaseException(VIDEO_CONVERT_ERROR);
        String obj_url = response.getBody();
        System.out.println("obj_url:"+obj_url);
        convertDao.saveObjUrl(objectID, obj_url);
        return obj_url;
    }

}
