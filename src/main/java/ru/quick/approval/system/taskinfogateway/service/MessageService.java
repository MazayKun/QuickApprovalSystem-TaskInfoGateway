package ru.quick.approval.system.taskinfogateway.service;

public interface MessageService {
    boolean sendMessage(int telegramId, String text, int taskId);
}
