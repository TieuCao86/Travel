package com.example.travel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PhuongTienMapper {
    @Named("splitStringToList")
    default List<String> splitStringToList(String value) {
        if (value == null || value.isEmpty()) return Collections.emptyList();
        return Arrays.asList(value.split(",\\s*"));
    }
}

