package com.hw.webcrawler.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Response object to return to the api when there is an error
 *
 * @author Dilip Tharoor
 */
@ApiModel(
        description = "Response when there is an error."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse implements ResponseBase {

    @ApiModelProperty("Response with error string")
    @JsonProperty
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
