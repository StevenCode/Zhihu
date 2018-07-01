package org.steven.zhihu.model;

public class Question {
    private String title;
    private String excerpt;
    private long answer_count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public long getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(long answer_count) {
        this.answer_count = answer_count;
    }



    @Override
    public String toString() {
        return "Question{" +
                "\ntitle='" + title + '\'' +
                "\n, excerpt='" + excerpt + '\'' +
                "\n, answer_count=" + answer_count +
                '}';
    }
}
