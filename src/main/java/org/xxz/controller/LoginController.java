package org.xxz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxz.anotation.RoleControl;


import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_inName}<p/>
 * <p>Description:<p/>
 * <p>Company: </p>
 *
 * @author litao
 * @mail llsmpsvn@gmail.com
 * @date on 2017/3/31
 */


@Controller
public class LoginController {
    @RoleControl("litao")
    @RequestMapping("login")
    @ResponseBody
    public List<String> login() {
        List<String> list = new ArrayList<>();
        list.add("1");
        System.out.println("login");
        return list;
    }

}
