package org.steven.zhihu.model;

import java.util.List;

public class Activities {
    private Paging paging;
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @Override
    public String toString() {
        return "Activities{" +
                "\npaging=" + paging +
                "\n, data=" + data +
                '}';
    }
}
