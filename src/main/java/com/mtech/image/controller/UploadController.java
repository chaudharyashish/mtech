package com.mtech.image.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mtech.image.utiities.AESenc;

@Controller
public class UploadController {

	
	@Value("${rootPath}")
	String rootDirectoryPath;

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "F://temp//";

    @GetMapping("/upload")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
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
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
    
    @RequestMapping(value="/downloadFile")
    public void getLogFile(
    		@RequestParam("file") String encodedString,
    		HttpServletResponse response) throws Exception {
    	String decodeString = null;
    	if(!StringUtils.isEmpty(encodedString)) 
    		decodeString = new String(Base64.getDecoder().decode(encodedString));
    	else {
    		throw new Exception("Invalid Url Call");
    	}
        try {
        	/*String fileName="a.docx";
            File file = new File(rootDirectoryPath + fileName);
            InputStream inputStream = new FileInputStream(file);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();*/
        } catch (Exception e){
            e.printStackTrace();
        }

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
