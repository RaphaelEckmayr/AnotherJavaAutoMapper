package net.AJAM.Mapper;

public enum MappingType {
    STRICT(1), MEDIUM(2), LOOSE(3);

    private final int value;

    MappingType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
