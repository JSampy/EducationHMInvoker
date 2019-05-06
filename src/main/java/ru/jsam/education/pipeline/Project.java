package ru.jsam.education.pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import ru.jsam.education.dto.ExecuteResult;
import ru.jsam.education.dto.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private String src;

    private String dst;

    private File projectFile;

    private String responseUrl;

    private List<String> log = new ArrayList<>();

    private List<String> successLog = new ArrayList<>();

    private List<String> errorLog = new ArrayList<>();

    private boolean success = true;

    private int lastExitCode;

    private String lastMessage;

    private String lastSuccessMessage;

    private String lastErrorMessage;

    private boolean isIntputDataExists;

    private boolean isInputDataInSystemIn;

    private List<String> stackTrace = new ArrayList<>();

    private InputFile inputData;

    private int successCount;
    private int failCount;
    private int errorCount;

    private List<ru.jsam.education.dto.Data> failData;
    private List<ru.jsam.education.dto.Data> errorData;

    private void addRecord(List<String> list, String log) {
        list.add(log);
    }

    private void addRecords(List<String> list, List<String> records) {
        records
                .forEach(r -> addRecord(list, r));
    }

    public void addLog(String log) {
        addRecord(this.log, log);
    }

    public void addLog(List<String> logs) {
        addRecords(this.log, logs);
    }

    public void addSuccessLog(String log) {
        addRecord(successLog, log);
    }

    public void addSuccessLog(List<String> logs) {
        addRecords(successLog, logs);
    }

    public void addErrorLog(String log) {
        addRecord(errorLog, log);
    }

    public void addErrorLog(List<String> logs) {
        addRecords(errorLog, logs);
    }

    private void addData(List<ru.jsam.education.dto.Data> dataList, ru.jsam.education.dto.Data data){
        dataList.add(data);
    }

    public void addFailData(ru.jsam.education.dto.Data data){
        if (failData == null){
            failData = new ArrayList<>();
        }
        addData(failData, data);
    }

    public void addErrorData(ru.jsam.education.dto.Data data){
        if (errorData == null){
            errorData = new ArrayList<>();
        }
        addData(errorData, data);
    }

    public void executedIssue(ExecuteResult result, File file) {
        executedIssue(
                result.getExitCode() == 0,
                result.getExitCode(),
                result.getResult(),
                file
        );
    }

    public void executedIssue(
            boolean success,
            int exitCode,
            List<String> executeResultLog,
            File projectFile
    ) {
        setSuccess(success);
        setLastExitCode(exitCode);

        addLog("expected : " + executeResultLog);
        if (success) {
            addSuccessLog(executeResultLog);
            setLastSuccessMessage(
                    Optional.of(executeResultLog)
                            .filter(CollectionUtils::isNotEmpty)
                            .map(list -> list.stream().reduce("", (a, b) -> a + " " + b))
                            .orElse(null)
            );
        } else {
            addErrorLog(executeResultLog);
            setLastErrorMessage(
                    Optional.of(executeResultLog)
                            .filter(CollectionUtils::isNotEmpty)
                            .map(list -> list.stream().reduce("", (a, b) -> a + " " + b))
                            .orElse(null)
            );
        }

        addLog("exit code: " + exitCode);
        addLog("success: " + success);
        setLastMessage(executeResultLog
                .stream()
                .reduce("", (a, b) -> b));

        if (projectFile != null) {
            setProjectFile(projectFile);
        }
    }

    public void errorIssue(String messaage) {
        setSuccess(false);
        setLastExitCode(-1);
        addLog("exit code: -1");
        addLog(messaage);
        addErrorLog(messaage);
        setLastMessage(messaage);
        setLastErrorMessage(messaage);
    }

    public void errorIssue(Exception e) {
        setSuccess(false);
        setLastExitCode(-1);
        addLog("exit code: -1");
        addLog(e.getMessage());
        addErrorLog(e.getMessage());
        setLastMessage(e.getMessage());
        setLastErrorMessage(e.getMessage());
        setStackTrace(
                Stream.of(e.getStackTrace())
                        .map(s -> s.toString())
                        .collect(Collectors.toList()));
        getStackTrace().add(0, e.getClass().getName());
    }

}