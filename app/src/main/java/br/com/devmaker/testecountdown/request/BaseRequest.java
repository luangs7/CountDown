package br.com.devmaker.testecountdown.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.com.devmaker.testecountdown.voley.DataResponse;


/**
 * Created by anderson on 05/11/2015.
 */
public class BaseRequest extends DataResponse {
    @Expose
    @SerializedName("envio")
    private Boolean envio;

    @Expose
    @SerializedName("msg")
    private String exception;

    @Expose
    @SerializedName("success")
    private Boolean result;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public boolean success() {
        try {
            if(result)
                return true;
            else
                return false;
        }catch (Exception ex) {
            return true;
        }
    }


}
