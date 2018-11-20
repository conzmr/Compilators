public class Token {
	public String id;
	public String type;
	public Integer initCharacter;
	public Integer line;
	
	public Token(String id, String type, Integer initCharacter, Integer line) {
		super();
		this.id = id;
		this.type = type;
		this.initCharacter = initCharacter;
		this.line = line;
	}
}