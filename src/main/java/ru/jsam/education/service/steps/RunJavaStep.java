package ru.jsam.education.service.steps;

import ru.jsam.education.dto.Data;
import ru.jsam.education.dto.ExecuteResult;
import ru.jsam.education.pipeline.Project;
import ru.jsam.education.utills.TerminalCommandExecutor;

import java.io.File;
import java.io.IOException;

public class RunJavaStep extends AbstractRunStep {

    private TerminalCommandExecutor terminal;

    public RunJavaStep() {
        this.terminal = new TerminalCommandExecutor();
    }

    private String stepName = "RUN";
    private String runCommand = "java -jar Project.jar";

    @Override
    ExecuteResult run(Project project, Data data) throws IOException {
        return terminal.execute(
                getRunCommand(
                        project,
                        data.getInput().stream().reduce("", (a, b) -> a + " " + b)),
                new String[0],
                new File(project.getProjectFile().getParent()));
    }

    @Override
    public String stemName() {
        return stepName;
    }

    private String getRunCommand(Project project, String data) {
        String result = runCommand;

        if (project.isIntputDataExists()) {
            if (project.isInputDataInSystemIn()) {
                result = String.format("echo %s | " + result + " -", data);
            } else {
                result += " " + data;
            }
        }

        return result;
    }

}