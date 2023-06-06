package com.example.nerfbiz.src.convert;

import com.example.nerfbiz.config.BaseException;
import com.example.nerfbiz.config.BaseResponseStatus;
import com.example.nerfbiz.src.convert.model.GetRenderingRes;
import com.example.nerfbiz.src.convert.model.GetVideoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ConvertProvider {

    private final ConvertDao convertDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ConvertProvider(ConvertDao convertDao) {
        this.convertDao = convertDao;
    }

    public List<GetVideoRes> getVideos(int userIdx) throws BaseException {
        try{
            return convertDao.getVideos(userIdx);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
//
//    public List<GetRenderingRes> getRenderings(int userIdx) throws BaseException{
//        try{
//
//        }
//    }
}
