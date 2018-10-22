package cn.dogplanet.constant;

import java.util.ArrayList;
import java.util.List;

import cn.dogplanet.app.util.GsonHelper;
import cn.dogplanet.app.util.SPUtils;
import cn.dogplanet.app.util.StringUtils;
import cn.dogplanet.entity.Area;
import cn.dogplanet.entity.AreaListResp;
import cn.dogplanet.entity.CardTypeResp;
import cn.dogplanet.entity.CategoryResp.Categories;
import cn.dogplanet.entity.CategoryResp.Categories.Sub_category;
import cn.dogplanet.entity.Expert;
import cn.dogplanet.entity.Travel;

/**
 * 缓存
 * editor:ztr
 * package_name:cn.dogplanet.constant
 * file_name:WCache.java
 * date:2016-12-6
 */
public class WCache {
    // 缓存省市区数据
    public static List<Area.Place> CACHE_AREAS = new ArrayList<>();
    // 缓存证件列表
    public static List<CardTypeResp.CardTypeEntity> CACHE_CARDS=new ArrayList<>();

    // 缓存区号数据
    public static List<AreaListResp.AreaEntity> CACHE_AREA_LIST=new ArrayList<>();

    public static List<Categories> CACHE_CATEGORIES=new ArrayList<>();

    public static List<Travel> CAHCE_TRAVEL=new ArrayList<>();
    /**
     * 获取缓存的用户数据
     *
     * @return
     */
    public static Expert getCacheExpert() {
        String expert_str = (String) SPUtils.get(WConstant.EXPERT_DATA, "");
        Expert expert = GsonHelper.parseObject(expert_str, Expert.class);
        return expert;
    }


    public static List<AreaListResp.AreaEntity> getAreaEntity(){
        return CACHE_AREA_LIST;
    }

    public static String getShowCategory(String cateId,String sub_cateId){
        if(StringUtils.isBlank(cateId)){
            return "";
        }
        ArrayList<String> cates = new ArrayList<>();
        for(Categories category:CACHE_CATEGORIES){
            if(category.getId().equals(cateId)){
                cates.add(category.getName());
                List<Sub_category> sub_category = category.getSub_category();
                if(sub_category!=null){
                    for(Sub_category sub_category2:sub_category){
                        if(sub_category2.getId().equals(sub_cateId)){
                            cates.add(sub_category2.getName());
                            break;
                        }
                    }
                }
            }
        }
        return StringUtils.join(cates.toArray(), ".");

    }

    public static String getCardName(String card_id){
        if(StringUtils.isBlank(card_id)){
            return "";
        }
        for(CardTypeResp.CardTypeEntity entity:CACHE_CARDS){
            if(card_id.equals(entity.getId())){
                return entity.getName();
            }
        }
        return "";
    }

    public static String getShowArea(String country_id, String province_id,
                                     String city_id) {
        if (StringUtils.isBlank(country_id)) {
            return "";
        }
        ArrayList<String> address = new ArrayList<>();
        for (Area.Place area : CACHE_AREAS) {
            if (area.getArea_id().equals(country_id)) {
                address.add(area.getArea_name());
                List<Area.Place.Province> provinces = area.getProvinces();
                if(provinces!=null){
                    for (Area.Place.Province province : provinces) {
                        if (province.getProvince_id().equals(province_id)) {
                            address.add(province.getProvince_name());
                            if(province.getCities()!=null&&province.getCities().size()>0){
                                for(Area.Place.City city:province.getCities()){
                                    if(city.getCity_id().equals(city_id)){
                                        address.add(city.getCity_name());
                                        break;
                                    }
                                }
                            }else {
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }
        return StringUtils.join(address.toArray(), "，");

    }

    public static String getShowArea(String province_id, String city_id) {
        if (StringUtils.isBlank(province_id)) {
            return "";
        }
        ArrayList<String> address = new ArrayList<>();
        for (Area.Place area : CACHE_AREAS) {
            if (area.getArea_id().equals(province_id)) {
                address.add(area.getArea_name());
                List<Area.Place.Province> citys = area.getProvinces();
                if(citys!=null){
                    for (Area.Place.Province city : citys) {
                        if (city.getProvince_id().equals(city_id)) {
                            address.add(city.getProvince_name());
                        }
                    }
                }
                break;
            }
        }
        return StringUtils.join(address.toArray(), "，");
    }

    public static List<Travel> getCahceTravel(){
        return CAHCE_TRAVEL;
    }

    public static void setCahceTravel(List<Travel> travels){
        CAHCE_TRAVEL=travels;
    }
}
