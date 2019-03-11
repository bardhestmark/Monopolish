package com.teamfour.monopolish.gui.View;

/**
 * View constants that contain filenames for .fxml files.
 * References to .fxml files should use an enum from this class,
 * to prevent inconsistency in filenames within the application
 *
 * @author Mikael Kalstad
 * @version 1.0
 */
public enum ViewConstants {
    /**
     * Reference to the file location of the .fxml view files
     */
    FILE_PATH("../View/"),

    /**
     * References to the .fxml view files
     */
    LOGIN("login.fxml"),
    DASHBOARD("dashboard.fxml"),
    REGISTER("register.fxml");

    private String value;

    /**
     * Method that is needed to get the actual value of the enum
     *
     * @return enum value
     */
    public String getValue() {
        return value;
    }

    /**
     * Constructor for the enums.
     *
     * @param value a value the enum holds
     */
    ViewConstants(String value) {
        this.value = value;
    }
}