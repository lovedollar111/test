package cn.dogplanet.entity;

import java.util.List;

/**
 * Created by zhangtianrui on 17/5/25.
 */
public class CardTypeResp extends Resp {

    private List<CardTypeEntity> cardArr;

    public List<CardTypeEntity> getCardTypeEntities() {
        return cardArr;
    }

    public void setCardTypeEntities(List<CardTypeEntity> cardTypeEntities) {
        this.cardArr = cardTypeEntities;
    }

    public class CardTypeEntity{
        private String id;
        private String name;

        public CardTypeEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
