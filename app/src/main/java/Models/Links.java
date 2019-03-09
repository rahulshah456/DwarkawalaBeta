package Models;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Links{

	@SerializedName("curies")
	private List<CuriesItem> curies;

	@SerializedName("replies")
	private List<RepliesItem> replies;

	@SerializedName("version-history")
	private List<VersionHistoryItem> versionHistory;

	@SerializedName("author")
	private List<AuthorItem> author;

	@SerializedName("wp:term")
	private List<WpTermItem> wpTerm;

	@SerializedName("about")
	private List<AboutItem> about;

	@SerializedName("self")
	private List<SelfItem> self;

	@SerializedName("wp:featuredmedia")
	private List<WpFeaturedmediaItem> wpFeaturedmedia;

	@SerializedName("collection")
	private List<CollectionItem> collection;

	@SerializedName("wp:attachment")
	private List<WpAttachmentItem> wpAttachment;

	public void setCuries(List<CuriesItem> curies){
		this.curies = curies;
	}

	public List<CuriesItem> getCuries(){
		return curies;
	}

	public void setReplies(List<RepliesItem> replies){
		this.replies = replies;
	}

	public List<RepliesItem> getReplies(){
		return replies;
	}

	public void setVersionHistory(List<VersionHistoryItem> versionHistory){
		this.versionHistory = versionHistory;
	}

	public List<VersionHistoryItem> getVersionHistory(){
		return versionHistory;
	}

	public void setAuthor(List<AuthorItem> author){
		this.author = author;
	}

	public List<AuthorItem> getAuthor(){
		return author;
	}

	public void setWpTerm(List<WpTermItem> wpTerm){
		this.wpTerm = wpTerm;
	}

	public List<WpTermItem> getWpTerm(){
		return wpTerm;
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

	public void setWpFeaturedmedia(List<WpFeaturedmediaItem> wpFeaturedmedia){
		this.wpFeaturedmedia = wpFeaturedmedia;
	}

	public List<WpFeaturedmediaItem> getWpFeaturedmedia(){
		return wpFeaturedmedia;
	}

	public void setCollection(List<CollectionItem> collection){
		this.collection = collection;
	}

	public List<CollectionItem> getCollection(){
		return collection;
	}

	public void setWpAttachment(List<WpAttachmentItem> wpAttachment){
		this.wpAttachment = wpAttachment;
	}

	public List<WpAttachmentItem> getWpAttachment(){
		return wpAttachment;
	}

	@Override
 	public String toString(){
		return 
			"Links{" + 
			"curies = '" + curies + '\'' + 
			",replies = '" + replies + '\'' + 
			",version-history = '" + versionHistory + '\'' + 
			",author = '" + author + '\'' + 
			",wp:term = '" + wpTerm + '\'' + 
			",about = '" + about + '\'' + 
			",self = '" + self + '\'' + 
			",wp:featuredmedia = '" + wpFeaturedmedia + '\'' + 
			",collection = '" + collection + '\'' + 
			",wp:attachment = '" + wpAttachment + '\'' + 
			"}";
		}
}