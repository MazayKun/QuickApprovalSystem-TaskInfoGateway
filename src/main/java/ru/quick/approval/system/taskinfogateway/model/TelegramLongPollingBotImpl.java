package ru.quick.approval.system.taskinfogateway.model;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.quick.approval.system.api.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author SolodkovVV
 * Класс для реализации телеграмм бота с вариантом long pulling
 * служит для взаимодействия с апи бота, получения ответов от подльзователей и отправки сообщений пользователям
 */
@Log4j2
@Component
public class TelegramLongPollingBotImpl extends TelegramLongPollingBot {
    /**
     * Метод для отправки текста по id чата от имени бота
     * сообщение приходит с кнопками
     *
     * @param chatId id чата с пользователем
     * @param text   текст сообщения для отправки
     */
    public void sendMsg(int chatId, String text, int taskId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId((long) chatId);
        sendMessage.setText(text);
        setButtons(sendMessage, taskId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Метод установки кнопок к сообщению.
     * Кнопки автоматически масштабируются по экрану и исчезают после ответа
     *
     * @param sendMessage сообщение, к которому прикрепляют кнопки
     */
    private void setButtons(SendMessage sendMessage, int taskId) {
        //TO DO
        //убрать возможность пользователю писать сообщение, только нажимать кнопку, если возможно
        log.info(sendMessage.toString());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Отказать " + taskId));
        keyboardFirstRow.add(new KeyboardButton("Согласовать " + taskId));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    /**
     * Метод обработки ответов от всех пользователей. Реализует основную логику вызова сторонних сервисов для передачи ответов
     * В зависимости от полученного сообщение маршрутизует сообщение
     * Срабатывает каждый раз, когда пользователь отвечает Боту
     *
     * @param update входящий параметр, который пришёл от пользователя
     */
    @Override
    public void onUpdateReceived(Update update) {
        log.info("get message " + update.toString());
        Message message = update.getMessage();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = null;
        ResponseEntity<Task[]> responseTasks = null;
        String[] textWords = message.getText().split(" ");
        switch (textWords[0]) {
            case "Согласовать":
                response = restTemplate.exchange(Property.DOMAIN + Property.PORT + Property.AGREEDTASKENDPOINT.replaceAll("taskID", textWords[1]),
                        Property.AGREEDTASKMETHOD, entity, String.class);
                log.info(response.toString());
                sendMsg(message, "Ваш ответ принят. Согласовано");
                break;
            case "Отказать":
                response = restTemplate.exchange(Property.DOMAIN + Property.PORT + Property.DENIEDTASKENDPOINT.replaceAll("taskID", textWords[1]),
                        Property.DENIEDTASKMETHOD, entity, String.class);
                log.info(response.toString());
                sendMsg(message, "Ваш ответ принят. Отказано");
                break;
            case "help":
                sendMsg(message, "Вы можете получить список своих задач на согласование с помощью фразы Список. \n" +
                        "Так же Вы можете согласовать или отказать по любой заявке с помощью Согласовать Код заявки или Отказать Код заявки");
                break;
            case "Список":
                //TO DO
                //необходимо указать новый сервис, который будет возвращать список АКТИВНЫХ задач пользователя
                responseTasks = restTemplate.exchange("http://localhost:8080/user/6/task",
                        HttpMethod.GET, entity, Task[].class);
                sendMsg(message, "Список:");
                for (int i = 0; i < responseTasks.getBody().length; i++) {
                    sendMsg(message, "Задача :" + responseTasks.getBody()[i].toString());
                }
                break;
            default:
                sendMsg(message, "Не распознанная команда. Повторите снова. Напишите help и Бот поможет" +
                        "");
        }
    }

    /**
     * Метод для отправки объекта с сообщением по id чата от имени бота
     * Используется для ответа на сообщение пользователя
     *
     * @param message сообщение к отправке
     * @param text    текст сообщения
     */
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return Property.NAME;
    }

    @Override
    public String getBotToken() {
        return Property.TOKEN;
    }


}
