package com.pickupapp.infra;

public enum EnumSpaceType {
    SOCIETY("1"), GRASS("2"), EARTH("3"), COURT ("4");
    private final String description;

    EnumSpaceType(String description){
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
