package cn.dogplanet.entity;

import java.util.List;

public class AreaListResp extends Resp {

    private List<AreaEntity> area;

    public List<AreaEntity> getArea() {
        return area;
    }

    public void setArea(List<AreaEntity> area) {
        this.area = area;
    }


    public class AreaEntity {
        private String id;
        private String area_name;
        private String area_code;
        private String area_image;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getArea_code() {
            return area_code;
        }

        public void setArea_code(String area_code) {
            this.area_code = area_code;
        }

        public String getArea_image() {
            return area_image;
        }

        public void setArea_image(String area_image) {
            this.area_image = area_image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
