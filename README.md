# NL→SQL UI (Spring Boot)

Веб-интерфейс для взаимодействия с FastAPI сервисом **NL→SQL Reports**.

Показывает:
- ввод текстового описания отчёта,
- результат генерации (SQL + rationale + ошибки валидации),
- выполнение и вывод таблицы,
- (опционально) превью "контекста": схема БД + top-distinct по выбранным колонкам.

## Требования
- Java 17+
- Запущенный FastAPI сервис (по умолчанию http://127.0.0.1:8000)
- (Опционально) доступ к Postgres для отображения контекста превью

## Настройка
`src/main/resources/application.yml`:
- `nl2sql.fastapi.base-url`
- `spring.datasource.*` (если `nl2sql.context.enabled=true`)

## Запуск
```bash
mvn spring-boot:run
```

Открыть: http://127.0.0.1:8080
