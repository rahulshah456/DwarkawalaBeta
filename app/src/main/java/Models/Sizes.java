package Models;


import com.google.gson.annotations.SerializedName;


public class Sizes{

	@SerializedName("list_mag_wp_thumbnail_blog_list")
	private ListMagWpThumbnailBlogList listMagWpThumbnailBlogList;

	@SerializedName("thumbnail")
	private Thumbnail thumbnail;

	@SerializedName("medium")
	private Medium medium;

	@SerializedName("list_mag_wp_thumbnail_blog_grid")
	private ListMagWpThumbnailBlogGrid listMagWpThumbnailBlogGrid;

	@SerializedName("list_mag_wp_thumbnail_widget_small")
	private ListMagWpThumbnailWidgetSmall listMagWpThumbnailWidgetSmall;

	@SerializedName("full")
	private Full full;

	public void setListMagWpThumbnailBlogList(ListMagWpThumbnailBlogList listMagWpThumbnailBlogList){
		this.listMagWpThumbnailBlogList = listMagWpThumbnailBlogList;
	}

	public ListMagWpThumbnailBlogList getListMagWpThumbnailBlogList(){
		return listMagWpThumbnailBlogList;
	}

	public void setThumbnail(Thumbnail thumbnail){
		this.thumbnail = thumbnail;
	}

	public Thumbnail getThumbnail(){
		return thumbnail;
	}

	public void setMedium(Medium medium){
		this.medium = medium;
	}

	public Medium getMedium(){
		return medium;
	}

	public void setListMagWpThumbnailBlogGrid(ListMagWpThumbnailBlogGrid listMagWpThumbnailBlogGrid){
		this.listMagWpThumbnailBlogGrid = listMagWpThumbnailBlogGrid;
	}

	public ListMagWpThumbnailBlogGrid getListMagWpThumbnailBlogGrid(){
		return listMagWpThumbnailBlogGrid;
	}

	public void setListMagWpThumbnailWidgetSmall(ListMagWpThumbnailWidgetSmall listMagWpThumbnailWidgetSmall){
		this.listMagWpThumbnailWidgetSmall = listMagWpThumbnailWidgetSmall;
	}

	public ListMagWpThumbnailWidgetSmall getListMagWpThumbnailWidgetSmall(){
		return listMagWpThumbnailWidgetSmall;
	}

	public void setFull(Full full){
		this.full = full;
	}

	public Full getFull(){
		return full;
	}

	@Override
 	public String toString(){
		return 
			"Sizes{" + 
			"list_mag_wp_thumbnail_blog_list = '" + listMagWpThumbnailBlogList + '\'' + 
			",thumbnail = '" + thumbnail + '\'' + 
			",medium = '" + medium + '\'' + 
			",list_mag_wp_thumbnail_blog_grid = '" + listMagWpThumbnailBlogGrid + '\'' + 
			",list_mag_wp_thumbnail_widget_small = '" + listMagWpThumbnailWidgetSmall + '\'' + 
			",full = '" + full + '\'' + 
			"}";
		}
}