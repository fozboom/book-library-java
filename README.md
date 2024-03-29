# Book Library Java Application

Это приложение представляет собой простую книжную библиотеку, разработанную на Java с использованием Spring Boot Framework и Hibernate. Оно включает в себя сложные связи между сущностями, такие как "один ко многим", "многие к одному" и "многие ко многим". Приложение также использует кэширование для ускорения доступа к данным.

## Описание

Приложение позволяет управлять коллекцией книг, авторов и издателей, включая добавление новых книг, авторов и издателей, редактирование существующих и удаление неактуальных записей. В основе реализации лежит архитектурный паттерн MVC (Model-View-Controller), что обеспечивает четкое разделение бизнес-логики, представления и управления веб-запросами. Для передачи данных между слоями используются объекты DTO (Data Transfer Object). Для ускорения доступа к данным используется кэш-сервис, который содержит отдельные кэши для каждой сущности.

## Содержание
- [Запуск](#Запуск)
- [Реализация](#Реализация)
- [SonarCloud](SonarCloud)
- [Контакты разработчика](#Контакты)

## Задание 1

1. Создайте и запустите локально простейший веб/REST сервис, используя любой открытый пример с использованием Java stack: Spring (Spring Boot)/Maven/Gradle/Jersey/Spring MVC.
2. Добавьте GET ендпоинт, принимающий входные параметры в качестве queryParams в URL согласно варианту, и возвращающий любой hard-coded результат в виде JSON согласно варианту.

## Задание 2

1. Подключите к проекту базу данных (PostgreSQL/MySQL/и т.д.). Реализуйте связи между сущностями:
    - (0 - 7 баллов) - Реализация связи один ко многим `@OneToMany`
    - (8 - 10 баллов) - Реализация связи многие ко многим `@ManyToMany`
2. Реализуйте CRUD-операции со всеми сущностями.

## Задание 3

1. Добавить в проект GET ендпоинт (он должен быть полезный) с параметром(-ами). Данные должны быть получены из БД с помощью "кастомного" запроса (@Query) с параметром(-ами) ко вложенной сущности.
2. Добавить простейший кэш в виде in-memory Map (в виде отдельного бина).


## Запуск

1. **Подключение к базе данных**: Для полной функциональности проекта необходимо подключиться к базе данных MySQL версии 8.0.33. Разверните Docker контейнер с MySQL и укажите параметры подключения в файле `application.properties`, а именно: хост, логин, пароль и имя базы данных. В проекте используется Spring Data JPA и Hibernate для взаимодействия с базой данных, что обеспечивает удобный и гибкий способ работы с данными.
2. **Запуск приложения**: После настройки подключения к базе данных, запустите приложение. Можно использовать средства сборки Maven или Gradle для сборки и запуска проекта. В случае использования Maven, выполните команду `mvn spring-boot:run` в корневой директории проекта. Если вы используете Gradle, выполните команду `gradle bootRun`.
3. **Использование веб-интерфейса**: После успешного запуска приложения откройте веб-браузер и перейдите по адресу `http://localhost:8080/api/v1/books`, чтобы получить список всех книг в формате JSON. Для выполнения CRUD-операций используйте соответствующие HTTP-методы (GET, POST, PUT, DELETE) на соответствующих ендпоинтах (`/api/v1/books`, `/api/v1/authors`, `/api/v1/publishers`).

## Реализация

В проекте реализованы три основные сущности: `Book`, `Author` и `Publisher`. Между ними установлены следующие связи:

- `Book` и `Author` имеют связь "многие ко многим", что позволяет одной книге иметь множество авторов, и наоборот, одному автору - множество книг.
- `Book` и `Publisher` имеют двустороннюю связь. Один издатель (`Publisher`) может издавать множество книг (`Book`), и наоборот, одна книга может быть издана множеством издателей.

Для управления данными используются репозитории Spring Data JPA, которые предоставляют методы для основных CRUD-операций. Контроллеры обрабатывают веб-запросы и делегируют выполнение бизнес-логики сервисам. Данные передаются между слоями с использованием объектов DTO.

## Sonar Cloud

Анализ проекта можно посмотреть по [ссылке](https://sonarcloud.io/project/overview?id=fozboom_book-library-java)

## Контакты

По любым вопросам писать на почту fozboom@gmail.com 
	
