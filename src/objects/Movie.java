package objects;

public class Movie {
	
	private int ID;
	private String title;
	private int FSK;
	private int length;
	private String location;
	private String series;
	private String category;
	private String description;
	
	
	

	public Movie(int ID, String title, int FSK, int length, String loc, String series, String category, String description){
		this.ID = ID;
		this.title = title;
		this.FSK = FSK;
		this.length = length;
		this.location = loc;
		this.series = series;
		this.category = category;
		this.description = description;
	}




	public int getID() {
		return ID;
	}




	public void setID(int iD) {
		ID = iD;
	}




	public String getTitle() {
		return title;
	}




	public void setTitle(String title) {
		this.title = title;
	}




	public int getFSK() {
		return FSK;
	}




	public void setFSK(int fSK) {
		FSK = fSK;
	}




	public int getLength() {
		return length;
	}




	public void setLength(int length) {
		this.length = length;
	}




	public String getLocation() {
		return location;
	}




	public void setLocation(String location) {
		this.location = location;
	}




	public String getSeries() {
		return series;
	}




	public void setSeries(String series) {
		this.series = series;
	}




	public String getCategory() {
		return category;
	}




	public void setCategory(String category) {
		this.category = category;
	}

	
	public String getDescription(){
		return description;
	}
	
	
	public void setDescription(String description){
		this.description = description;
	}
	
}
