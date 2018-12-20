package cn.dogplanet.entity;

import java.util.List;

public class ProductListResp extends Resp {

    private List<Product> products;

    private List<Product> product;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
