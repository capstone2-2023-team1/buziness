package com.example.nerfbiz.src.user;

import com.example.nerfbiz.config.BaseException;
import com.example.nerfbiz.config.BaseResponse;
import com.example.nerfbiz.src.user.model.PostUserReq;
import com.example.nerfbiz.src.user.model.PostUserRes;
import com.example.nerfbiz.utils.JwtService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.nerfbiz.config.BaseResponseStatus.POST_USERS_EMPTY_EMAIL;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     *
     * @param postUserReq
     * @return
     */

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq){

        if(postUserReq.getEmail()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

}
