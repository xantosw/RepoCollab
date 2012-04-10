package edu.rit.se.RepoForensics.File;

public abstract class BlameFileLine {
	
	protected String rawLine;
	protected int containingFileRev;
	protected int lineRev;
	protected String source;
	protected String author;
	protected int lineNumber;
	
	public BlameFileLine(String rawLine, int containtingFileRev, int lineNumber){
		this.rawLine = rawLine;
		this.containingFileRev = containtingFileRev;
		this.lineNumber = lineNumber;
	}
	
	abstract void parse();

	public String getRawLine() {
		return rawLine;
	}

	public void setRawLine(String rawLine) {
		this.rawLine = rawLine;
	}

	public int getContainingFileRev() {
		return containingFileRev;
	}

	public void setContainingFileRev(int containingFileRev) {
		this.containingFileRev = containingFileRev;
	}

	public int getLineRev() {
		return lineRev;
	}

	public void setLineRev(int lineRev) {
		this.lineRev = lineRev;
	}

	public String getSourceLine() {
		return source;
	}

	public void setSourceLine(String sourceLine) {
		this.source = sourceLine;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	

}
