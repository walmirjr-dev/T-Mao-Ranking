package com.walmir.tmaoranking.dto.request;

public record AuthRequest(
        String email,
        String password
) {}