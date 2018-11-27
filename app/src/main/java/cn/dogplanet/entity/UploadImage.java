package cn.dogplanet.entity;
public class UploadImage extends Resp {

	private UImage image;

	public UImage getImage() {
		return image;
	}

	public void setImage(UImage image) {
		this.image = image;
	}

	public class UImage {
		private Integer image_id;
		private String image_url;
		private String thumb_url;

		public Integer getImage_id() {
			return image_id;
		}

		public void setImage_id(Integer image_id) {
			this.image_id = image_id;
		}

		public String getImage_url() {
			return image_url;
		}

		public void setImage_url(String image_url) {
			this.image_url = image_url;
		}

		public String getThumb_url() {
			return thumb_url;
		}

		public void setThumb_url(String thumb_url) {
			this.thumb_url = thumb_url;
		}

	}
}
