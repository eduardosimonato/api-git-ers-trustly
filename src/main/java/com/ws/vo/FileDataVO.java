package com.ws.vo;

public class FileDataVO {

	private String 	fileName;
	private String 	urlRefFile;
	private Integer qtdOfFilesLines;
	private Integer sizeOfFile;
	private String 	extension;
	
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
		
	public String getFileName() {
		return fileName;
	}
	public String getUrlRefFile() {
		return urlRefFile;
	}
	public void setUrlRefFile(String urlRefFile) {
		this.urlRefFile = urlRefFile;
	}
	public Integer getQtdOfFilesLines() {
		return qtdOfFilesLines;
	}
	public void setQtdOfFilesLines(Integer qtdOfFilesLines) {
		this.qtdOfFilesLines = qtdOfFilesLines;
	}
	public Integer getSizeOfFile() {
		return sizeOfFile;
	}
	public void setSizeOfFile(Integer sizeOfFile) {
		this.sizeOfFile = sizeOfFile;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
		
}
