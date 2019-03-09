package Models;


import com.google.gson.annotations.SerializedName;


public class AboutItem{

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
			"AboutItem{" + 
			"href = '" + href + '\'' + 
			"}";
		}
}