package Models;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ImageMeta{

	@SerializedName("aperture")
	private String aperture;

	@SerializedName("copyright")
	private String copyright;

	@SerializedName("orientation")
	private String orientation;

	@SerializedName("focal_length")
	private String focalLength;

	@SerializedName("iso")
	private String iso;

	@SerializedName("keywords")
	private List<Object> keywords;

	@SerializedName("caption")
	private String caption;

	@SerializedName("created_timestamp")
	private String createdTimestamp;

	@SerializedName("credit")
	private String credit;

	@SerializedName("camera")
	private String camera;

	@SerializedName("title")
	private String title;

	@SerializedName("shutter_speed")
	private String shutterSpeed;

	public void setAperture(String aperture){
		this.aperture = aperture;
	}

	public String getAperture(){
		return aperture;
	}

	public void setCopyright(String copyright){
		this.copyright = copyright;
	}

	public String getCopyright(){
		return copyright;
	}

	public void setOrientation(String orientation){
		this.orientation = orientation;
	}

	public String getOrientation(){
		return orientation;
	}

	public void setFocalLength(String focalLength){
		this.focalLength = focalLength;
	}

	public String getFocalLength(){
		return focalLength;
	}

	public void setIso(String iso){
		this.iso = iso;
	}

	public String getIso(){
		return iso;
	}

	public void setKeywords(List<Object> keywords){
		this.keywords = keywords;
	}

	public List<Object> getKeywords(){
		return keywords;
	}

	public void setCaption(String caption){
		this.caption = caption;
	}

	public String getCaption(){
		return caption;
	}

	public void setCreatedTimestamp(String createdTimestamp){
		this.createdTimestamp = createdTimestamp;
	}

	public String getCreatedTimestamp(){
		return createdTimestamp;
	}

	public void setCredit(String credit){
		this.credit = credit;
	}

	public String getCredit(){
		return credit;
	}

	public void setCamera(String camera){
		this.camera = camera;
	}

	public String getCamera(){
		return camera;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setShutterSpeed(String shutterSpeed){
		this.shutterSpeed = shutterSpeed;
	}

	public String getShutterSpeed(){
		return shutterSpeed;
	}

	@Override
 	public String toString(){
		return 
			"ImageMeta{" + 
			"aperture = '" + aperture + '\'' + 
			",copyright = '" + copyright + '\'' + 
			",orientation = '" + orientation + '\'' + 
			",focal_length = '" + focalLength + '\'' + 
			",iso = '" + iso + '\'' + 
			",keywords = '" + keywords + '\'' + 
			",caption = '" + caption + '\'' + 
			",created_timestamp = '" + createdTimestamp + '\'' + 
			",credit = '" + credit + '\'' + 
			",camera = '" + camera + '\'' + 
			",title = '" + title + '\'' + 
			",shutter_speed = '" + shutterSpeed + '\'' + 
			"}";
		}
}