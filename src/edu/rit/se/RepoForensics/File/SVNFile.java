package edu.rit.se.RepoForensics.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class SVNFile extends RepoFile {

	public SVNFile(String fileURL, BufferedReader input, int revision) {
		super(fileURL, input, revision);
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

}
