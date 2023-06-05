package com.example.nerfbiz.src.user;

import com.example.nerfbiz.config.BaseException;
import com.example.nerfbiz.config.BaseResponseStatus;
import com.example.nerfbiz.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.nerfbiz.config.BaseResponseStatus.DATABASE_ERROR;

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
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
