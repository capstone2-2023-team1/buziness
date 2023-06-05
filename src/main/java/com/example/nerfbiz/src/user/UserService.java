package com.example.nerfbiz.src.user;

import com.example.nerfbiz.config.BaseException;
import com.example.nerfbiz.config.BaseResponseStatus;
import com.example.nerfbiz.src.user.model.PostUserReq;
import com.example.nerfbiz.src.user.model.PostUserRes;
import com.example.nerfbiz.utils.JwtService;
import com.example.nerfbiz.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.nerfbiz.config.BaseResponseStatus.*;

@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService){
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복 체크
        if(userProvider.checkEmail(postUserReq.getEmail())==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = SHA256.encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        }catch (Exception ignored){
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
