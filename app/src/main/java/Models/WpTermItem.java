package Models;


import com.google.gson.annotations.SerializedName;


public class WpTermItem{

	@SerializedName("taxonomy")
	private String taxonomy;

	@SerializedName("href")
	private String href;

	@SerializedName("embeddable")
	private boolean embeddable;

	public void setTaxonomy(String taxonomy){
		this.taxonomy = taxonomy;
	}

	public String getTaxonomy(){
		return taxonomy;
	}

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
			"WpTermItem{" + 
			"taxonomy = '" + taxonomy + '\'' + 
			",href = '" + href + '\'' + 
			",embeddable = '" + embeddable + '\'' + 
			"}";
		}
}