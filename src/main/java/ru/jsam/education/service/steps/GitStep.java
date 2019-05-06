package ru.jsam.education.service.steps;

import org.apache.commons.lang3.StringUtils;
import ru.jsam.education.dto.ExecuteResult;
import ru.jsam.education.pipeline.Project;
import ru.jsam.education.utills.TerminalCommandExecutor;

import java.io.File;
import java.util.stream.Stream;

public class GitStep implements Step {

    private String cloneCommand = "git clone %s";

    private String stepName = "GIT CLONE";

    @Override
    public boolean validate(Project project) {
        return StringUtils.isNotEmpty(project.getSrc()) && StringUtils.isNotEmpty(project.getDst());
    }

    @Override
    public void accept(Project project) throws Exception {
        ExecuteResult execute_result = new TerminalCommandExecutor()
                .execute(
                        String.format(cloneCommand, project.getSrc()),
                        new String[0],
                        new File(project.getDst()));

        String s = Stream.of(new File(project.getDst()).list())
                .findFirst()
                .orElse(null);

        project.executedIssue(
                execute_result.isSuccess(),
                execute_result.getExitCode(),
                execute_result.getResult(),
                new File(project.getDst() + "/" + s)
        );
    }

    @Override
    public String stemName() {
        return stepName;
    }
}