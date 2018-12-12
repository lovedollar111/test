package cn.dogplanet.entity;

public class ProductResp extends Resp {

    private ProductDetail product;

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
    }
}
