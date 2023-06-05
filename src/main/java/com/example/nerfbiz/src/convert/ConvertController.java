package com.example.nerfbiz.src.convert;

import com.example.nerfbiz.config.BaseException;
import com.example.nerfbiz.config.BaseResponse;
import com.example.nerfbiz.config.Constant;
import com.example.nerfbiz.src.convert.model.PostConvertReq;
import com.example.nerfbiz.src.convert.model.PostConvertRes;
import com.example.nerfbiz.src.convert.model.Target;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.BlobId;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = Constant.FRONT_SERVER_PATH)
@RestController
public class ConvertController {

    @Autowired
    private final ConvertProvider convertProvider;
    @Autowired
    private final ConvertService convertService;

    public ConvertController(ConvertProvider convertProvider, ConvertService convertService){
        this.convertProvider = convertProvider;
        this.convertService = convertService;
    }

    /**
     * 변환 요청 API
     * [POST] /video
     * @param postConvertReq
     * @param files
     * @return
     */

    @RequestMapping(value="video", method= RequestMethod.POST)
    public BaseResponse<PostConvertRes> videoUpload (@RequestBody PostConvertReq postConvertReq, @RequestParam(value="file", required=false) MultipartFile[] files) {

            String objectId = System.currentTimeMillis()+"";
            try {
                System.out.println("gcs 업로드");
                convertService.uploadVideo(objectId, files[0]);
            }catch (BaseException exception){
                return new BaseResponse(exception.getStatus());
            }
            try {
                System.out.println("NeRF-server에 작업 요청");
                return new BaseResponse<>(convertService.convert(postConvertReq, objectId));
            }
            catch (BaseException exception) {
                return new BaseResponse<>(exception.getStatus());
            }
    }

}
