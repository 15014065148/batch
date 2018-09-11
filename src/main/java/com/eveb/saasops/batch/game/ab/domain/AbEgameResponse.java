package com.eveb.saasops.batch.game.ab.domain;


import lombok.Data;

import java.util.List;

@Data
public class AbEgameResponse {
    private String error_code;
    private String message;
    private Page page;

    class Page {
        private int count;
        private List<AbEgameLogModel> datas;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<AbEgameLogModel> getDatas() {
            return datas;
        }

        public void setDatas(List<AbEgameLogModel> datas) {
            this.datas = datas;
        }

        public Page(int count, List<AbEgameLogModel> datas) {
            this.count = count;
            this.datas = datas;
        }
    }

    public List<AbEgameLogModel> getPageDatas() {
        return getPage().getDatas();
    }

    public int getPageCount() {
        return getPage().getCount();
    }
}
