package ru.quick.approval.system.taskinfogateway.model;

import org.springframework.http.HttpMethod;

public class Property {

    static final String TOKEN = "956369237:AAGsI_9k9HzYDP6Q_4IzG9NNuhX-Y6ZDuDg";

    static final String NAME = "QuickApprovalSystemBot";

    static final String DOMAIN = "http://localhost:";

    static final String PORT ="8080" ;

    static final String AGREEDTASKENDPOINT = "/task/taskID/status/agreed";

    static final HttpMethod AGREEDTASKMETHOD  = HttpMethod.POST;

    static final HttpMethod DENIEDTASKMETHOD  = HttpMethod.POST;

    static final String DENIEDTASKENDPOINT = "/task/taskID/status/denied" ;
}
