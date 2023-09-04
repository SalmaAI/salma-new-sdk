package ai.mawdoo3.salma.utils.security;

import java.io.Serializable;


public class FulfilmentEncryptedRequest {
    private String payload;
    private String key;

    public FulfilmentEncryptedRequest(String payload, String key) {
        this.payload = payload;
        this.key = key;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
