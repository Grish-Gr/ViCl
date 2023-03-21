package com.mter.vicl.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class Response{

    private final String massage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String debugMessage;

    public Response(String massage, String debugMessage) {
        this.massage = massage;
        this.debugMessage = debugMessage;
    }

    public Response(String massage) {
        this.massage = massage;
        this.debugMessage = null;
    }
}
