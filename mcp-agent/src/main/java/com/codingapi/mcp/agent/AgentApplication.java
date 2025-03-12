package com.codingapi.mcp.agent;

import com.codingapi.mcp.agent.interceptor.LoggingInterceptor;
import com.codingapi.mcp.agent.service.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class AgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }

    @Bean
    public RestClient.Builder restClient() {
        return RestClient.builder()
                .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .requestInterceptor(new LoggingInterceptor(false));
    }


    @Bean
    public CommandLineRunner predefinedQuestions(ChatService chatService, ConfigurableApplicationContext context) {

        return args -> {
            System.out.println("Running predefined questions with AI model responses:\n");
            String question = "how is weather forcast on 47.6062,-122.3321 in Seattle?";
            System.out.println("QUESTION: " + question);
            System.out.println("ASSISTANT: " + chatService.ask(question));
            context.close();
        };
    }


}
