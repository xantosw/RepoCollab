package edu.rit.se.RepoCollab;

import java.io.*;
import java.util.regex.Pattern;

import edu.rit.se.RepoCollab.File.BlameFileLine;
import edu.rit.se.RepoCollab.File.RepoFile;
import edu.rit.se.RepoCollab.File.SVNBlameFileLine;

public class RepositoryCollabMain {

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
			System.out.println(svnComm.getFileInfoByName("Name"));
			System.out.println(svnComm.getFileInfoByName("Repository Root"));
			
			RepoFile svnfile = svnComm.buildRepoFile();
			
			System.out.println(svnfile.getFileLines().size());

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
	}

}
