package com.mtech.image.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mtech.image.model.User;
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

	@Value("${linkValidityTimeInMinutes}")
	private String linkValidityTimeInMinutes;

	@Value("${max-file-size}")
	private String maxFileSize;
	
	@GetMapping("/upload")
	public String index() {
		return "upload";
	}

	@Autowired
	private SendMail sendMail;

	@Autowired
	private AESenc aesenc;

	@PostMapping("/upload")
	public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("email") String emailToShare, RedirectAttributes redirectAttributes) throws Exception {

		ModelAndView m = new ModelAndView("upload");
		
		if (file.isEmpty()) {
			m.addObject("errorFlag", true);
			m.addObject("message", "Select a file to share.");
			return m;
		}
		
		if(file.getOriginalFilename().split("\\.")[1].equalsIgnoreCase(".exe")) {
			m.addObject("errorFlag", true);
			m.addObject("message", "Exe files not allowed.");
			return m;
		}
		
/*		if(Long.parseLong(maxFileSize)*1024*1024 <= file.getSize()*1024*1024) {
			m.addObject("errorFlag", true);
			m.addObject("message", "Size of file should be less than 10 MegaBytes.");
			return m;
		}*/

		if(StringUtils.isEmpty(emailToShare)) {
			m.addObject("errorFlag", true);
			m.addObject("message", "Enter an email address.");
			return m;
		}
		
		if(!isValidEmailAddress(emailToShare)) {
			m.addObject("errorFlag", true);
			m.addObject("message", "Entered email address is incorrect.");
			return m;
		}

		try {
			File fileObjectForRootdirectory = new File(rootDirectoryPath);
			if(!fileObjectForRootdirectory.exists()) {
				fileObjectForRootdirectory.mkdirs();
			}

			byte[] bytes = file.getBytes();
			Path path = Paths.get(rootDirectoryPath + file.getOriginalFilename());
			Files.write(path, bytes);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'. Email Sent to user");

			User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String filePath= domainName + contextPath 
					+"/downloadFile?username="+loggedInUser.getFirstName()
					+ "&file=" + URLEncoder.encode(aesenc.encrypt((new Date().getTime()+"|"+file.getOriginalFilename()).toString()),"UTF8");

			sendMail.sendEmail(
					file.getOriginalFilename(), 
					filePath, 
					"",
					loggedInUser.getFirstName()+(StringUtils.isEmpty(loggedInUser.getLastName())?"":loggedInUser.getLastName()), 
					emailToShare);
			m.addObject("errorFlag", false);
			m.addObject("message", "File uploaded successfully. Email sent to reciever with a link which is valid for 1 minute only.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return m;
	}

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	@RequestMapping(value="/downloadFile")
	public void getLogFile(
			@RequestParam("username") String username,
			@RequestParam("file") String encryptedString,
			HttpServletResponse response) throws Exception {
		String decodeString = null;
		if(!StringUtils.isEmpty(encryptedString)) 
			decodeString = aesenc.decrypt(encryptedString);
		else {
			throw new Exception("Invalid Url Call");
		}

		String timeStamp = decodeString.split("\\|")[0];
		if(!checkIfTimeIsPast(timeStamp)) {
			response.getWriter().write("Link Expired");
		}
		else {
			String fileName = decodeString.split("\\|")[1];
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
		if(new Date().getTime() - Long.parseLong(timeInMillis) >= (Integer.parseInt(linkValidityTimeInMinutes)*1000)) {
			return false;
		}
		return true;
	}

	@RequestMapping(value="/encryptData")
	public void encrypt(@RequestParam("data") String dataToEncrypt) throws Exception {
		System.out.println(aesenc.encrypt(dataToEncrypt));
	}

	@RequestMapping(value="/decryptData")
	public void decrypt(@RequestParam("data") String dataToDecrypt) throws Exception {
		System.out.println(aesenc.decrypt(dataToDecrypt));
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
}
