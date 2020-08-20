package com.ws.vo;

import java.util.ArrayList;
import java.util.List;

public class ContentPageVO {
	
	private String urlPage;
	private List<FileDataVO> filesList;
	
	public ContentPageVO(String urlRefFile) {
		super();
		this.urlPage = urlRefFile;
		filesList = new ArrayList<FileDataVO>();
	}	

	public String getUrlPage() {
		return urlPage;
	}

	public void setUrlPage(String urlPage) {
		this.urlPage = urlPage;
	}

	public List<FileDataVO> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<FileDataVO> filesList) {
		this.filesList = filesList;
	}
	
}
