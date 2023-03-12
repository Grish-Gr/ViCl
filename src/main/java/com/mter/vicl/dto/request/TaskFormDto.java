package com.mter.vicl.dto.request;

import java.util.Date;

public record TaskFormDto(String title,
                          String description,
                          Date expirationDate,
                          Long classroomID) {
}
