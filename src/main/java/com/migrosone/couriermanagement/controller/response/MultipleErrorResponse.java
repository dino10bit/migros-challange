package com.migrosone.couriermanagement.controller.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class MultipleErrorResponse {

    @Setter(AccessLevel.NONE)
    private List<String> messages = new ArrayList<>();
}
