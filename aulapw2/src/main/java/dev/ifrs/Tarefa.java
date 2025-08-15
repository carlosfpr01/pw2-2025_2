package dev.ifrs;

public class Tarefa {

    private static int countId = 0;
    private int id = 0;
    private String name;
    private String content;

    public Tarefa(String name, String content) {
        this.id = countId++;
        this.name = name;
        this.content = content;
    }


    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
