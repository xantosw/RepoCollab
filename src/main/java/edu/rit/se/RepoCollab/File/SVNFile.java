package edu.rit.se.RepoCollab.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import edu.rit.se.RepoCollab.SVNCommand;
import edu.rit.se.RepoCollab.History.DiffHunk;

public class SVNFile extends RepoFile {

	public SVNFile(String fileURL, BufferedReader input, int revision, String author) {
		super(fileURL, input, revision, author);
		//this.buildLines(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<Integer, BlameFileLine> buildLines(BufferedReader input) {
		HashMap<Integer, BlameFileLine> blameLines = new HashMap<Integer, BlameFileLine>();
		
		int lineNumber = 1;
		String line = null;

        try {
			while((line=input.readLine()) != null) {
			   BlameFileLine fileLine = new SVNBlameFileLine(line, this.getRevision() ,lineNumber);
			   blameLines.put(lineNumber,fileLine);
			   lineNumber++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blameLines;
	}

	@Override
	public ArrayList<String> getAllRevisions() {
		ArrayList<String> revisions = new ArrayList<String>();
		String line = null;	
		try {
				BufferedReader input = new SVNCommand(getFileURL()).allFileRevision();
				while((line=input.readLine()) != null) {
				   revisions.add(line);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return revisions;
	}

	@Override
	public ArrayList<String> getAllAuthors() {
		ArrayList<String> authors = new ArrayList<String>();
		String line = null;	
		try {
				BufferedReader input = new SVNCommand(getFileURL()).allAuthors();
				while((line=input.readLine()) != null) {
				   authors.add(line);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return authors;
	}

	@Override
	public ArrayList<DiffHunk> getFileDiffFromPrevVer() {
		ArrayList<DiffHunk> hunkRanges = new ArrayList<DiffHunk>();
		String line = null;	
		try {
				BufferedReader input = new SVNCommand(getFileURL()).diffNonContext(getRevision());
				while((line=input.readLine()) != null) {
				   hunkRanges.add(new DiffHunk(line));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return hunkRanges;
	}
	
}
