package ru.jsam.education.service.steps;

import org.apache.commons.collections4.CollectionUtils;
import ru.jsam.education.dto.ExecuteResult;
import ru.jsam.education.pipeline.Project;
import ru.jsam.education.utills.FindUtils;
import ru.jsam.education.utills.TerminalCommandExecutor;

import java.io.File;
import java.util.Optional;

public class BuildSimpleStep implements Step {

    private TerminalCommandExecutor terminal;
    private FindUtils find;

    private String stemName = "BUILD:simple";
    private String project_jar_name = "Project.jar";
    private String buildCommand = "jar cvfm %s %s %s";

    public BuildSimpleStep() {
        terminal = new TerminalCommandExecutor();
        find = new FindUtils();
    }


    @Override
    public boolean validate(Project project) {
        return project.getProjectFile() != null && project.getProjectFile().exists();
    }

    @Override
    public void accept(Project project) throws Exception {
        String classes = Optional
                .ofNullable(find.findClasses(project.getProjectFile(), "*.class", new String[0], false))
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> list
                        .stream()
                        .reduce("", (a, b) -> a + " " + b))
                .orElse(null);

        String manifest = Optional
                .ofNullable(find.find(project.getProjectFile(), "MANIFEST.MF", new String[0], true))
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> list.get(0))
                .orElse(null);

        String jar_command =
                String.format(buildCommand,
                        "../" + project_jar_name,
                        manifest,
                        classes
                );

        ExecuteResult execute_result = terminal.execute(jar_command, new String[0], new File(project.getProjectFile().getAbsolutePath() + "/target"));

        project.executedIssue(execute_result, new File(project.getProjectFile().getAbsolutePath() + "/" + project_jar_name));
    }

    @Override
    public String stemName() {
        return stemName;
    }
}
