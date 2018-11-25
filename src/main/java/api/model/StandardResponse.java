package api.model;

import com.google.gson.JsonElement;

public class StandardResponse {

    private String status;

    private String message;

    private JsonElement data;

    public StandardResponse(String status) {
        this.status = status;
    }

    public StandardResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandardResponse(String status, JsonElement jsonElement) {
        this.status = status;
        this.data = jsonElement;
    }
}
