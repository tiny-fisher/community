//package com.nowcoder.community.controller;
//
//
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Controller
//public class IndexController {
//
//    // 首页的路径
//    @RequestMapping(path={"/","/index"}, method = {RequestMethod.GET})
//    @ResponseBody
//    public String index(){
//        return "hello world";
//    }
//
//    // another
//    @RequestMapping(path={"/profile/{groupId}/{userId}"})
//    @ResponseBody
//    public String profile(@PathVariable("userId") int userId,
//                          @PathVariable("groupId") String groupId,
//                          @RequestParam(value = "type",defaultValue = "1", required = true)int type,
//                          @RequestParam(value = "key",defaultValue = "zz",required  = false) String key){
//        return String.format("Profile Page of %s / %d , request: request = %d / key = %s", groupId, userId,type,key);
//    }
//
//
//
//    // 演示模板
//    @RequestMapping(path={"/vm"}, method = {RequestMethod.GET})
//    public String template(Model model){
//        model.addAttribute("value1", "vvvvv1");
//        List<String> colors = Arrays.asList(new String[] {"RED","GREEN","BLUE"});
//        model.addAttribute("colors",colors);
//        return "home";
//    }
//
//
//
//
//
//}
