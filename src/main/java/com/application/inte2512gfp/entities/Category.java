package com.application.inte2512gfp.entities;

public enum Category {
    // category
    NEWEST(0, "Newest"),
    BUSINESS(1, "Business"),
    COVID(2, "Covid"),
    ENTERTAINMENT(3, "Entertainment"),
    HEALTH(4, "Health"),
    POLITICS(5, "Politics"),
    SPORTS(6, "Sports"),
    TECHNOLOGY(7, "Technology"),
    WORLD(8, "World"),
    OTHERS(9, "Others");

    private int value = 0;
    private String engName = "";


    // constructor
    Category(int value, String engName) {
        this.value = value;
        this.engName = engName;
    }

    // getter
    public String getEngName() {
        return engName;
    }

    /**
     * find category by string
     * @param text
     * @return category
     */
    public static Category getCategoryFromText(String text) {
        // find category
        for (Category category : Category.values()) {
            if (category.getEngName().equalsIgnoreCase(text))
                return category;
        }
        return null;
    }
}