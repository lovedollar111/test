package cn.dogplanet.entity;

/**
 * 旅行社
 * editor:ztr
 * package_name:cn.dogplanet.entity
 * file_name:Travel.java
 * date:2016-12-6
 */
public class Travel {
	private Integer id;
	private String name;
	private String type;
	private String logo;
	private String introduce;
	private String head_job;
	private String head_tel;
	private String area_code;
	private String head;//负责人姓名
	private String area_id;
	private String is_recommend;
	private String create_time;
	private String logoUrl;
	private String expertCount;
	private String activeExpertCount;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getHead_job() {
		return head_job;
	}

	public void setHead_job(String head_job) {
		this.head_job = head_job;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getIs_recommend() {
		return is_recommend;
	}

	public void setIs_recommend(String is_recommend) {
		this.is_recommend = is_recommend;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getExpertCount() {
		return expertCount;
	}

	public void setExpertCount(String expertCount) {
		this.expertCount = expertCount;
	}

	public String getActiveExpertCount() {
		return activeExpertCount;
	}

	public void setActiveExpertCount(String activeExpertCount) {
		this.activeExpertCount = activeExpertCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Travel [id=" + id + ", name=" + name + "]";
	}


	public String getHead_tel() {
		return head_tel;
	}

	public void setHead_tel(String head_tel) {
		this.head_tel = head_tel;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
}
