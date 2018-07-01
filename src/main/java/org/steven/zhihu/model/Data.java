package org.steven.zhihu.model;

public class Data {
    private String verb;
    private Target target;
    private String action_text;
    private long created_time;
    private long id;

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Data{" +
                "\nverb='" + verb + '\'' +
                "\n, target=" + target +
                "\n, action_text='" + action_text + '\'' +
                "\n, created_time=" + created_time +
                "\n, id=" + id +
                '}';
    }
}
