package com.hust.rbacbackend.controller;

import com.hust.rbacbackend.component.ResultInfo;
import com.hust.rbacbackend.entity.Role;
import com.hust.rbacbackend.entity.User;
import com.hust.rbacbackend.service.api.RoleService;
import com.hust.rbacbackend.service.api.UserService;
import com.hust.rbacbackend.vo.RoleIdListVO;
import com.hust.rbacbackend.vo.UserUpdateVO;
import com.sun.javafx.image.IntPixelGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PostMapping("/{uid}/roles")
    public ResultInfo addRoles(@PathVariable("uid") Integer id,@RequestBody RoleIdListVO roleIdListVO){
        if(id==null){
            throw new IllegalArgumentException("用户名不能为空");
        }
        userService.addRoles(id,roleIdListVO.getRoleIdList());
        return ResultInfo.success(200,"post请求成功",null);
    }

    @GetMapping("/{uid}")
    public ResultInfo queryUser(@PathVariable("uid") Integer uid){
        if(uid==null){
            throw new IllegalArgumentException("用户名不能为空");
        }
        User user = userService.queryUser(uid);
        if(user==null){
            throw new IllegalArgumentException("用户不存在");
        }
        return ResultInfo.success(200,"查询成功",user);
    }

    @GetMapping("")
    public ResultInfo queryAllUsers(){
        List<User> list=userService.queryAllUsers();
        return ResultInfo.success(200,"操作成功",list);
    }

    @DeleteMapping("/{uid}/roles")
    public ResultInfo removeRoles(@PathVariable("uid") Integer uid,@RequestBody RoleIdListVO roleIdList){
        userService.delRoles(uid,roleIdList.getRoleIdList());
        return ResultInfo.success(200,"操作成功",null);
    }

    @PutMapping("/{uid}")
    public ResultInfo updateUser(@PathVariable("uid") Integer id,@RequestBody UserUpdateVO userUpdateVO){
        if(userUpdateVO==null||userUpdateVO.getUser()==null){
            throw new IllegalArgumentException("请填写用户信息");
        }
        User user = userUpdateVO.getUser();
        RoleIdListVO roleIdList = userUpdateVO.getRoleIdList();
        user.setId(id);
        userService.updateUser(user);
        userService.delRoles(user);
        if(roleIdList!=null&&roleIdList.getRoleIdList().size()>0){
            userService.addRoles(user.getId(),roleIdList.getRoleIdList());
        }
        return ResultInfo.success("更新成功");
    }
}
