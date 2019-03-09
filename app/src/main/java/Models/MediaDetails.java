package Models;


import com.google.gson.annotations.SerializedName;


public class MediaDetails{

	@SerializedName("file")
	private String file;

	@SerializedName("sizes")
	private Sizes sizes;

	@SerializedName("image_meta")
	private ImageMeta imageMeta;

	@SerializedName("width")
	private int width;

	@SerializedName("height")
	private int height;

	public void setFile(String file){
		this.file = file;
	}

	public String getFile(){
		return file;
	}

	public void setSizes(Sizes sizes){
		this.sizes = sizes;
	}

	public Sizes getSizes(){
		return sizes;
	}

	public void setImageMeta(ImageMeta imageMeta){
		this.imageMeta = imageMeta;
	}

	public ImageMeta getImageMeta(){
		return imageMeta;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getWidth(){
		return width;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getHeight(){
		return height;
	}

	@Override
 	public String toString(){
		return 
			"MediaDetails{" + 
			"file = '" + file + '\'' + 
			",sizes = '" + sizes + '\'' + 
			",image_meta = '" + imageMeta + '\'' + 
			",width = '" + width + '\'' + 
			",height = '" + height + '\'' + 
			"}";
		}
}