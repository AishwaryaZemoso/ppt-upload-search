package com.example.ppt.dto;

public class UploadResponse {

    private String message;
    private int fileId;

    public UploadResponse(String message, int fileId) {
        this.message = message;
        this.fileId = fileId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
