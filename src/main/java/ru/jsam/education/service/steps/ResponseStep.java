package ru.jsam.education.service.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.jsam.education.pipeline.Project;

public class ResponseStep implements Step {
    @Override
    public boolean validate(Project project) {
        return project.isSuccess() && StringUtils.isNotEmpty(project.getResponseUrl());
    }

    @Override
    public void accept(Project project) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(project);

        HttpClient http = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost();

        HttpEntity entity = new SerializableEntity(response);

        post.setEntity(entity);

        http.execute(post);

    }

    @Override
    public String stemName() {
        return "RETURN STEP";
    }
}
