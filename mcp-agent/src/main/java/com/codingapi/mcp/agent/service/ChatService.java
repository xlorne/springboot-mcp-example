package com.codingapi.mcp.agent.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

@Component
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = builder
                .defaultTools(toolCallbackProvider)
                .build();
    }

    public String ask(String question) {
        ChatClient.ChatClientRequestSpec requestSpec = chatClient.prompt(question);
        ChatClient.CallResponseSpec responseSpec = requestSpec.call();
        return responseSpec.content();
    }

}
