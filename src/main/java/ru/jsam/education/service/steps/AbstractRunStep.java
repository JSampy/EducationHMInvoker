package ru.jsam.education.service.steps;

import ru.jsam.education.dto.Data;
import ru.jsam.education.dto.ExecuteResult;
import ru.jsam.education.pipeline.Project;

import java.io.IOException;
import java.util.Arrays;

public abstract class AbstractRunStep implements Step {

    @Override
    public boolean validate(Project project) {
        return project.getProjectFile() != null
                && project.getProjectFile().exists()
                && (!project.isIntputDataExists() || project.getInputData() != null);
    }


    @Override
    public void accept(Project project) throws Exception {
        int success = 0;
        int fail = 0;
        int error = 0;

        for (Data data : project.getInputData().getDataSet()) {
            try {
                ExecuteResult executed_result = run(project, data);

                if (executed_result.isSuccess() && executed_result.getResult().equals(data.getExpected())) {
                    success++;
                } else {
                    fail++;
                    data.setResult(executed_result.getResult());
                    project.addFailData(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                error++;
                project.addErrorData(data);
            }
        }

        project.setSuccessCount(success);
        project.setFailCount(fail);
        project.setErrorCount(error);

        project.executedIssue(
                true,
                0,
                Arrays.asList(
                        String.format(
                                "Run command expected : success : %s, fail : %s, error : %s",
                                success,
                                fail,
                                error
                        )
                ),
                null
        );
    }

    abstract ExecuteResult run(Project project, Data data) throws IOException;

}
