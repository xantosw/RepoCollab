package edu.rit.se.RepoCollab.History;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class takes in a diffHunk in the format:
 * @@ -l,s +l,s @@
 * -l is the line number modified from the original file and s is the number of lines
 * +l and the following s is for the modified version.
 * 
 * NOTE!!!! THIS IS ASSUMING CONTEXT HAS BEEN REMOVED FROM THE DIFFS
 * 
 * Examples:
 * @@ -157 +157,3 @@    -- line 157 changed and 2 lines were added to modified rev
 * @@ -163,2 +165,3 @@  -- line 163 and 164 changed and an extra line included in modified rev
 * @@ -279,0 +283 @@    -- a line was added after line 279 in original file
 * @@ -448 +447,0 @@    -- line 448 was deleted
 * 
 * @author xantos
 *
 */
public class DiffHunk {
	private String originStartLine;
	private String originNumLinesAffected;
	private String rawHunk;
	
	public DiffHunk(String rawHunk) throws Exception{
		this.rawHunk = rawHunk;
		this.parse();
	}

	private void parse() throws Exception{
		//find start line regex (?<=@@\s-)[0-9]+
		//find num line for origin because lookbehind needs fixed width throw in hard digits from start
		//eg (?<=@@\s-157,)[0-9]+
		
		//not needed but incase (grab modified verson start line (?<=\+)[0-9]+
		
		//for this study only need to grab lines deleted or modified from the origin
		//ArrayList<String> matchList = new ArrayList<String>();
		this.originStartLine = "###";
		
		//defaults to 1 if not included
		this.originNumLinesAffected = "1";
		
		Pattern regex = Pattern.compile("(?<=@@\\s-)[0-9]+");
		Matcher regexMatcher = regex.matcher(rawHunk);
		if(regexMatcher.find()) {
		   // System.out.println( regexMatcher.group());
			this.originStartLine = regexMatcher.group();
		} else {
			throw new Exception("Invalid diff range hunk: " + this.rawHunk);
		}
		
		//the startline needs to be part of the regex because lookbehind is fixedwidth
		regex = Pattern.compile("(?<=@@\\s-" + this.originStartLine +",)[0-9]+");
		regexMatcher = regex.matcher(rawHunk);
		if(regexMatcher.find()) {
			// System.out.println( regexMatcher.group());
			this.originNumLinesAffected = regexMatcher.group();
		} 
		//System.out.println("l=" + this.originStartLine + " :s=" + this.originNumLinesAffected);
	}
	
	public int getOriginStartLine() {
		return Integer.parseInt(originStartLine);
	}

	public int getOriginNumLinesAffected() {
		return  Integer.parseInt(originNumLinesAffected);
	}

	public String getRawHunk() {
		return this.rawHunk;
	}

	public static void main(String[] args) throws Exception{
		new DiffHunk("@@ -163,2 +165,3 @@");
		new DiffHunk("@@ -157 +157,3 @@");
		new DiffHunk("@@ -279,0 +283 @@");
		new DiffHunk("@@ -448 +447,0 @@");
	}
}
