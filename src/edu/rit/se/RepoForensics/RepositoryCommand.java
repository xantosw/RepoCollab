package edu.rit.se.RepoForensics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import edu.rit.se.RepoForensics.File.RepoFile;

public abstract class RepositoryCommand {
	
	protected String fileUrl;
	protected HashMap<String,String> fileInfo;
	
	public RepositoryCommand(String urlPathToFile){
		this.fileUrl = urlPathToFile;
		try {
			this.fileInfo = this.buildFileInformation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public abstract String blameCommand();
	
	public abstract String blameRevCommand(int rev);
	
	public abstract String allFileRevisionsCommand();
	
	public abstract String fileInfoCommand();
	
	public abstract HashMap<String, String> buildFileInformation();
	
	public abstract RepoFile buildRepoFile();
	
	public abstract RepoFile buildRepoFileRev(int rev);
	
	public BufferedReader blame() throws Exception{
		System.out.println("executing: " + this.blameCommand());
         ProcessBuilder myProc2 = new ProcessBuilder("/bin/sh","-c", "exec " + this.blameCommand());
         myProc2.redirectErrorStream(true);        
         final Process process = myProc2.start();
         InputStream myIS = process.getInputStream();
        
         BufferedReader input = new BufferedReader(new InputStreamReader(myIS));

         System.out.println("blame Exited ");
         return input;
	}
	
	public BufferedReader blameRev(int rev) throws Exception{
		System.out.println("executing: " + this.blameRevCommand(rev));
	
		ProcessBuilder myProc2 = new ProcessBuilder("/bin/sh","-c", "exec " + this.blameRevCommand(rev));
        myProc2.redirectErrorStream(true);        
        final Process process = myProc2.start();
        InputStream myIS = process.getInputStream();
  
        BufferedReader input = new BufferedReader(new InputStreamReader(myIS));
        
        System.out.println("blameRev Exited");
        return input;
	}
	
	public BufferedReader allFileRevision() throws Exception{
		System.out.println("executing: " + this.allFileRevisionsCommand());
		
		ProcessBuilder myProc2 = new ProcessBuilder("/bin/sh","-c", "exec " + this.allFileRevisionsCommand());
        myProc2.redirectErrorStream(true);        
        final Process process = myProc2.start();
        InputStream myIS = process.getInputStream();
  
        BufferedReader input = new BufferedReader(new InputStreamReader(myIS));        
		System.out.println("allFileRevision Exited");
        return input;
	}
	
	public BufferedReader fileInfo() throws Exception{
		System.out.println("executing: " + this.fileInfoCommand());
		
		ProcessBuilder myProc2 = new ProcessBuilder("/bin/sh","-c", "exec " + this.fileInfoCommand());
        myProc2.redirectErrorStream(true);        
        final Process process = myProc2.start();
        InputStream myIS = process.getInputStream();
  
        BufferedReader input = new BufferedReader(new InputStreamReader(myIS));
        System.out.println("fileInfo Exited");
        return input;
	}
	
	public String getFileInfoByName(String key){
		return this.fileInfo.get(key);
	}
	
	public String getFileURL(){
		return this.fileUrl;
	}
	
	
}
