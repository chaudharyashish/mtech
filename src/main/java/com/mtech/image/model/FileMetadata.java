package com.mtech.image.model;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class FileMetadata {
	
	private Long id;
	private String path;
	private Date uploadedDate;
	private User uploadedBy;
	private User sharedWith;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	public User getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(User uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	
	@OneToOne(fetch=FetchType.EAGER, optional=true)
	public User getSharedWith() {
		return sharedWith;
	}
	public void setSharedWith(User sharedWith) {
		this.sharedWith = sharedWith;
	}
	
	@Override
	public String toString() {
		return "FileMetadata [id=" + id + ", path=" + path + ", uploadedDate=" + uploadedDate + ", uploadedBy="
				+ uploadedBy + ", sharedWith=" + sharedWith + "]";
	}
}