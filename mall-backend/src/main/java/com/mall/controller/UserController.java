package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.constant.MessageConstant;
import com.mall.model.domain.User;
import com.mall.model.domain.UserDTO;
import com.mall.model.request.UserLoginRequest;
import com.mall.model.request.UserRegisterRequest;
import com.mall.service.UserService;
import com.mall.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.mall.constant.MessageConstant.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        return userService.userRegister(username, password, checkPassword);
    }

    /**
     * 查询用户是否已存在
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/findUsername")
    public BaseResponse<Boolean> checkUsername(String username) {
        if (StringUtils.isAnyBlank(username)) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        long l = userService.checkUsername(username);
        if (l < 0) {
            return ResultUtils.error(true, MessageConstant.REPEAT_USERNAME_FAIL);

        }
        return ResultUtils.success(false, MessageConstant.REPEAT_USERNAME_SUCCESS);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(null, MessageConstant.REQUEST_FAIL);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        return userService.userLogin(username, password, request);
    }

    /**
     * 查询个人信息
     *
     * @return
     */
    @GetMapping("/me")
    public BaseResponse<UserDTO> queryMe() {
        UserDTO user = UserHolder.getUser();
        System.out.println("UserDTO:"+user);
        if (user == null) {
            return ResultUtils.error(null, LOGIN_FAIL);
        }
        return ResultUtils.success(user, LOGIN_SUCCESS);
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @GetMapping("/all")
    public BaseResponse<List<UserDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 禁用账户
     * @param userId 用户id
     * @return
     */
    @PutMapping("{userId}")
    public BaseResponse<Boolean> disableUser(@PathVariable("userId") Integer userId) {
        if (userId == null) {
            return ResultUtils.error(null, REQUEST_FAIL);
        }
        return userService.disableUser(userId);
    }

    @DeleteMapping("{userId}")
    public BaseResponse<Boolean> deleteUser(@PathVariable("userId") Integer userId) {
        if (userId == null) {
            return ResultUtils.error(null, REQUEST_FAIL);
        }
        return userService.deleteUser(userId);
    }

}
