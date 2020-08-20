package com.ws.vo;

public class ResultApiVO {
	
	private String fileType;
	private String sumQtdLines;
	private String sumSizeType;
	
	private static final String PREFIXTYPE = " Type: ";
	private static final String PREFIXTOTOFLINES = " Lines: ";
	private static final String PREFIXTOTOFSIZE =  " Size: ";
	
	public ResultApiVO(String fileType, String sumQtdLines, String sumSizeType) {
		super();
		this.fileType = PREFIXTYPE + fileType;
		this.sumQtdLines = PREFIXTOTOFLINES + sumQtdLines;
		this.sumSizeType = PREFIXTOTOFSIZE + sumSizeType + " Bytes;";
	}
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = PREFIXTYPE + fileType;
	}
	public String getSumQtdLines() {
		return sumQtdLines;
	}
	public void setSumQtdLines(String sumQtdLines) {
		this.sumQtdLines = PREFIXTOTOFLINES + sumQtdLines;
	}
	public String getSumSizeType() {
		return sumSizeType;
	}
	public void setSumSizeType(String sumSizeType) {
		this.sumSizeType = PREFIXTOTOFSIZE + sumSizeType;
	}
	
	@Override
	public String toString() {
		String quebraLinha = System.getProperty("line.separator");
		return "[" + fileType + ", " + sumQtdLines + ", " + sumSizeType
				+ "]" + quebraLinha;
	}
			
}
