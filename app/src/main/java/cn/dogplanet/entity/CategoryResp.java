package cn.dogplanet.entity;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import cn.dogplanet.entity.CategoryResp.Categories.Sub_category;/** * 达人商品分类 * editor:ztr * package_name:cn.dogplanet.entity * file_name:CategoryResp.java * date:2016-12-6 */public class CategoryResp extends Resp {	private List<Categories> categories;	public List<Categories> getCategories() {		return categories;	}	public void setCategories(List<Categories> categories) {		this.categories = categories;	}	public class Categories {		private String id;		private String name;		private List<Sub_category> sub_category;		public String getId() {			return id;		}		public void setId(String id) {			this.id = id;		}		public String getName() {			return name;		}		public void setName(String name) {			this.name = name;		}		public List<Sub_category> getSub_category() {			return sub_category;		}		public void setSub_category(List<Sub_category> sub_category) {			this.sub_category = sub_category;		}		public class Sub_category {			private String id;			private String name;			public String getId() {				return id;			}			public void setId(String id) {				this.id = id;			}			public String getName() {				return name;			}			public void setName(String name) {				this.name = name;			}		}	}	/**	 * 获取一级分类数据	 * 	 * @param areas	 */	public static List<String> getProvinceValueData(List<Categories> areas) {		List<String> results = new ArrayList<String>();		for (Categories area : areas) {			results.add(area.getName());		}		return results;	}	/**	 * 获取二级分类的数据	 * 	 * @param areas	 * @return	 */	public static Map<String, List<String>> getAreaValueData(List<Categories> areas) {		Map<String, List<String>> map = new HashMap<String, List<String>>();		for (Categories pArea : areas) {			List<String> cs = new ArrayList<String>();			List<Sub_category> tAreas = pArea.getSub_category();			if (null != tAreas && !tAreas.isEmpty()) {				for (Sub_category c : tAreas) {					cs.add(c.getName());				}			}			map.put(pArea.getName(), cs);		}		return map;	}}