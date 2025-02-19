package com.toyland.user.infrastructure;

public enum UserSortEnum {
    CREATED_AT("createdAt"),
    USER_NAME("username");

    private final String property;

    UserSortEnum(String property) { this.property = property;}

    public String getProperty() { return property; }

    public static UserSortEnum findByProperty(String property) {
        for (UserSortEnum sortProperty : values()) {
            if (sortProperty.getProperty().equals(property)) {
                return sortProperty;
            }
        }
        return null;
    }

}
