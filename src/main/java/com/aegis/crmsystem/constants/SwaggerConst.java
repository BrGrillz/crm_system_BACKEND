package com.aegis.crmsystem.constants;

import springfox.documentation.service.Contact;

public class SwaggerConst {
    public static final String PATH = "com.aegis.crmsystem";
    public static final String PATH_REGEX = "/api/v1.*";


    public static final class MetaData{
        public static final String TITLE = "Spring Boot REST API";
        public static final String DESCRIPTION = "Spring Boot REST API для aegis crmsystem";
        public static final String VERSION = "1.0.0";
        public static final String LICENCE = "Apache License Version 2.0";
        public static final String LICENCE_URL = "https://www.apache.org/licenses/LICENSE-2.0";
        public static final Contact CONTACT = new Contact("Шалайкин Глеб Николаевич", "", "gleb.shalaykin@gmail.com");

    }

    public static final class Tasks{
        public static final String API_TITLE = "ЗАДАЧИ";
        public static final String GET_ALL_TASKS = "Получение списка задач";
        public static final String GET_TASK_BY_ID = "Получить задачу";
        public static final String CREATE_TASK = "Создать задачу";
        public static final String UPDATE_TASK = "Обновить задачу";
        public static final String DELETE_TASK = "Удалить задачу";

        public static final class Model{
            public static final String ID = "ID задачи";
            public static final String TITLE = "Заголовок задачи";
            public static final String TEXT = "Текс задачи";
            public static final String CREATE_DATE = "Дата создания задачи";
            public static final String UPDATE_DATE = "Дата обновления задачи";
        }
    }

    public static final class Auth{
        public static final String API_TITLE = "АВТОРИЗАЦИЯ";
        public static final String LOGIN = "Авторизация пользователя";
        public static final String REFRESH_TOKEN = "Получить новые токены по refresh token";
        public static final String VERIFY_TOKEN = "Проверка валидности access token";
    }

    public static final class Admin{
        public static final String API_TITLE = "АДМИНИСТРИРОВАНИЕ";
        public static final String ALL_ROLE = "Получить список всех ролей";
        public static final String ALL_USERS_BY_ROLE = "Получить список пользователей связанных с этой ролью";
        public static final String ALL_USERS = "Получить список всех пользователей";
    }
}
