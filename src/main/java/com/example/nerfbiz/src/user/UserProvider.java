package com.example.nerfbiz.src.user;

import com.example.nerfbiz.config.BaseException;
import com.example.nerfbiz.config.BaseResponseStatus;
import com.example.nerfbiz.src.user.model.PostLoginReq;
import com.example.nerfbiz.src.user.model.PostLoginRes;
import com.example.nerfbiz.src.user.model.User;
import com.example.nerfbiz.utils.JwtService;
import com.example.nerfbiz.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.nerfbiz.config.BaseResponseStatus.*;

@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserProvider(UserDao userDao, JwtService jwtService){
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public int checkEmail(String email) throws BaseException {
        try{
            return userDao.checkEmail(email);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

}
