# TaskInfoGateway

1. Сервис предназначен для взаимодействия с участником согласования, посредством Телеграм Бота
2. Для тестирования сервис необходимо:
	- Зарегистрироваться у телеграмм бота @QuickApprovalSystemBot и получить ID своего чата с ботом. Специфика Телеграм Ботов такова, что чтобы Бот мог тебе написать - ты должен стартовать работу с ним.
	- Вызвать сервис http://localhost:8082/message/user с методом POST и запросом {"telegramId":,"text":"Simple Text", "taskId":1}
	- Ответить на сообщение бота с помощью кнопки Согласовать или Отказать
	- В зависимости от ответа в DBController пойдёт вызов сервиса смены статуса у задачи. По умолчанию сейчас меняется статус 1-ой задачи.

Сервис взаимодействия с Telegram ботом для приложения QAS