package ru.quick.approval.system.taskinfogateway.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import ru.quick.approval.system.api.controller.MessageApi;
import ru.quick.approval.system.api.model.MessageTelegram;
import ru.quick.approval.system.taskinfogateway.service.MessageService;

import java.util.Optional;

/**
 * @author SolodkovVV
 * контроллер для управления входящим от сервиса DBController сообщением
 */
@RestController
@Log4j2
public class MessageController implements MessageApi {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * Метод для получения входящего сообщения и отправки уведомления в телеграм бота
     * @param message входящее сообщение
     * @return результат обработки сервиса
     */
    @Override
    public ResponseEntity<Void> messageSend(MessageTelegram message) {
        log.info("start messageSend with " + message.getTelegramId() + " " + message.getText());
        ResponseEntity<Void> responseEntity = null;
        if (messageService.sendMessage(message.getTelegramId(), message.getText(), message.getTaskId())) {
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } else responseEntity = new ResponseEntity<>(HttpStatus.CONFLICT);
        return responseEntity;
    }
}
