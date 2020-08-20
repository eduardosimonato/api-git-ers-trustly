package com.ws;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.io.FilenameUtils;

import com.ws.exception.ErroExceptionApi;
import com.ws.util.UrlContent;
import com.ws.vo.ContentPageVO;
import com.ws.vo.FileDataVO;
import com.ws.vo.ResultApiVO;

/**
 * Root resource
 */
@Path("v1")
public class ApiGitResource {
	
	private static final String REGEX_FILES_DATA = "<div class=\"text-mono[^\"]*\">\\s*(?:([0-9]+)\\s*lines\\s*\\(([0-9]+)\\s*sloc\\)[^\\/]*\\/span>\\s*)?([0-9\\.]+\\s*(?:Bytes|KB|MB)+)";
	private static final String REGEX_LIST_FILES_PAGE = "(<div role=\"rowheader\"(.|\n)+?div)";
	private static final String GIT_ENDPOINT = "https://github.com/";
	private ResultApiVO resultvofiltered;
    		
    @GET
	@Path("/scrapgit/")
    @Consumes("text/plain")
    @Produces("text/plain")
	public String scrapingGit(String urlgit) throws ErroExceptionApi {
    	    	
		String filename="";
		String extension="";
		String hreffile="";
		String urlrefcontentStr="";
		
		if(!(urlgit.contains(GIT_ENDPOINT))) {
			return "Url is not a GIT Url!!";
		}
		
		Queue<Url> urlList = new LinkedList<Url>();
		ContentPageVO page = new ContentPageVO(urlgit);
		
		Url urlstrwhile = new Url();
		urlstrwhile.setUrlRef(page.getUrlPage());
		urlList.add(urlstrwhile);
				
		try {
									
			while (!urlList.isEmpty()) {
									
				urlstrwhile = urlList.remove();
				
				String urlStr = urlstrwhile.getUrlRef();
				
				URL url = new URL(urlStr);
							
				CharSequence pageContent = UrlContent.getURLContent(url);
				
				Matcher resultaction = this.executeRegex(pageContent, REGEX_FILES_DATA);
																			
				if((resultaction.find())){
																
						String qtdlinesS = resultaction.group(1);
    	            	    	            	
    	            	String sizeS = resultaction.group(3);
						Integer sizefile;
    	            	String[] splited = sizeS.split("\\s+");
						
    	            	Float value = Float.parseFloat(splited[0]);
						
						int mult = 1;
						if (sizeS.toLowerCase().endsWith("kb")) {
							mult = 1024;
						} else if (sizeS.toLowerCase().endsWith("mb")) {
							mult = 1024 * 1024;
						}
						
						sizefile = Math.round(value * mult);
						
						Optional<FileDataVO> fileDataVOpt = page.getFilesList().stream()
												.filter(file -> urlStr.equals(file.getUrlRefFile()))
												.findFirst();
						
						if (fileDataVOpt.isPresent()) {
							FileDataVO fileDataVO = fileDataVOpt.get();
							if(!(fileDataVO == null)) {
								fileDataVO.setQtdOfFilesLines(Integer.parseInt(qtdlinesS));
								fileDataVO.setSizeOfFile(sizefile);
							}
						}	
						
				}else{
					
					Matcher resultactionlist = this.executeRegex(pageContent, REGEX_LIST_FILES_PAGE);
					
					if( (resultactionlist.find()) ) {
																		
						StringBuffer conteudoarquivospasta = new StringBuffer();
						while (resultactionlist.find()) {
							conteudoarquivospasta.append(resultactionlist.group());
				        }
																							
						// get urlref and name of files.. 
			            Pattern listaatributos = Pattern.compile("(title=\"([^\"]*)\")|(href=\"([^\"]*)\")");
			            Matcher m2 = listaatributos.matcher(conteudoarquivospasta);
			            			            			            
			            while (m2.find()) {
			            	if ( m2.group().matches("title=\"([^\"]*)\"") ) {
			            		 filename = m2.group().substring(7, m2.group().lastIndexOf('\"'));
			            	}else if ( m2.group().matches("href=\"([^\"]*)\"") ) {
			            		hreffile = m2.group().substring(7, m2.group().lastIndexOf('\"'));
			            		Url regerurlstr = new Url();
			            		urlrefcontentStr = GIT_ENDPOINT + hreffile;
			            		regerurlstr.setUrlRef(urlrefcontentStr);
			            		urlList.add(regerurlstr);
			            		extension = FilenameUtils.getExtension(GIT_ENDPOINT + hreffile);
			            		if(!(extension.equals(""))) {
			            			FileDataVO fileDataVO = new FileDataVO();
			            			fileDataVO.setExtension(extension);
			            			fileDataVO.setUrlRefFile(urlrefcontentStr);
			            			fileDataVO.setFileName(filename);
			            			page.getFilesList().add(fileDataVO);
			            		}
			            	}		
			            }
					}
				}
			} // while..
		} catch (MalformedURLException e) {
			throw new ErroExceptionApi("Invalid URL");
		} catch (IOException e) {
			throw new ErroExceptionApi("IO Exception!!");
		}
			
		return mountResult(page);
				
	}
	
	private Matcher executeRegex(CharSequence charsequence, String regex) {	
		// scraping content
		Pattern plistaarquivospastas = Pattern.compile( regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher m = plistaarquivospastas.matcher(charsequence);
		return m;
	}
	
	private String mountResult(ContentPageVO page) {
				
		// grouping results..
		Map<String, Integer> collectSize = page.getFilesList().stream().collect(Collectors
				.groupingBy(FileDataVO::getExtension, Collectors.summingInt((FileDataVO d) -> d.getSizeOfFile())));

		Map<String, Integer> collectquantity = page.getFilesList().stream().collect(Collectors
				.groupingBy(FileDataVO::getExtension, Collectors.summingInt((FileDataVO d) -> d.getQtdOfFilesLines())));

		List<ResultApiVO> listresult = new ArrayList<ResultApiVO>();
		collectSize.forEach((key, value) -> listresult.add(new ResultApiVO(key, "", value.toString())));

		collectquantity.forEach((key, value) -> {
			Optional<ResultApiVO> resultapiopt = listresult.stream().filter(list -> list.getFileType().contains(key))
					.findFirst();
			if (resultapiopt.isPresent()) {
				resultvofiltered = resultapiopt.get();
				if (!(resultvofiltered == null)) {
					resultvofiltered.setSumQtdLines(value.toString());
				}	
			}
		});
		
		return "URLGIT: " + page.getUrlPage() + System.getProperty("line.separator") + listresult.stream().map(e -> e.toString()).reduce("", String::concat);
		
	}
	
	// class for save url´s ref to get files..
	public class Url {
		private String UrlRef="";

		public String getUrlRef() {
			return UrlRef;
		}

		public void setUrlRef(String urlRef) {
			UrlRef = urlRef;
		}
	}
    
}
