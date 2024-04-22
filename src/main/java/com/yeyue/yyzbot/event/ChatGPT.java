package com.yeyue.yyzbot.event;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ChatGPT {
    @Value("${userAccount.chatGpt.token}")
    private String token;

    private static final String lock = "gptLock";
    private static OpenAiService service;
    List<ChatMessage> messages = new ArrayList<>();

    @Component
    public class StartInit2 implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            service = new OpenAiService(token, Duration.ofSeconds(55));
        }
    }

    public void chatGPT(MessageEvent event, Integer type) {
        try {
            String[] command = event.getMessage().serializeToMiraiCode().split("\\s+");
            String text = type == 1 ? command[0] : command[1];

            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), text);

            if (text.equals("重置")) {
                messages = new ArrayList<>();
                event.getSubject().sendMessage("对话已重置");
                return;
            }

            synchronized (lock) {
                messages.add(systemMessage);

                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                        .builder()
                        .model("gpt-3.5-turbo")
                        .messages(messages)
                        .n(1)
                        .maxTokens(500)
                        .logitBias(new HashMap<>())
                        .build();

                List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
                ChatCompletionChoice chatCompletionChoice = choices.get(0);
                ChatMessage chatMessage = chatCompletionChoice.getMessage();
                if (type == 1) {
                    event.getSubject().sendMessage(new MessageChainBuilder().append(chatMessage.getContent().replace("\\n", "")).build());
                } else {
                    event.getSubject().sendMessage(new MessageChainBuilder().append(new At(event.getSender().getId())).append(chatMessage.getContent().replace("\\n", "")).build());
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            event.getSubject().sendMessage("出错拉，QAQ");
        }
    }

}
