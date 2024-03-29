package edu.rit.se.RepoCollab.File;

public class SVNBlameFileLine extends BlameFileLine{

	public SVNBlameFileLine(String rawLine, int containtingFileRev, int lineNumber) {
		super(rawLine, containtingFileRev, lineNumber);
		this.parse();
	}

	@Override
	void parse(){
		String line = this.getRawLine().trim();
        String source = line.replaceFirst("^[0-9]*\\s*\\S*","" );
        String lineRev = line;
        if(!source.trim().equals("")) 
        	lineRev = line.substring(0,line.indexOf(source));
        String[] revAndAuthor = lineRev.trim().split("\\s+");
         
        setSourceLine(source);
        //System.out.println(revAndAuthor[0]);
        setLineRev(Integer.parseInt(revAndAuthor[0]));
        setAuthor(revAndAuthor[1]);
	}

	
}
