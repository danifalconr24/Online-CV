package es.danifalconr.domain.model;

public class Language {

    private Long id;
    private String name;
    private Level level;

    public enum Level {
        LOW,
        MEDIUM,
        HIGH,
        NATIVE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
