# Product Monitoring — Мониторинг остатков

Система учёта продуктов, поставок, партий со сроками годности, блюд (рецептов) и заказов для ресторана / кухни.

**Стек:** Java 21 · Spring Boot 4 · PostgreSQL · React + Vite · Spring Security

---

## Возможности

| Модуль | Описание |
|--------|----------|
| **Склад** | Партии продуктов, сроки годности, списание просрочки |
| **Поставки** | Приём новой поставки на склад |
| **Продукты** | Справочник продуктов (`ProductInfo`) |
| **Блюда** | Создание блюд и рецептов (ингредиенты + количество) |
| **Заказы** | Оформление заказов с принципом **FEFO** (First Expired, First Out) |
| **Стоп-лист** | Недоступные блюда + прогноз |
| **Горячие предложения** | Рекомендации к продаже (истекающие партии) |
| **Аналитика** | Списания, дефицит |
| **SSE** | Real-time события склада (`/api/events`) |

---

## Структура проекта

```
├── pom.xml                          # Backend (Maven)
├── src/main/java/org/bacljan/product_monitoring/
│   ├── ProductMonitoringApplication.java
│   ├── config/                      # Security, CORS, Cache
│   ├── controller/                  # REST API
│   ├── model/                       # JPA-сущности
│   ├── mapper/                      # MapStruct
│   └── exception/                   # Обработка ошибок
├── src/main/resources/
│   └── application.properties
└── frontend/                        # React SPA
    ├── index.html
    ├── package.json
    └── src/
```

---

## Требования

- **JDK 21+**
- **Maven 3.9+**
- **PostgreSQL** (БД `product_monitoring`)
- **Node.js** (для frontend, опционально)

---

## Настройка БД

В `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/product_monitoring
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
server.port=8081
```

Создайте базу:

```sql
CREATE DATABASE product_monitoring;
```

---

## Запуск Backend

```bash
mvn spring-boot:run
```

API будет доступен по адресу: **http://localhost:8081**

### Авторизация (HTTP Basic)

| Пользователь | Пароль   | Роль    |
|--------------|----------|---------|
| `admin`      | `admin`  | ADMIN   |
| `kitchen`    | `kitchen`| KITCHEN |
| `waiter`     | `waiter` | WAITER  |

---

## Запуск Frontend

```bash
cd frontend
npx --yes serve -p 3000
```

Откройте http://localhost:3000

В шапке укажите URL API (по умолчанию `http://localhost:8081`) и нажмите **Обновить**.

Альтернатива — открыть `frontend/index.html` напрямую в браузере (может потребоваться CORS).

---

## Основные API-эндпоинты

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/api/products` | Список продуктов |
| `POST` | `/api/products` | Регистрация продуктов |
| `POST` | `/api/shipment` | Новая поставка |
| `GET` | `/api/stocks` | Остатки |
| `GET` | `/api/stocks/batches` | Все партии |
| `GET` | `/api/stocks/expiring?days=3` | Истекающие партии |
| `POST` | `/api/stocks/write-off-expired` | Списание просрочки |
| `GET` / `POST` | `/api/dishes` | Блюда |
| `GET` | `/api/stoplist` | Стоп-лист |
| `GET` | `/api/stoplist/forecast` | Прогноз стоп-листа |
| `GET` | `/api/hot-offers` | Горячие предложения |
| `GET` | `/api/analytics` | Аналитика |
| `GET` | `/api/events` | SSE-события склада |
| `POST` | `/api/demo/seed` | Демо-данные |
| `GET` | `/api/me` | Текущий пользователь |

---

## Доменная модель (кратко)

- **ProductInfo** — справочник продукта
- **Product** (партия / batch) — конкретная партия с количеством и сроком годности
- **Shipment** — поставка
- **Dish** + **DishIngredient** — блюдо и его рецепт
- **Order** + **OrderItem** — заказ и позиции
- **OrderStatus** — статус заказа

При списании и оформлении заказа применяется **FEFO**: в первую очередь используются партии с ближайшим сроком годности.

---

## Лицензия

Проект учебный / демонстрационный.
