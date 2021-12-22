package id.jayaantara.ibaca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetResponseModel {
    @SerializedName("message")
    private String message;
    @SerializedName("values")
    private List<DataPaper> values;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataPaper> getValues() {
        return values;
    }

    public void setValues(List<DataPaper> values) {
        this.values = values;
    }
}
