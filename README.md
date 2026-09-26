# Card Transfer Tests (Selenide + JUnit 5)

Автотесты для функции перевода средств с карты на карту в тестовом приложении
`app-ibank-build-for-testers.jar` (Netology, ДЗ "Page Object's").

## Стек
- Java 11
- Gradle
- Selenide 6.19.1
- JUnit 5

## Структура проекта
```
card-transfer-tests
├── artifacts
│   └── app-ibank-build-for-testers.jar   (положить сюда самому, см. ниже)
├── src
│   └── test
│       └── java
│           ├── data
│           │   └── DataHelper.java
│           ├── pages
│           │   ├── LoginPage.java
│           │   ├── VerificationPage.java
│           │   ├── DashboardPage.java
│           │   └── TransferPage.java
│           └── tests
│               ├── BaseTest.java
│               └── CardTransferTest.java
├── build.gradle
├── settings.gradle
├── .gitignore
└── README.md
```

## 1. Перед первым запуском

`app-ibank-build-for-testers.jar` уже лежит в папке `artifacts/` — я положил
именно тот файл, который ты прислал, и селекторы в коде проверены прямо по
его содержимому (распаковал jar и прочитал минифицированный JS фронтенда),
так что переписывать их вручную через DevTools не требуется.

1. Запусти SUT в отдельном терминале (или через IntelliJ Terminal):
   ```
   java -jar artifacts/app-ibank-build-for-testers.jar
   ```
   Приложение поднимет локальный сервер на `http://localhost:9999`.
   Не закрывай этот терминал, пока пишешь и гоняешь тесты — если его закрыть,
   тесты начнут падать с ошибкой подключения.
2. (Необязательно) Проверь глазами, что приложение работает: открой
   `http://localhost:9999`, залогинься вручную (`vasya` / `qwerty123`,
   код `12345`), убедись, что видишь список из двух карт.

## 2. Импорт проекта в IntelliJ IDEA

1. `File → Open...` → выбери папку `card-transfer-tests` → `OK`.
2. IntelliJ увидит `build.gradle` и предложит импортировать Gradle-проект —
   нажми **Load Gradle Project** (или дождись автоматической синхронизации,
   индикатор внизу справа).
3. Дождись, пока в правом нижнем углу закончится индексация и загрузка
   зависимостей ("Indexing...", потом "Downloading...").

## 3. Запуск тестов

**Из IntelliJ:** открой `src/test/java/tests/CardTransferTest.java`,
нажми на зелёный треугольник слева от названия класса → **Run 'CardTransferTest'**.

**Через панель Gradle в IntelliJ (рекомендуется):**
Справа сбоку окна IntelliJ есть вертикальная вкладка **Gradle**. Открой её →
разверни `card-transfer-tests → Tasks → verification` → дважды кликни на
**test**. Внизу откроется консоль с результатом.

**Из терминала:** в этом проекте нет `gradlew`/`gradlew.bat` (Gradle Wrapper),
поэтому команда `./gradlew ...` работать не будет ("файл не найден"). Либо
используй способ выше через панель Gradle, либо, если у тебя отдельно
установлен Gradle, набери просто `gradle clean test`.

В любом случае убедись, что SUT (jar) запущен и ждёт на `localhost:9999` —
иначе тесты упадут с ошибкой подключения/таймаутом.

Результат смотри в консоли: `BUILD SUCCESSFUL` и зелёные галочки — тесты прошли.
`BUILD FAILED` — открой лог, найди `AssertionError` или ошибку Selenide
(обычно означает, что нужно поправить селектор или это реальный баг SUT).

Отчёт в HTML: `build/reports/tests/test/index.html` — открой в браузере.

## 4. Если тест падает из-за бага приложения

Не подгоняй тест под баг. Заведи Issue в своём GitHub-репозитории с описанием:
шаги воспроизведения, ожидаемый результат, фактический результат, скриншот.

## 5. Git и GitHub

В корне проекта:
```
git init
git add .
git commit -m "Add Page Object tests for card-to-card transfer"
```

Создай пустой публичный репозиторий на github.com (без README/gitignore —
они уже есть локально), затем:
```
git branch -M main
git remote add origin https://github.com/<твой_логин>/<имя_репозитория>.git
git push -u origin main
```

Проверь на странице репозитория на GitHub, что все файлы появились.

## 6. CI (GitHub Actions) — по желанию

Добавь `.github/workflows/tests.yml` с шагами: checkout → setup-java →
`./gradlew test`. После первого успешного прогона добавь бейджик сборки
в начало этого README:
```
![Build Status](https://github.com/<логин>/<репозиторий>/actions/workflows/tests.yml/badge.svg)
```

## Важное про баланс карт

SUT не перезапускается между тестами, поэтому баланс "10 000 ₽" из условия —
только стартовое значение. Тесты в этом проекте читают текущий баланс со
страницы перед действием и сверяют результат относительно него, а не
относительно захардкоженной суммы — так тесты остаются валидными при любом
порядке и количестве прогонов.
