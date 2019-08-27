package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {


    // 为了调用 Service
    @Autowired
    private AlphaService alphaService;



    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }


    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }


    @RequestMapping("/http")
    public void http(HttpServletRequest  request, HttpServletResponse response){
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration= request.getHeaderNames(); // 得到所有请求行的key  是迭代器
        while(enumeration.hasMoreElements()){
            String name  = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " +value);
        }
        System.out.println(request.getParameter("code"));

        // return response data
        response.setContentType("text/html;charset=utf-8");
        try (
            PrintWriter writer  =  response.getWriter();
        ){

            writer.write("<hl>牛客网</hl>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // 处理请求数据
    // GET 请求

    // /students?current=1&limit=20
    @RequestMapping(path="/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);

        return "some student";
    }


    // /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent (@PathVariable("id") int id){
        System.out.println(id);
        return "a student";

    }


    //POST 请求
    @RequestMapping(path ="/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, double age){
        System.out.println(name);
        System.out.println(age);
        return "post success";
    }



    //向浏览器 响应动态HTML数据

    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", 30);
        mav.setViewName("/demo/view");
        return mav;

    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getScholl(Model model){
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", 121);

        return "/demo/view";
    }


    // 异步请求 当前网页不刷新,
    // 响应 json 数据
    // Java 对象  -> JSon 字符串 -> JS对象

    @RequestMapping(path = "/emp", method  = RequestMethod.GET)
    @ResponseBody // 不加这个以为返回的是 html
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        return emp;
    }


    // 返回一组员工
    @RequestMapping(path = "/emps", method  = RequestMethod.GET)
    @ResponseBody // 不加这个以为返回的是 html
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", 27);
        emp.put("salary", 10000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", 31);
        emp.put("salary", 12000.00);
        list.add(emp);


        return list;
    }



    //cockie 示例

    @RequestMapping(path = "/cookie/test", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置 cookie 生效的范围
        cookie.setPath("/community/alpha");
        // set the lifetime of cookie, into the hardware;
        cookie.setMaxAge(60 * 10);
        // 发送 cookie;
        response.addCookie(cookie);

        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }


    // session demo;
    // session set;
    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody

// Session 只要声明, SpringMVC 就能给你注入进来;

    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }


    // session get
    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody

    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));

        return "get session";
    }



}
