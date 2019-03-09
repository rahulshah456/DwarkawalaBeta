package Models;


import com.google.gson.annotations.SerializedName;


public class WpFeaturedmediaItem{

	@SerializedName("href")
	private String href;

	@SerializedName("embeddable")
	private boolean embeddable;

	public void setHref(String href){
		this.href = href;
	}

	public String getHref(){
		return href;
	}

	public void setEmbeddable(boolean embeddable){
		this.embeddable = embeddable;
	}

	public boolean isEmbeddable(){
		return embeddable;
	}

	@Override
 	public String toString(){
		return 
			"WpFeaturedmediaItem{" + 
			"href = '" + href + '\'' + 
			",embeddable = '" + embeddable + '\'' + 
			"}";
		}
}