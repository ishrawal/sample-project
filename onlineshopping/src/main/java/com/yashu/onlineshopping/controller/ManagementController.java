package com.yashu.onlineshopping.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.yashu.onlineshopping.validator.ProductValidator;
import com.yashu.shoppingbackend.dao.CategoryDAO;
import com.yashu.shoppingbackend.dao.ProductDAO;
import com.yashu.shoppingbackend.dto.Category;
import com.yashu.shoppingbackend.dto.Product;

@Controller
@RequestMapping("/manage")
public class ManagementController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	private static final Logger logger= LoggerFactory.getLogger(ManagementController.class);
	
	@RequestMapping(value="/products", method=RequestMethod.GET)
	public ModelAndView showManageProducts(@RequestParam(name="operation",required=false)String operation){
		ModelAndView mv= new ModelAndView("page");
		
		mv.addObject("userClickManageProducts", true);
		mv.addObject("title", "Manage Products");
		
		Product nproduct= new Product();
		
		//set few of the fields
        nproduct.setSupplierId(1);
        nproduct.setActive(true);
        
        mv.addObject("product", nproduct);
        
        if(operation!=null){
        	if(operation.equals("product")){
        		mv.addObject("message", "Product Submitted Successfully!");
        	}
        	
        }
			
		
		return mv;
	}
	
	@ModelAttribute("categories")
	public List<Category> getCategories(){
		
		return categoryDAO.list();
	}
	
	@RequestMapping(value="/products", method=RequestMethod.POST)
	public String handleProductSubmission(@Valid  @ModelAttribute("product") Product mproduct,BindingResult result,Model model){
		
		
		
		new ProductValidator().validate(mproduct, result);
		
		
		//check it there are any errors
		
		if(result.hasErrors()){
			model.addAttribute("userClickManageProducts", true);
			model.addAttribute("title", "Manage Products");
			model.addAttribute("message", "Validation failed for Product Submission!");
			
			return "page";
		}
		
		
		logger.info(mproduct.toString());
		
		productDAO.add(mproduct);
		
		return "redirect:/manage/products?operation=product";
	}


}
