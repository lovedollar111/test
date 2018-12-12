package cn.dogplanet.entity;

import java.util.List;

public class HomeResp extends Resp {


    private List<Banner> banner;

    private List<Product> recommendProduct;

    private List<Product> latestProduct;

    private List<Product> hotProduct;

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }


    public List<Product> getRecommendProduct() {
        return recommendProduct;
    }

    public void setRecommendProduct(List<Product> recommendProduct) {
        this.recommendProduct = recommendProduct;
    }

    public List<Product> getLatestProduct() {
        return latestProduct;
    }

    public void setLatestProduct(List<Product> latestProduct) {
        this.latestProduct = latestProduct;
    }

    public List<Product> getHotProduct() {
        return hotProduct;
    }

    public void setHotProduct(List<Product> hotProduct) {
        this.hotProduct = hotProduct;
    }

    public class Banner {
        private String url;
        private String image_url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

    }
}
