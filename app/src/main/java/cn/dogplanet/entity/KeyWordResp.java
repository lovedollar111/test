package cn.dogplanet.entity;

import java.util.List;

public class KeyWordResp extends Resp {

    private List<KeywordsList> keywordsList;

    public List<KeywordsList> getKeywordsList() {
        return keywordsList;
    }

    public void setKeywordsList(List<KeywordsList> keywordsList) {
        this.keywordsList = keywordsList;
    }

    public class KeywordsList{
        private String keywords;

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }
    }
}
