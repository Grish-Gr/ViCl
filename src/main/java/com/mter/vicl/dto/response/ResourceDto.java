package com.mter.vicl.dto.response;

import com.mter.vicl.entities.FileInfo;
import org.springframework.core.io.Resource;

public record ResourceDto(Resource resource, FileInfo fileInfo) {
}
