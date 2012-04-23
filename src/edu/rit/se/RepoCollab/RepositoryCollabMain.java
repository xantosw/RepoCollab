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

	/**
	 * @param args
	 */
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
			
			RepositoryCommand svnComm = new SVNCommand("https://svn.php.net/repository/php/php-src/trunk/run-tests.php");

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
				
				System.out.println("Revision number:" + currRevNum + " : Author:" + author);
				for(DiffHunk diff : diffhunks){
					int startline = diff.getOriginStartLine();
					int numlines = diff.getOriginNumLinesAffected();
					
					while(numlines > 0){
						int line = startline + (numlines - 1);
						System.out.println("\t line number:" + line + " Author is: " + blamelines.get(line).getAuthor() );
						numlines--;
					}
				}
				
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
