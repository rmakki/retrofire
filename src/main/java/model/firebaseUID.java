package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by raniamakki on 3/31/18.
 */

// POJO used to retrieve the UID returned by firebase for POST requests
// sample json return from firebase {"name":"-L8sG9m_KP3m9ttEmua4"}

public class FirebaseUID {

    @SerializedName("name")
    private String name;

    public FirebaseUID() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
