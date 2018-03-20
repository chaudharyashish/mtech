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
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mtech.image.model.User;
import com.mtech.image.repository.UserRepository;
import com.mtech.image.utiities.AESenc;
import com.mtech.image.utiities.SendMail;

@Controller
public class UploadController {

	@Value("${rootPath}")
	private String rootDirectory;

	@Value("${domainName}")
	private String domainName;

	@Value("${server.contextPath}")
	private String contextPath;

	@Value("${linkValidityTimeInSeconds}")
	private String linkValidityTimeInSeconds;

	@Value("${max-file-size}")
	private String maxFileSize;
	
	@Autowired
	private SendMail sendMail;

	@Autowired
	private AESenc aesenc;
	
	@Autowired
	private UserRepository userRepository;

	
	@GetMapping("/upload")
	public ModelAndView index() {
		ModelAndView m = new ModelAndView("upload");
		m.addObject("users", userRepository.findAll());
		return m;
	}
	
	@PostMapping("/upload")
	public ModelAndView singleFileUpload(@RequestParam(name="file") MultipartFile file,
			@RequestParam(name="emailToShare", required=false) List<String> emailToShare) throws Exception {

		ModelAndView m = new ModelAndView("upload");
		m.addObject("users", userRepository.findAll());
		if (file.isEmpty()) {
			m.addObject("errorFlag", true);
			m.addObject("message", "Select a file to share.");
			return m;
		}
		else if(CollectionUtils.isEmpty(emailToShare)) {
			m.addObject("errorFlag", true);
			m.addObject("message", "Select atleast one email address.");
			return m;
		}
		
		try {
			String rootDirectoryPath = System.getProperty("user.home")+rootDirectory;
			File fileObjectForRootdirectory = new File(rootDirectoryPath);
			if(!fileObjectForRootdirectory.exists()) {
				fileObjectForRootdirectory.mkdirs();
			}
			
			User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			for (String email : emailToShare) {
				File fileObjectForChildDirectory = new File(rootDirectoryPath+email);
				if(!fileObjectForChildDirectory.exists()) {
					fileObjectForChildDirectory.mkdirs();
				}

				byte[] bytes = file.getBytes();
				Path path = Paths.get(rootDirectoryPath+ email +"//" + file.getOriginalFilename());
				Files.write(path, bytes);

				User toUser = userRepository.findByUsername(email);
				String filePath= domainName + contextPath 
						+"/download?param=" 
						+ URLEncoder.encode(
											aesenc.encrypt(
												toUser.getUsername() +"|"+ 
												new Date().getTime()+"|"+
												file.getOriginalFilename())
										,"UTF8");
				sendMail.sendEmail(
					file.getOriginalFilename(), 
					filePath, 
					toUser.getFirstName(),
					loggedInUser.getFirstName()+(StringUtils.isEmpty(loggedInUser.getLastName())?"":loggedInUser.getLastName()), 
					email,
					linkValidityTimeInSeconds);
			}
			m.addObject("errorFlag", false);
			m.addObject("message", "File shared successfully. Email sent to reciever(s) with a link which is valid for " + 
							((Long.parseLong(linkValidityTimeInSeconds) < 60) 
							? linkValidityTimeInSeconds+" seconds" 
							: Long.parseLong(linkValidityTimeInSeconds)/60+" minutes")
						+" only.");
		} catch (IOException e) {
			m.addObject("errorFlag", true);
			m.addObject("message", e);
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

	@RequestMapping(value="/download")
	public void getLogFile(
			@RequestParam("param") String encryptedString,
			HttpServletResponse response) throws Exception {
		String decodeString = null;
		if(!StringUtils.isEmpty(encryptedString)) 
			decodeString = aesenc.decrypt(encryptedString);
		else {
			response.getWriter().write("Url is tempered. Invalud Url.");
			throw new Exception("Invalid Url Call");
		}
		
		String username = decodeString.split("\\|")[0];
		String timeStamp = decodeString.split("\\|")[1];
		if(null == userRepository.findByUsername(username)) {
			response.getWriter().write("Invalid user is requesting");
		}
		else if(!checkIfTimeIsPast(timeStamp)) {
			response.getWriter().write("Link Expired");
		}
		else {
			String fileName = decodeString.split("\\|")[2];
			try {
				String rootDirectoryPath = System.getProperty("user.home")+rootDirectory;
				File file = new File(rootDirectoryPath + username +"//" + fileName);
				InputStream inputStream = new FileInputStream(file);
				response.setContentType("application/force-download");
				response.setHeader("Content-Disposition", "attachment; filename="+fileName); 
				IOUtils.copy(inputStream, response.getOutputStream());
				response.flushBuffer();
				inputStream.close();
			} catch (Exception e){
				response.getWriter().write(e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private boolean checkIfTimeIsPast(String timeInMillis) {
		if(new Date().getTime() - Long.parseLong(timeInMillis) >= (Integer.parseInt(linkValidityTimeInSeconds)*1000)) {
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
