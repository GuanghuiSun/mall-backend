package com.mall.controller;

import com.mall.base.BaseResponse;
import com.mall.base.ResultUtils;
import com.mall.exception.BusinessException;
import com.mall.model.domain.UserDTO;
import com.mall.model.request.UserLoginRequest;
import com.mall.model.request.UserRegisterRequest;
import com.mall.service.UserService;
import com.mall.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.mall.base.ErrorCode.*;
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
    public BaseResponse<Integer> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        return ResultUtils.success(userService.userRegister(username, password, checkPassword), REGISTER_SUCCESS);
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
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        return ResultUtils.success(false, REPEAT_USERNAME_SUCCESS);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        String token = userService.userLogin(username, password);
        return ResultUtils.success(token, LOGIN_SUCCESS);
    }

    /**
     * 查询个人信息
     *
     * @return
     */
    @GetMapping("/me")
    public BaseResponse<UserDTO> queryMe() {
        UserDTO user = UserHolder.getUser();
        System.out.println("UserDTO:" + user);
        return ResultUtils.success(user, LOGIN_SUCCESS);
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @GetMapping("/all")
    public BaseResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResultUtils.success(users, SELECT_SUCCESS);
    }

    /**
     * 禁用账户
     *
     * @param userId 用户id
     * @return
     */
    @PutMapping("{userId}")
    public BaseResponse<Boolean> disableUser(@PathVariable("userId") Integer userId) {
        if (userId == null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Boolean success = userService.disableUser(userId);
        if (Boolean.FALSE.equals(success)) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, CHANGE_VALID_FAIL);
        }
        return ResultUtils.success(true, CHANGE_VALID_SUCCESS);
    }

    @DeleteMapping("{userId}")
    public BaseResponse<Boolean> deleteUser(@PathVariable("userId") Integer userId) {
        if (userId == null) {
            throw new BusinessException(PARAMS_NULL_ERROR);
        }
        Boolean success = userService.deleteUser(userId);
        if (Boolean.FALSE.equals(success)) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, DELETE_FAIL);
        }
        return ResultUtils.success(true, DELETE_SUCCESS);
    }

}
