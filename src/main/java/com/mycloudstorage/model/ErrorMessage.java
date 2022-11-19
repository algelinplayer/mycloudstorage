package com.mycloudstorage.model;

public enum ErrorMessage {
    UNKNOWN(0, ""),
    MAX_FILESIZE_EXCEEDED(1, "File max size was exceeded."),
    FILENAME_ALREADY_EXISTS(2, "File name already exists."),
    PAGE_NOT_FOUND(3, "Page not found."),
    NO_FILE(4, "No file informed.");

    int code;
    String messageDescription;

    ErrorMessage(int code, String messageDescription) {
        this.code = code;
        this.messageDescription = messageDescription;
    }

    public int getCode() {
        return code;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

}
