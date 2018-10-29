package cn.dogplanet.entity;


/**
 * 分享用到的数据
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:ShareData.java
 * date:2016-12-6
 */
public class ShareData {
	// 分享标题
	private String title;
	// 分享图片
	private String pic;
	// 分享文字内容
	private String content;
	// 分享用到的链接
	private String url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
