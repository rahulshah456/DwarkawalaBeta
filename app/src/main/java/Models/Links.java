package Models;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Links{

	@SerializedName("replies")
	private List<RepliesItem> replies;

	@SerializedName("author")
	private List<AuthorItem> author;

	@SerializedName("about")
	private List<AboutItem> about;

	@SerializedName("self")
	private List<SelfItem> self;

	@SerializedName("collection")
	private List<CollectionItem> collection;

	@SerializedName("wp:featuredmedia")
	private List<FeatureMedia> featuredmedia;

	public List<FeatureMedia> getFeaturedmedia() {
		return featuredmedia;
	}

	public void setFeaturedmedia(List<FeatureMedia> featuredmedia) {
		this.featuredmedia = featuredmedia;
	}

	public void setReplies(List<RepliesItem> replies){
		this.replies = replies;
	}

	public List<RepliesItem> getReplies(){
		return replies;
	}

	public void setAuthor(List<AuthorItem> author){
		this.author = author;
	}

	public List<AuthorItem> getAuthor(){
		return author;
	}

	public void setAbout(List<AboutItem> about){
		this.about = about;
	}

	public List<AboutItem> getAbout(){
		return about;
	}

	public void setSelf(List<SelfItem> self){
		this.self = self;
	}

	public List<SelfItem> getSelf(){
		return self;
	}

	public void setCollection(List<CollectionItem> collection){
		this.collection = collection;
	}

	public List<CollectionItem> getCollection(){
		return collection;
	}

	@Override
 	public String toString(){
		return 
			"Links{" + 
			"replies = '" + replies + '\'' + 
			",author = '" + author + '\'' + 
			",about = '" + about + '\'' + 
			",self = '" + self + '\'' +
					",FeturedMedia = '" + featuredmedia + '\'' +
					",collection = '" + collection + '\'' +
			"}";
		}
}