
package com.example.lab4_getimageinflickr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Title {

    @SerializedName("_content")
    @Expose
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
