package id.jayaantara.ibaca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostResponseModel {
    @SerializedName("message")
    private String message;
    @SerializedName("values")
    private DataPaper values;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataPaper getValues() {
        return values;
    }

    public void setValues(DataPaper values) {
        this.values = values;
    }
}
