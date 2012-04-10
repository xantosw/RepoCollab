package edu.rit.se.RepoForensics.File;
import java.io.BufferedReader;
import java.util.*;

public abstract class RepoFile {
	protected String fileURL;
	protected HashMap<Integer,BlameFileLine> fileLines;
	protected int revision;
	
	public RepoFile(String fileURL, BufferedReader input, int revision){
		this.fileURL = fileURL;
		this.fileLines = this.buildLines(input);
		this.revision = revision;
	}
	
	public abstract HashMap<Integer, BlameFileLine> buildLines(BufferedReader input);

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
}
