package edu.rit.edu.RepoCollab.Bug;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import edu.rit.se.RepoCollab.RepositoryCommand;
import edu.rit.se.RepoCollab.SVNCommand;
import edu.rit.se.RepoCollab.Util.DBUtil;

public class BugSearchMain {
	private static String INSERT_GREPPED_BUG =
		"insert into GREPPED_BUGS (file_path, bug_id) values (?,?)";
	
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
			DBUtil dbUtil = new DBUtil();
			for(File file:found){
				RepositoryCommand svnComm = new SVNCommand(file.getAbsolutePath());
				ArrayList<String> matches = svnComm.getLogGrepResults("#[0-9]*");
				System.out.println(file.getAbsolutePath());
				
				for(String match: matches){
					Object[] values = {file.getAbsolutePath(), match};
					dbUtil.executeInsertSQL(INSERT_GREPPED_BUG, values);
					System.out.println("\t" + match);
				}
			}
		}catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
	}
}
