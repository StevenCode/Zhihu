package org.steven.zhihu.model;

public class Table {
    private long seq;
    private long id;
    private String verb;
    private String action_text;
    private long created_time;
    private String excerpt;
    private String url;
    private String title;
    private String question_excerpt;
    private long answer_count;


    public Table() {
    }

    public Table(long seq, Data data) {
        this.seq = seq;
        this.id = data.getId();
        this.verb = data.getVerb();
        this.action_text = data.getAction_text();
        this.created_time = data.getCreated_time();
        this.excerpt = data.getTarget().getExcerpt();
        this.url = data.getTarget().getUrl();
        this.title = data.getTarget().getQuestion().getTitle();
        this.question_excerpt = data.getTarget().getQuestion().getExcerpt();
        this.answer_count = data.getTarget().getQuestion().getAnswer_count();
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getAction_text() {
        return action_text;
    }

    public void setAction_text(String action_text) {
        this.action_text = action_text;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion_excerpt() {
        return question_excerpt;
    }

    public void setQuestion_excerpt(String question_excerpt) {
        this.question_excerpt = question_excerpt;
    }

    public long getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(long answer_count) {
        this.answer_count = answer_count;
    }
}
