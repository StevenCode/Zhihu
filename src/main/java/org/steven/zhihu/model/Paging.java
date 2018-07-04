package org.steven.zhihu.model;




public class Paging {
    private Integer id;

    private boolean is_end;
    private String next;
    private String previous;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isIs_end() {
        return is_end;
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        return "Paging{" +
                "\nis_end=" + is_end +
                "\n, next='" + next + '\'' +
                "\n, previous='" + previous + '\'' +
                '}';
    }
}
