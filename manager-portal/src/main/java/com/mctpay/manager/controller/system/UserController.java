package com.mctpay.manager.controller.system;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mctpay.common.base.model.PageParam;
import com.mctpay.common.base.model.ResponseData;
import com.mctpay.common.base.model.ResponsePageInfo;
import com.mctpay.common.uitl.UIdUtils;
import com.mctpay.manager.model.dto.system.UserDTO;
import com.mctpay.manager.model.param.UserParam;
import com.mctpay.manager.service.system.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: guodongwei
 * @Description: 用户相关接口
 * @Date: 2020/2/24 20:08
 */
@Api(value = "系统管理", tags = "系统管理")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    // @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "管理员注册", notes = "管理员注册", httpMethod = "POST", consumes = "application/json")
    @PostMapping("/insertUser")
    public ResponseData insertUser(@RequestBody UserParam userParam) {
        // 设置会员ID
        String id = UIdUtils.getUid().toString();
        userParam.setId(id);
        userParam.setStatus(2);
        userParam.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userParam.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return userService.insertUser(userParam);
    }

    @ApiOperation(value = "冻结/激活管理员", notes = "冻结/激活管理员；status传值为正数则是激活的状态，负数为冻结状态，传该管理原status的相反数", httpMethod = "POST", consumes = "application/json")
    @PostMapping("/switchUser")
    public ResponseData switchUser(@RequestParam String userId, @RequestParam Integer state) {
        return userService.switchUser(userId, state);
    }

    @ApiOperation(value = "分页查询管理员", notes = "分页查询管理员;status值为1||2，表示激活管理员，-1||-2为冻结管理员", httpMethod = "POST", consumes = "application/json")
    @PostMapping("/listUser")
    public ResponsePageInfo<List<UserDTO>> listUser(@RequestBody PageParam pageParam) {
        Page<Object> pageInfo = PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        if (!StringUtils.isEmpty(pageParam.getOrder())) {
            PageHelper.orderBy(pageParam.getOrder());
        }
        List<UserDTO> userDTOs = userService.listUser();
        return new ResponsePageInfo<List<UserDTO>>().success(userDTOs, pageInfo);
    }

    @ApiOperation(value = "根据输入内容查询管理员", notes = "冻结/激活管理员；status传值为正数则是激活的状态，负数为冻结状态，传该管理原status的相反数", httpMethod = "POST", consumes = "application/json")
    @PostMapping("/listUserByInput")
    public ResponseData<List<UserDTO>> listUserByInput(String inputContent, @RequestBody PageParam pageParam) {
        Page<Object> pageInfo = PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        if (!StringUtils.isEmpty(pageParam.getOrder())) {
            PageHelper.orderBy(pageParam.getOrder());
        }
        List<UserDTO> userDTOs = userService.listUserByInput(inputContent);
        return new ResponsePageInfo<List<UserDTO>>().success(userDTOs, pageInfo);
    }
}
