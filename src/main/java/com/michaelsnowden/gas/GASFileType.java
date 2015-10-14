package com.michaelsnowden.gas;

/**
 * @author michael.snowden
 */
public enum GASFileType {
    GS("gs", "server_js");
    private final String extension;
    private final String type;

    GASFileType(String extension, String type) {
        this.extension = extension;
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }

    public static GASFileType getByExtension(String extension) {
        for (GASFileType fileType : GASFileType.values()) {
            if (fileType.getExtension().equals(extension)) {
                return fileType;
            }
        }
        return null;
    }

    public static GASFileType getByType(String type) {
        for (GASFileType fileType : GASFileType.values()) {
            if (fileType.getType().equals(type)) {
                return fileType;
            }
        }
        return null;
    }
}
