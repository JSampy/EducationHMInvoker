package ru.jsam.education.utills;

import org.apache.commons.collections4.CollectionUtils;
import ru.jsam.education.dto.ExecuteResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FindUtils {

    private TerminalCommandExecutor terminal;

    public FindUtils() {
        terminal = new TerminalCommandExecutor();
    }

    public List<String> find(File dir, String expression, String[] args, boolean isFullPath) throws IOException {
        return Optional.ofNullable(terminal.execute("find -name " + expression, args, dir))
                .filter(ExecuteResult::isSuccess)
                .map(ExecuteResult::getResult)
                .orElse(new ArrayList<>())
                .stream()
                .map(s -> (isFullPath ? dir.getAbsolutePath() + "/" : "") + s)
                .collect(Collectors.toList());
    }

    public List<String> findClasses(File dir, String expression, String[] args, boolean isFullPath) throws IOException {
        return Optional.ofNullable(find(dir, expression, args, isFullPath))
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> list
                        .stream()
                        .map(s -> s.replaceAll("./target/", ""))
                        .collect(Collectors.toList()))
                .orElse(null);
    }
}