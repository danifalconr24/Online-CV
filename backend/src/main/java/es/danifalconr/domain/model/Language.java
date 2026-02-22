package es.danifalconr.domain.model;

public record Language(Long id, String name, Level level) {

    public enum Level {
        LOW,
        MEDIUM,
        HIGH,
        NATIVE
    }
}
