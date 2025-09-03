package com.astrapay.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoteRequestDto {
    @NotBlank(message = "Content must not be empty")
    private String content;
}