package edu.rit.se.RepoCollab;

import java.io.BufferedReader;
import java.util.HashMap;

import edu.rit.se.RepoCollab.File.BlameFileLine;
import edu.rit.se.RepoCollab.File.RepoFile;
import edu.rit.se.RepoCollab.File.SVNBlameFileLine;
import edu.rit.se.RepoCollab.File.SVNFile;

public class SVNCommand extends RepositoryCommand{

	private final String REPLACE_URL_SEQ = "#URL";
	private final String REPLACE_REV_SEQ = "#REV";
	
	private final String SVNBLAME = "svn blame #URL";
	private final String SVNBLAMEREV = "svn blame --revision #REV #URL";
	private final String SVNALLFILEREV = "svn log -q #URL | grep -E -e \"^r[[:digit:]]+\" -o | cut -c2- | sort -n";
	private final String SVNFILEINFO = "svn info #URL";
	private final String SVNFILEINFOREV = "svn info -r #REV #URL";
	private final String SVNALLAUTHORS = "svn log --quiet #URL | grep \"^r\" | awk '{print $3}' | sort | uniq";
	//using linux diff command instead of svn, to get rid of contexts in diff and grep to only grab the ranges"
	private final String SVNDIFFNONCONTEXT = "svn diff --diff-cmd=diff -x -U0 -c #REV #URL | grep @@";
	
	public SVNCommand(String urlPathToFile) {
		super(urlPathToFile);
	}

	@Override
	public String blameCommand() {
		String command = SVNBLAME.replace( REPLACE_URL_SEQ, this.getFileURL());
		return command;
	}

	@Override
	public String blameRevCommand(int rev) {
		String command = SVNBLAMEREV.replace( REPLACE_URL_SEQ, this.getFileURL());
		command = command.replace(REPLACE_REV_SEQ, String.valueOf(rev));
		return command;
	}
	
	@Override
	public String diffNonContextCommand(int rev) {
		String command = SVNDIFFNONCONTEXT.replace( REPLACE_URL_SEQ, this.getFileURL());
		command = command.replace(REPLACE_REV_SEQ, String.valueOf(rev));
		return command;
	}

	@Override
	public String allFileRevisionsCommand() {
		String command = SVNALLFILEREV.replace( REPLACE_URL_SEQ, this.getFileURL());
		return command;
	}

	@Override
	public String fileInfoCommand() {
		String command = SVNFILEINFO.replace( REPLACE_URL_SEQ, this.getFileURL());
		return command;
	}

	@Override
	public String fileInfoForRevCommand(int rev) {
		String command = SVNFILEINFOREV.replace( REPLACE_URL_SEQ, this.getFileURL());
		command = command.replace(REPLACE_REV_SEQ, String.valueOf(rev));
		return command;
	}

	
	@Override
	public String allAuthorsCommand() {
		String command = SVNALLAUTHORS.replace( REPLACE_URL_SEQ, this.getFileURL());
		return command;
	}

	
	@Override
	public HashMap<String, String> buildFileInformation() {
		HashMap<String, String> fileInfo = new HashMap<String, String>();
		try {
			BufferedReader input = this.fileInfo();
			String line = null;
			while((line=input.readLine()) != null) {
				/*
				 * Path: run-tests.php
					Name: run-tests.php
					URL: https://svn.php.net/repository/php/php-src/trunk/run-tests.php
					Repository Root: https://svn.php.net/repository
					Repository UUID: c90b9560-bf6c-de11-be94-00142212c4b1
					Revision: 324977
					Node Kind: file
					Last Changed Author: mike
					Last Changed Rev: 324319
					Last Changed Date: 2012-03-17 05:35:25 -0400 (Sat, 17 Mar 2012)
				 * 
				 */
					if(!line.trim().equals("")){
						String[] infoPair = line.split(":",2);
						fileInfo.put(infoPair[0].trim(),infoPair[1].trim());
					}
					
				}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileInfo;
	}
	
	@Override
	public HashMap<String, String> buildFileInformationForRev(int rev) {
		HashMap<String, String> fileInfo = new HashMap<String, String>();
		try {
			BufferedReader input = this.fileInfoForRev(rev);
			String line = null;
			while((line=input.readLine()) != null) {
				/*
				 * Path: run-tests.php
					Name: run-tests.php
					URL: https://svn.php.net/repository/php/php-src/trunk/run-tests.php
					Repository Root: https://svn.php.net/repository
					Repository UUID: c90b9560-bf6c-de11-be94-00142212c4b1
					Revision: 324977
					Node Kind: file
					Last Changed Author: mike
					Last Changed Rev: 324319
					Last Changed Date: 2012-03-17 05:35:25 -0400 (Sat, 17 Mar 2012)
				 * 
				 */
					if(!line.trim().equals("")){
						String[] infoPair = line.split(":",2);
						fileInfo.put(infoPair[0].trim(),infoPair[1].trim());
					}
					
				}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileInfo;
	}
	
	@Override
	public RepoFile buildRepoFile() {
		RepoFile svnFile = null;
		try {
			BufferedReader input = this.blame();
			String fileUrl = this.getFileURL();
			
			HashMap<String,String> fileinfo = this.buildFileInformation();
			
			
			int latestRevision = Integer.parseInt(fileinfo.get("Last Changed Rev"));
			String author = fileinfo.get("Last Changed Author");
			
			svnFile = new SVNFile(fileUrl,input,latestRevision, author);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return svnFile;
	}

	@Override
	public RepoFile buildRepoFileRev(int rev) {
		RepoFile svnFile = null;
		try {
			BufferedReader input = this.blameRev(rev);
			String fileUrl = this.getFileURL();
			HashMap<String,String> fileinfo = this.buildFileInformationForRev(rev);
			
			String author = fileinfo.get("Last Changed Author");
		
			svnFile = new SVNFile(fileUrl,input,rev,author);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return svnFile;
	}


}
