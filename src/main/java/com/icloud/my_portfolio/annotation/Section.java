package com.icloud.my_portfolio.annotation;

public enum Section {
    HOME("Home"),
    POST("Post"),
    CATEGORY("Category");

    private String value;

    Section(String vluae) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
