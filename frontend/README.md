# Фронтенд — Мониторинг остатков (Флагман)

Одностраничный интерфейс без сборки. Работает из браузера.

## Запуск

### Вариант 1 — просто открыть
Открой `index.html` в браузере.  
Если бэкенд на другом origin, браузер может заблокировать запросы (CORS) — тогда используй вариант 2.

### Вариант 2 — локальный сервер (рекомендуется)
```bash
cd frontend
npx --yes serve -p 3000
```
Открой http://localhost:3000

Или:
```bash
python3 -m http.server 3000
```

## Подключение к бэкенду

1. Запусти Spring Boot (обычно `http://localhost:8080`)
2. В шапке сайта в поле URL укажи адрес API (по умолчанию `http://localhost:8080`)
3. Нажми **Обновить**

### CORS на бэкенде

Добавь конфиг, иначе браузер заблокирует запросы:

```java
package org.bacljan.product_monitoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "null")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
```

`null` нужен, если открываешь `index.html` как файл (`file://`).

## Экраны

| Вкладка    | Что делает                          |
|------------|-------------------------------------|
| Склад      | Партии, сроки, списание просрочки   |
| Поставка   | Приём новой поставки                |
| Продукты   | Справочник продуктов                |
| Блюда      | Создание блюд и рецептов            |
| Заказ      | Оформление заказа (FEFO)            |
| Стоп-лист  | Недоступные блюда                   |
| Горячие    | Рекомендации к продаже              |
| Аналитика  | Списания и дефицит                  |
