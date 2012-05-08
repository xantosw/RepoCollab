package edu.rit.se.RepoCollab;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import edu.rit.se.RepoCollab.File.BlameFileLine;
import edu.rit.se.RepoCollab.File.RepoFile;
import edu.rit.se.RepoCollab.File.SVNBlameFileLine;
import edu.rit.se.RepoCollab.History.DiffHunk;
import edu.rit.se.RepoCollab.Util.DBUtil;

public class RepositoryCollabMain {

	private static String INSERT_CHURN = 
		"insert into REPO_CHURN (rev_id,file_path,author,collab_churn,num_lines_added,num_lines_deleted) values (?,?,?,?,?,?)";
	private static String INSERT_AFFECTED_DEVS =
		"insert into REPO_DEV_AFFECTED (rev_id, affected_dev, num_lines, file_path) values (?,?,?,?)";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		try {
            String[] ext = {"c","h"};
			System.out.println("Grabing all c and h file from \"/Users/xantos/PHP/trunk/\" Directory...");
			Collection<File> found = FileUtils.listFiles(new File("/Users/xantos/PHP/trunk/main"), ext, true);
//			    for (File f : found) {
//			      System.out.println("Found file: " + f);
//			    } 
			System.out.println(found.size() + " files found...");
			for(File file:found){
				//RepositoryCommand svnComm = new SVNCommand("/Users/xantos/PHP/trunk/run-tests.php");
				RepositoryCommand svnComm = new SVNCommand(file.getAbsolutePath());
				RepoFile svnfile = svnComm.buildRepoFile();
				
				//ArrayList<String> l = svnfile.getAllAuthors();
				ArrayList<String> a = svnfile.getAllRevisions();
				
				//RepoFile svnfilever = svnComm.buildRepoFileRev(Integer.parseInt(a.get(2)));
				
				//start from the second revision, since the first is the creation
				
				//ArrayList<DiffHunk> d = svnfilever.getFileDiffFromPrevVer();
				
				//System.out.println("the author of the change is: " + svnfilever.getAuthor());
				//System.out.println("The diffs from the previous versons for revision:" + a.get(2) + " is:");
				
				RepoFile currentPrevRev = svnComm.buildRepoFileRev(Integer.parseInt(a.get(0)));
				// Get current time
				
	
				// Do something ...
	
				// Get elapsed time in milliseconds
				for(int i = 1; i < 50 && i < a.size(); i++){
					int currRevNum = Integer.parseInt(a.get(i));
					RepoFile currRev = svnComm.buildRepoFileRev(currRevNum);
					String filepath = currRev.getFileURL();
					String author = currRev.getAuthor();
					
					HashMap<Integer,BlameFileLine> blamelines = currentPrevRev.getFileLines();
					
					ArrayList<DiffHunk> diffhunks = currRev.getFileDiffFromPrevVer();
					//need to capture:
					
					System.out.println("Revision number:" + currRevNum + " : Author:" + author);
					int collab_churn = 0;
					int num_lines_deleted = 0;
					int num_lines_added = 0;
					HashMap<String,Integer> devToNumLinesMap = new HashMap<String, Integer>();
					
					DBUtil dbUtil = new DBUtil();
					
					for(DiffHunk diff : diffhunks){
						int startline = diff.getOriginStartLine();
						int startnumlines = diff.getOriginNumLinesAffected();
						int deststartline = diff.getDestStartLine();
						int destnumlines = diff.getDestNumLines();
						
						while(startnumlines > 0){
							int line = startline + (startnumlines - 1);
							String owner = blamelines.get(line).getAuthor(); 
							System.out.println("\t line number:" + line + " Author is: " + owner );
							
							if(!author.equalsIgnoreCase(owner)){
								collab_churn++;
								if(!devToNumLinesMap.containsKey(owner)){
									devToNumLinesMap.put(owner, 1);
								}else{
									devToNumLinesMap.put(owner, devToNumLinesMap.get(owner) + 1);
								}
								
							}
							num_lines_deleted++;
							startnumlines--;
						}
						
						num_lines_added += diff.getDestNumLines();
					}
					for (Entry<String, Integer> entry : devToNumLinesMap.entrySet()) {
					    String owner = entry.getKey();
					    Object numlines = entry.getValue();
					 
						//AFF_DEV = (rev_id, affected_dev, num_lines)
					    Object[] values = {currRevNum,owner,numlines,filepath};
						dbUtil.executeInsertSQL(INSERT_AFFECTED_DEVS, values);
						
					}
					//REPO_CHRUN = (rev_id,file_path,author,collab_churn,num_lines_added,num_lines_deleted) 
					Object[] values = {currRevNum,filepath,author,collab_churn,num_lines_added,num_lines_deleted};
					dbUtil.executeInsertSQL(INSERT_CHURN, values);
					
					System.out.println("\t\t collabchur:" +  collab_churn + " numdel:" + num_lines_deleted + " numadd:" + num_lines_added + " loc:" + devToNumLinesMap.size());
					//for next iteration
					currentPrevRev = currRev;
				}
				
			
			//System.out.println(svnfile.getFileLines().size());
			//System.out.println("Version: " + a.get(2) + " has " + svnfilever.getFileLines().size());
			
			//System.out.println(l.size() + " number of authors");
			//System.out.println(a.size() + " number of revs");
			}
			double elapsedTimeMillis = ((System.currentTimeMillis()-start)/1000.0/60.0);
			System.out.println("time elasped:" + elapsedTimeMillis + " minutes");
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
	}

}
