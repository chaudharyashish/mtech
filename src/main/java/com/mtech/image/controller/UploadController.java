package com.mtech.image.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mtech.image.utiities.AESenc;
import com.mtech.image.utiities.SendMail;

@Controller
public class UploadController {

	
	@Value("${rootPath}")
	private String rootDirectoryPath;
	
	@Value("${domainName}")
	private String domainName;
	
	@Value("${server.contextPath}")
	private String contextPath;
	
	
    @GetMapping("/upload")
    public String index() {
        return "upload";
    }

    @Autowired
    SendMail sendMail;
    
    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
    		@RequestParam("email") String emailToShare, RedirectAttributes redirectAttributes) throws Exception {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:upload";
        }

        try {
        	File fileObjectForRootdirectory = new File(rootDirectoryPath);
        	if(!fileObjectForRootdirectory.exists()) {
        		fileObjectForRootdirectory.mkdirs();
        	}
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(rootDirectoryPath + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'. Email Sent to user");
            
            String filePath= domainName + contextPath +"/downloadFile?username=ashish&file=" + aesenc.encrypt(Base64.getEncoder().encode((new Date().getTime()+""+file.getOriginalFilename()).getBytes()).toString());
            sendMail.sendEmail(file.getOriginalFilename(), filePath, "Ashish Gupta", "Administrator", emailToShare);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/upload";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "upload";
    }
    
    @RequestMapping(value="/downloadFile")
    public void getLogFile(
    		@RequestParam("username") String username,
    		@RequestParam("file") String encryptedString,
    		HttpServletResponse response) throws Exception {
    	String decodeString = null;
    	if(!StringUtils.isEmpty(encryptedString)) 
    		decodeString = aesenc.decrypt(new String(Base64.getDecoder().decode(encryptedString)));
    	else {
    		throw new Exception("Invalid Url Call");
    	}
    	
    	String timeStamp = decodeString.split("+")[0];
    	if(!checkIfTimeIsPast(timeStamp)) {
    		
    	}
    	else {
    		String fileName = decodeString.split("+")[1];
    		try {
    			File file = new File(rootDirectoryPath + fileName);
    			InputStream inputStream = new FileInputStream(file);
    			response.setContentType("application/force-download");
    			response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
    			IOUtils.copy(inputStream, response.getOutputStream());
    			response.flushBuffer();
    			inputStream.close();
    		} catch (Exception e){
    			e.printStackTrace();
    		}
    	}

    }
    
    private boolean checkIfTimeIsPast(String timeInMillis) {
    	if(new Date().getTime() - Long.parseLong(timeInMillis) >= 60*1000) {
    		return false;
    	}
    	return true;
    }
    
    @Autowired
    private AESenc aesenc;
    
    @RequestMapping(value="/encryptData")
    public void encrypt(@RequestParam("data") String dataToEncrypt) throws Exception {
    	System.out.println(aesenc.encrypt(dataToEncrypt));
    }
    
    @RequestMapping(value="/decryptData")
    public void decrypt(@RequestParam("data") String dataToDecrypt) throws Exception {
    	System.out.println(aesenc.decrypt(dataToDecrypt));
    }
    
    @RequestMapping(value="/prepareDownloadUrl")
    public void prepareDownloadUrl(@RequestParam("fileName") String fileName) {
    	
    }
}
