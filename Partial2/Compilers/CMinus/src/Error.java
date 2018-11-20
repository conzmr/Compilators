public class Error {
	public String id;
	public Character error;
	public Integer position;
	public Integer line;
	
	public Error(String id, Character error, Integer position, Integer line) {
		super();
		this.id = id;
		this.error = error;
		this.position = position;
		this.line = line;
	}
}
