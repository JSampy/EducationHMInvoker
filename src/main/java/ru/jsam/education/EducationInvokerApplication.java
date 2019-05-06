package ru.jsam.education;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.jsam.education.dto.InputFile;
import ru.jsam.education.pipeline.Pipeline;
import ru.jsam.education.pipeline.Project;
import ru.jsam.education.service.steps.CompileStep;
import ru.jsam.education.service.steps.GitStep;
import ru.jsam.education.service.steps.BuildSimpleStep;
import ru.jsam.education.service.steps.RunJavaStep;

import java.io.File;
import java.io.IOException;

public class EducationInvokerApplication {

    public static void main(String[] args) throws IOException {

        String src = args[0];//"git@github.com:JSampy/SimpleJava.git";
        String dst = args[1];//"/home/shamil/projects/simple/git/";
        File inputFile = new File(args[2]);//new File(System.getProperty("user.home") + "/projects/simple/inputdata.txt");
        String responseUrl = "http://localhost:8080/response/some_guid";

        ObjectMapper mapper = new ObjectMapper();
        InputFile inputData = mapper.readValue(inputFile, InputFile.class);

        Project project = new Project();
        project.setSrc(src);
        project.setDst(dst);
        project.setResponseUrl(responseUrl);
        project.setInputData(inputData);
        project.setIntputDataExists(true);
        project.setInputDataInSystemIn(false);

        Pipeline pipeline = new Pipeline();
        pipeline.addSteps(
                new GitStep(),
                new CompileStep(),
                new BuildSimpleStep(),
                new RunJavaStep()
        );

        pipeline.run(project);

        System.out.println(mapper.writeValueAsString(project));
    }
}