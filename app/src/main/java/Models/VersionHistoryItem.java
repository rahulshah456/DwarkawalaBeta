package Models;


import com.google.gson.annotations.SerializedName;


public class VersionHistoryItem{

	@SerializedName("href")
	private String href;

	public void setHref(String href){
		this.href = href;
	}

	public String getHref(){
		return href;
	}

	@Override
 	public String toString(){
		return 
			"VersionHistoryItem{" + 
			"href = '" + href + '\'' + 
			"}";
		}
}