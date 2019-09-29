package com.pickupapp.infra;

public enum EnumSpaceType {
    SOCIETY("Society"), GRASS("Gramado"), EARTH("Terra"), COURT ("Quadra");
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
