package cn.dogplanet.entity;

import java.util.List;

import cn.dogplanet.R;

public class Product {

    // category的值 0:全部1:潜水类 2:海上娱乐 3:套票 4:陆地玩乐 6:门船票
    public static final String CATE_ALL = "0";
    public static final String CATE_DIVING = "1";
    public static final String CATE_SEA = "2";
    public static final String CATE_TICKET = "6";
    public static final String CATE_LAND = "4";
    public static final String CATE_OTHER = "3";

    private String pro_id;
    private String name;
    private String url;
    private Integer price;
    private Integer base_price;
    private boolean joinStore;
    private int scenic_id;
    private String scenic_name;
    private boolean is_package;
    private String short_comment;
    private String date;
    // 是否从景区中删除
    private boolean is_delete;
    // 是否需要身份证
    private boolean hasIdCard;

    // 装修商城->客户端业务使用 分类
    private String cate;
    // 装修商城->客户端业务使用
    private String status;

    // 服务器端返回分类
    private String category;
    // 上传产品的达人id 0:平台产品
    private int expert_id;

    private List<String> feature;

    private Boolean recommend;

    public List<String> getFeature() {
        return feature;
    }

    public void setFeature(List<String> feature) {
        this.feature = feature;
    }

    public int getExpert_id() {
        return expert_id;
    }

    public void setExpert_id(int expert_id) {
        this.expert_id = expert_id;
    }

    public boolean isHasIdCard() {
        return hasIdCard;
    }

    public void setHasIdCard(boolean hasIdCard) {
        this.hasIdCard = hasIdCard;
    }

    public boolean isIs_delete() {
        return is_delete;
    }

    public void setIs_delete(boolean is_delete) {
        this.is_delete = is_delete;
    }

    public int getScenic_id() {
        return scenic_id;
    }

    public void setScenic_id(int scenic_id) {
        this.scenic_id = scenic_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShort_comment() {
        return short_comment;
    }

    public void setShort_comment(String short_comment) {
        this.short_comment = short_comment;
    }

    public String getScenic_name() {
        return scenic_name;
    }

    public void setScenic_name(String scenic_name) {
        this.scenic_name = scenic_name;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public boolean isIs_package() {
        return is_package;
    }

    public void setIs_package(boolean is_package) {
        this.is_package = is_package;
    }

    public boolean isJoinStore() {
        return joinStore;
    }

    public void setJoinStore(boolean joinStore) {
        this.joinStore = joinStore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getBase_price() {
        return base_price;
    }

    public void setBase_price(Integer base_price) {
        this.base_price = base_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product [pro_id=" + pro_id + ", name=" + name + ", url="
                + url + ", price=" + price + ", base_price=" + base_price
                + ", joinStore=" + joinStore + ", scenic_id=" + scenic_id
                + ", scenic_name=" + scenic_name + ", is_package="
                + is_package + ", short_comment=" + short_comment
                + ", date=" + date + ", is_delete=" + is_delete
                + ", hasIdCard=" + hasIdCard + ", cate=" + cate
                + ", status=" + status + ", category=" + category
                + ", expert_id=" + expert_id + "]";
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }
}

