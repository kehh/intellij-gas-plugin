package com.michaelsnowden.gas;

/**
 * @author michael.snowden
 */
public enum FileType {
    GS("gs", "server_js");
    private final String extension;
    private final String type;

    FileType(String extension, String type) {
        this.extension = extension;
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }

    public static FileType getByExtension(String extension) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getExtension().equals(extension)) {
                return fileType;
            }
        }
        return null;
    }

    public static FileType getByType(String type) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getType().equals(type)) {
                return fileType;
            }
        }
        return null;
    }
}
