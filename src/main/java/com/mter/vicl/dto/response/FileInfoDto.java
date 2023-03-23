package com.mter.vicl.dto.response;

import java.time.LocalDate;

public record FileInfoDto(Long fileID, String name, Long size, String key, LocalDate uploadDate) {
}
