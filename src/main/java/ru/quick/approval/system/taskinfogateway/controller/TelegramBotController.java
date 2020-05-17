package ru.quick.approval.system.taskinfogateway.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.quick.approval.system.taskinfogateway.model.Property;
import ru.quick.approval.system.taskinfogateway.model.TelegramLongPollingBotImpl;

/**
 * @author SolodkovVV
 * Контроллер для управления телеграмм ботом. При инициализации приложения подключается к боту и начинает получать от него все сообщения
 */
@Log4j2
@Controller
public class TelegramBotController {
    private static TelegramLongPollingBotImpl telegramLongPollingBotImpl;

    /**
     * Конструктор, при старте запускает телеграм бота, который сразу подцепляется к чатам и ждёт сообщений
     */
    public TelegramBotController() {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramLongPollingBotImpl = new TelegramLongPollingBotImpl();
        try {
            telegramBotsApi.registerBot(telegramLongPollingBotImpl);
        }
        catch (TelegramApiRequestException e){
            e.printStackTrace();
            log.error("bot error");
        }
    }

    /**
     * Метод вызова отправки сообщения у телеграм бота
     * @param telegramId id чата телеграм бота с согласовантом
     * @param text текст сообщения для отправки
     */
    public static void sendMessage(int telegramId, String text, int taskId){
        telegramLongPollingBotImpl.sendMsg(telegramId, text, taskId);
    }
}
