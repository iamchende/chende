package cn.suse.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(){
		return "/index";
	}
	@RequestMapping("/welcome")
	public String welcome(HashMap<String, Object> map){
		return "/view/welcome";
	}
	@RequestMapping("/sell")
	public String sell(HashMap<String, Object> map){
		return "/view/sell";
	}
	@RequestMapping("/i_purchase")
	public String purchase(HashMap<String, Object> map){
		return "/view/purchase";
	}
	@RequestMapping("/temp")
	public String temp(HashMap<String, Object> map){
		return "/view/temp";
	}
}
