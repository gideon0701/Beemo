
package com.pentagon.beemo.Model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ContactNumberModel {

    @SerializedName("Numbers")
    private List<String> mNumbers;

    public List<String> getNumbers() {
        return mNumbers;
    }

    public void setNumbers(List<String> numbers) {
        mNumbers = numbers;
    }

}
