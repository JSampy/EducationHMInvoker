package ru.jsam.education.pipeline;

import ru.jsam.education.service.steps.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Pipeline {

    private List<Step> steps;

    public Pipeline() {
        steps = new ArrayList<>();
    }

    public void addStep(Step step) {
        if (step != null) {
            steps.add(step);
        }
    }

    public void addSteps(Step... step) {
        Stream.of(step)
                .forEach(this::addStep);
    }

    public Project run(Project project) {
        for (int i = 0; i < steps.size() /*&& project.isSuccess()*/; i++) {
            Step s = steps.get(i);

            project.addLog(s.stemName());
            try {
                boolean validate = s.validate(project);
                if (validate) {
                    s.accept(project);
                } else {
                    project.errorIssue("validate error : " + project.toString());
                }
            } catch (Exception e) {
                project.errorIssue(e);
            }
        }
        return project;
    }

}