package ru.jsam.education.utills;

import ru.jsam.education.dto.ExecuteResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class TerminalCommandExecutor {

    public ExecuteResult execute(String command, String[] commandArgs, File projectDir) throws IOException {
        return
                Optional.ofNullable(projectDir)
                        .filter(File::exists)
                        .map(f -> {
                            try {
                                return Optional.ofNullable(Runtime.getRuntime().exec(command, commandArgs, f));
                            } catch (IOException e) {
                                return Optional.empty();
                            }
                        })
                        .orElse(Optional.ofNullable(Runtime.getRuntime().exec(command, commandArgs)))
                        .map(o -> {
                            Process process = (Process) o;
                            try {
                                process.waitFor();
                            } catch (InterruptedException e) {
                                return null;
                            }
                            return process;
                        })
                        .map(p -> {
                                    boolean success = p.exitValue() == 0;
                                    InputStream in = success ? p.getInputStream() : p.getErrorStream();

                                    return ExecuteResult.builder()
                                            .result(
                                                    Optional.of(p)
                                                            .map(o -> in)
                                                            .map(InputStreamReader::new)
                                                            .map(BufferedReader::new)
                                                            .map(r -> r.lines()
                                                                    .collect(Collectors.toList()))
                                                            .orElse(new ArrayList<>())
                                            )
                                            .exitCode(p.exitValue())
                                            .success(success)
                                            .build();
                                }
                        )
                        .orElseThrow(() -> new RuntimeException("UNDEFINED EXCEPTION on : " + command + ". Path: " + projectDir.getAbsolutePath()));
    }
}
