package org.steven.zhihu.model;

public class Target {
    private String excerpt;
    private String url;
    private Question question;

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Target{" +
                "\nexcerpt='" + excerpt + '\'' +
                "\n, url='" + url + '\'' +
                "\n, question=" + question +
                '}';
    }
}
