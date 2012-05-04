package edu.rit.se.RepoCollab;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

import edu.rit.se.RepoCollab.File.BlameFileLine;
import edu.rit.se.RepoCollab.File.RepoFile;
import edu.rit.se.RepoCollab.File.SVNBlameFileLine;
import edu.rit.se.RepoCollab.History.DiffHunk;

public class RepositoryCollabMain {

	private static String INSERT_CHURN = 
		"insert into REPO_CHURN (rev_id,file_path,author,collab_churn,num_lines_added,num_lines_deleted) values (?,?,?,?,?,?)";
	private static String INSERT_AFFECTED_DEVS =
		"insert into REPO_DEV_AFFECTED (rev_id, affected_dev, num_lines)";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            /*
             * Runtime rt = Runtime.getRuntime();
             
            //Process pr = rt.exec("cmd /c dir");
            Process pr = rt.exec("svn blame --revision 81495 https://svn.php.net/repository/php/php-src/trunk/run-tests.php");

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;
            int lineNumber = 1;
            while((line=input.readLine()) != null) {
               BlameFileLine fileLine = new SVNBlameFileLine(line, 81495, lineNumber);
               
               System.out.println(fileLine.getLineNumber() + " " + 
            		   				fileLine.getContainingFileRev() + " " + 
            		   				fileLine.getLineRev() + " " + 
            		   				fileLine.getAuthor() + " " +
            		   				fileLine.getSourceLine() + " ");
               lineNumber++;
            }
            */
			
			RepositoryCommand svnComm = new SVNCommand("/Users/xantos/PHP/trunk/run-tests.php");

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
			long start = System.currentTimeMillis();

			// Do something ...

			// Get elapsed time in milliseconds
			for(int i = 1; i < 20; i++){
				int currRevNum = Integer.parseInt(a.get(i));
				RepoFile currRev = svnComm.buildRepoFileRev(currRevNum);
				String author = currRev.getAuthor();
				
				HashMap<Integer,BlameFileLine> blamelines = currentPrevRev.getFileLines();
				
				ArrayList<DiffHunk> diffhunks = currRev.getFileDiffFromPrevVer();
				//need to capture:
				//REPO_CHRUN = (rev_id,file_path,author,collab_churn,num_lines_added,num_lines_deleted) 
				//AFF_DEV = (rev_id, affected_dev, num_lines)
				System.out.println("Revision number:" + currRevNum + " : Author:" + author);
				int collab_churn = 0;
				int num_lines_deleted = 0;
				int num_lines_added = 0;
				HashMap<String,Integer> devToNumLinesMap = new HashMap<String, Integer>();
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
				System.out.println("\t\t collabchur:" +  collab_churn + " numdel:" + num_lines_deleted + " numadd:" + num_lines_added + " loc:" + devToNumLinesMap.size());
				//for next iteration
				currentPrevRev = currRev;
			}
			long elapsedTimeMillis = System.currentTimeMillis()-start;
			System.out.println("time elasped:" + elapsedTimeMillis + "ms");
			
			//System.out.println(svnfile.getFileLines().size());
			//System.out.println("Version: " + a.get(2) + " has " + svnfilever.getFileLines().size());
			
			//System.out.println(l.size() + " number of authors");
			//System.out.println(a.size() + " number of revs");
			
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
	}

}
