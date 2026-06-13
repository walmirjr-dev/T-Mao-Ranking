package com.walmir.tmaoranking.dto.request;

public record UserRequest(
        String name,
        String email,
        String password
) {}