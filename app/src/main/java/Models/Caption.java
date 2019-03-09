package Models;


import com.google.gson.annotations.SerializedName;


public class Caption{

	@SerializedName("rendered")
	private String rendered;

	public void setRendered(String rendered){
		this.rendered = rendered;
	}

	public String getRendered(){
		return rendered;
	}

	@Override
 	public String toString(){
		return 
			"Caption{" + 
			"rendered = '" + rendered + '\'' + 
			"}";
		}
}