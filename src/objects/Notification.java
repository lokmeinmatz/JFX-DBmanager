package objects;

public class Notification {
	private String title;
	private String description;
	private Boolean isError;
	public Notification(String title, String description, Boolean isError) {
		
		this.title = title;
		this.description = description;
		this.isError = isError;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsError() {
		return isError;
	}
	public void setIsError(Boolean isError) {
		this.isError = isError;
	}
	
	
	
}
