package ru.jsam.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private List<String> result;
    private boolean success;
    private String exceptionClassName;
    private String exceptionMessage;
}
