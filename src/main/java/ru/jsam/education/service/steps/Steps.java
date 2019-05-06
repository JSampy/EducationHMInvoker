package ru.jsam.education.service.steps;

public enum Steps {

    GIT_CLONE(new GitStep()),
    COMPILE(new CompileStep()),
    BUILD_SIMPLE(new BuildSimpleStep()),
    RUN(new RunJavaStep());

    private Step step;

    Steps(Step step) {
        this.step = step;
    }

    public Step getInstance() {
        return step;
    }
}