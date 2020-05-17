package ru.quick.approval.system.taskinfogateway.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.quick.approval.system.taskinfogateway.controller.TelegramBotController;


/**
 * @author SolodkovVV
 * Сервис для обработки запросов через контроллер бота {@link TelegramBotController}
 */
@Service
@Log4j2
public class MessageServiceImpl implements MessageService {

    @Autowired
    public MessageServiceImpl() {
    }

    /**
     * Метод генерации сообщения для отпавки пользователю
     * @param text текст сообщения, который пришёл из DBController
     * @param taskId id задачи пользователя, нужна для того, чтобы знать куда слать ответ
     * @return возвращает подготовленную строку для отпрвавки
     */
    private String generateMessageToUser(String text, int taskId){
        log.info("генерация сообщения по задаче " + taskId);
        return "Задача № " + taskId + " \n " +
                text + " \n" +
                "Необходимо согласовать или отказать. Для этого воспользуйтесь клавиатурой Бота";
    }

    /**
     * Метод отправки сообщения через контроллер телеграм бота {@link TelegramBotController}
     * @param telegramId чат id с согласовантом
     * @param text текст для отправк
     * @param taskId id задачи для отправки
     * @return возвращает результат отправки
     */
    @Override
    public boolean sendMessage(int telegramId, String text, int taskId) {
        //TO DO
        //ветку ответа с false, которая появится при ошибке
        log.info("try to send message");
        TelegramBotController.sendMessage(telegramId, generateMessageToUser(text, taskId), taskId);
        return true;
    }
}
