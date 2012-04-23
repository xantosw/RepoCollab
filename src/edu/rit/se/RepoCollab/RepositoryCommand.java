package edu.rit.se.RepoCollab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import edu.rit.se.RepoCollab.File.RepoFile;

public abstract class RepositoryCommand {
	
	protected String fileUrl;
	protected HashMap<String,String> fileInfo;
	
	public RepositoryCommand(String urlPathToFile){
		this.fileUrl = urlPathToFile;
	}
	
	
	public abstract String blameCommand();
	
	public abstract String blameRevCommand(int rev);
	
	public abstract String allFileRevisionsCommand();
	
	public abstract String allAuthorsCommand();
	
	public abstract String fileInfoCommand();
	
	public abstract String fileInfoForRevCommand(int rev);
	
	/**
	 * This command is supposed to only return the range information
	 * for each change WITHOUT the context. Using the linux diff command
	 * simplifies this when combined with the diff-cmd option for SVN for example
	 * 
	 * using previous changeset for specified revision
	 * 
	 * @return string command
	 */
	public abstract String diffNonContextCommand(int rev);
	
	public abstract HashMap<String, String> buildFileInformation();
	
	public abstract HashMap<String, String> buildFileInformationForRev(int rev);
	
	public abstract RepoFile buildRepoFile();
	
	public abstract RepoFile buildRepoFileRev(int rev);
	
	private BufferedReader executeCommand(String command) throws Exception{
		System.out.println("executing: " + command);
        ProcessBuilder myProc2 = new ProcessBuilder("/bin/sh","-c", "exec " + command);
        myProc2.redirectErrorStream(true);        
        final Process process = myProc2.start();
        InputStream myIS = process.getInputStream();
        BufferedReader input = new BufferedReader(new InputStreamReader(myIS));
        //   int exitCode = process.waitFor();
        // System.out.println("blame process exited with exit code:" + exitCode);
        return input;
	}
	
	public BufferedReader diffNonContext(int rev) throws Exception{
		return executeCommand(this.diffNonContextCommand(rev));
	}
	
	public BufferedReader blame() throws Exception{
		return  executeCommand(this.blameCommand());
	}
	
	public BufferedReader blameRev(int rev) throws Exception{
        return executeCommand(this.blameRevCommand(rev));
	}
	
	public BufferedReader allFileRevision() throws Exception{
		return executeCommand(this.allFileRevisionsCommand());
	}
	
	public BufferedReader fileInfo() throws Exception{
		return executeCommand(this.fileInfoCommand());
	}
	
	public BufferedReader fileInfoForRev(int rev) throws Exception{
		return executeCommand(this.fileInfoForRevCommand(rev));
	}
	
	
	public BufferedReader allAuthors() throws Exception{
		return executeCommand(this.allAuthorsCommand());
		
	}
	
//	public String getFileInfoByName(String key){
//		return this.fileInfo.get(key);
//	}
	
	public String getFileURL(){
		return this.fileUrl;
	}	
}
