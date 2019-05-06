package ru.jsam.education.service.steps;

import org.apache.commons.collections4.CollectionUtils;
import ru.jsam.education.dto.ExecuteResult;
import ru.jsam.education.pipeline.Project;
import ru.jsam.education.utills.FindUtils;
import ru.jsam.education.utills.TerminalCommandExecutor;

import java.io.File;
import java.util.Optional;

public class CompileStep implements Step {

    private String stemName = "COMPILE";

    private FindUtils find = new FindUtils();
    private TerminalCommandExecutor terminalExecutor = new TerminalCommandExecutor();

    private String compileCommand = "javac -d %s %s";
    private String[] find_args = {"-name", "*.java"};

    @Override
    public boolean validate(Project project) {
        return project.getProjectFile() != null
                && project.getProjectFile().exists();
    }

    @Override
    public void accept(Project project) throws Exception {
        String classess =
                Optional.ofNullable(find.find(project.getProjectFile().getAbsoluteFile(), "*.java", find_args, true))
                        .filter(CollectionUtils::isNotEmpty)
                        .map(list -> list.stream()
                                .reduce(" ", (a, b) -> a + " " + b))
                        .orElse(null);

        File targetDir = new File(project.getProjectFile().getAbsoluteFile() + "/target");
        targetDir.mkdir();

        String javac_command = String.format(
                compileCommand,
                targetDir.getAbsolutePath(),
                classess);

        ExecuteResult execute_result = terminalExecutor.execute(
                javac_command,
                new String[0],
                project.getProjectFile());

        project.executedIssue(execute_result, null);
    }

    @Override
    public String stemName() {
        return stemName;
    }
}
