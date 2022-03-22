package me.KrazyManJ.KrazyHeads.Core;

public enum HeadCategory {
    ALPHABET("alphabet"),
    ANIMALS("animals"),
    BLOCKS("blocks"),
    DECORATION("decoration"),
    FOOD_DRINKS("food-drinks"),
    HUMANS("humans"),
    HUMANOID("humanoid"),
    MISCELLANEOUS("miscellaneous"),
    MONSTERS("monsters"),
    PLANTS("plants");

    HeadCategory(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String fromId() { return id.toUpperCase().replace("-", "_");}
    private final String id;
}
