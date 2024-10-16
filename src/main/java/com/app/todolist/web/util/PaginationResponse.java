package com.app.todolist.web.util;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginationResponse<T> {
    private final List<T> contents;
    private final long totalElements;
    private final int totalPages;

    private PaginationResponse(List<T> contents, long size, long totalElements) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }

    public static <T> PaginationResponse<T> of(List<T> contents, long size, long totalElements) {
        return new PaginationResponse<>(contents, size, totalElements);
    }
}
