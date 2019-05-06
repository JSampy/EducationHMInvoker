package ru.jsam.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jsam.education.service.steps.Steps;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private String src;
    private String dst;

    private List<Steps> commands;

    private String callBackUrl;
    private String userName;
    private String password;

}
