package ru.jsam.education.service.steps;

import ru.jsam.education.pipeline.Project;

public interface Step {

    boolean validate(Project project);

    void accept(Project project) throws Exception;

    String stemName();
}
