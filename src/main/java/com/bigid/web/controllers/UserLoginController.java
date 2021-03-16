package com.bigid.web.controllers;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bigid.exceptions.ServerValidationException;
import com.bigid.services.SecurityService;
import com.bigid.services.UserService;
import com.bigid.web.commands.UserCommand;
import com.bigid.web.common.Constants;
import com.bigid.web.controllers.validator.UserRegistrationValidator;

@Controller
public class UserLoginController {
	
	private final Logger logger = Logger.getLogger(UserLoginController.class);
    
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private UploadController imageUploadController;

    @Autowired
    private UserRegistrationValidator userValidator;

    /**This API is used to find the registration details
     * @param model
     * @return
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new UserCommand());

        return "registration";
    }

    /**	This API is registration API to register a new user with their details
     * @param userForm
     * @param bindingResult
     * @param model
     * @param uploadAvtarImg
     * @param redir
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@ModelAttribute("userForm") UserCommand userForm, BindingResult bindingResult, Model model,
    		@RequestParam(name="uploadAvtarImg", required=false) MultipartFile uploadAvtarImg, RedirectAttributes redir) throws IOException {
        userValidator.validate(userForm, bindingResult);
        
        logger.info("new regsiter user details  @@:"+userForm);
        ModelAndView modelAndView = new ModelAndView();
        
        if (bindingResult.hasErrors()) {
        	throw new ServerValidationException("Invalid request", bindingResult);
        }
        
        if(uploadAvtarImg != null){
	        Map<String, String> uploadImageMap = imageUploadController.uploadImage(uploadAvtarImg, Constants.USER_PROFILE);
	        //TODO: Need to save images in hierarchical format, so that images operation can be focused rather scattered all over.
	        if(uploadImageMap.get("status").equals("success")){
	        	userForm.setAvatarImgPath(uploadImageMap.get("imgURL"));
	        }
        }
    	userService.saveOrUpdate(userForm);
    	securityService.autologin(userForm.getUsername(), userForm.getPassword());
    	
    	modelAndView.setViewName("redirect:/welcome");
    	//this makes sure usercommand is availble to redirect url.
        redir.addFlashAttribute("userCommand",userForm);
        return modelAndView;
    }

    /**
     * @param model
     * @param error
     * @param logout
     * @return
     */
    @RequestMapping(value = {"/login","/logout"}, method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
    	logger.info("Login ====@@@=====  Logout");
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    /**
     * @param model
     * @return
     */
    @RequestMapping(value = {"/demo-upload"}, method = RequestMethod.GET)
    public String imageUpload(Model model) {
    	logger.info("======Demo Upload=======");
        return "image_upload";
    }
    
    /**
     * @param model
     * @return
     */
    @RequestMapping(value = {"/" ,"/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
    	logger.info("======Welcome========");
        return "welcome";
    }
    
}