package edu.rit.se.RepoCollab.File;
import java.io.BufferedReader;
import java.util.*;

import edu.rit.se.RepoCollab.History.DiffHunk;

public abstract class RepoFile {
	protected String fileURL;
	protected HashMap<Integer,BlameFileLine> fileLines;
	protected int revision;
	protected String author;
//	protected LinkedList<String> allRevisions;
//	protected LinkedList<String> allAuthors;
	
	public RepoFile(String fileURL, BufferedReader input, int revision, String author){
		this.fileURL = fileURL;
		this.fileLines = this.buildLines(input);
		this.revision = revision;
		this.author = author;
	}
	
	//need to refactor this to not take a bufferedReader...
	public abstract HashMap<Integer, BlameFileLine> buildLines(BufferedReader input);
	public abstract ArrayList<String> getAllRevisions();
	public abstract ArrayList<String> getAllAuthors();
	
	public abstract ArrayList<DiffHunk> getFileDiffFromPrevVer();

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public HashMap<Integer, BlameFileLine> getFileLines() {
		return fileLines;
	}

	public void setFileLines(HashMap<Integer, BlameFileLine> fileLines) {
		this.fileLines = fileLines;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}	
	
	public String getAuthor() {
		return this.author;
	}

	public void setRevision(String author) {
		this.author = author;
	}
	
}
