# Практические работы по Kotlin / Android

Проектирование и разработка мобильных приложений на языке Котлин.
Мазенин Артем Андреевич, ИКБО-63-23.

12 отдельных приложений в одном Gradle-проекте. Каждая работа находится в своей папке; общие файлы сборки лежат в корне. Деление на модули 1–4, 5–8, 9–12 принято для итоговых отчетов.

## Состав

| Модуль | Работа | Каталог | Тема |
|---|---|---|---|
| 1 | 1 | [practice-01](practice-01/README.md) | Учет личных расходов |
| 1 | 2 | [practice-02](practice-02/README.md) | Навигация между тремя фрагментами |
| 1 | 3 | [practice-03](practice-03/README.md) | Разделение приложения на слои и MVVM |
| 1 | 4 | [practice-04](practice-04/README.md) | Камера и хронологический журнал снимков |
| 2 | 5 | [practice-05](practice-05/README.md) | Загрузка каталога через Retrofit и хранение в SQLite |
| 2 | 6 | [practice-06](practice-06/README.md) | Внедрение зависимостей с помощью Koin |
| 2 | 7 | [practice-07](practice-07/README.md) | Загрузка изображения в потоках Network и Disk |
| 2 | 8 | [practice-08](practice-08/README.md) | Модульное и интерфейсное тестирование загрузчика |
| 3 | 9 | [practice-09](practice-09/README.md) | Персональная тема Jetpack Compose |
| 3 | 10 | [practice-10](practice-10/README.md) | Перенос загрузчика изображений на Compose |
| 3 | 11 | [practice-11](practice-11/README.md) | Scaffold и две панели переходов |
| 3 | 12 | [practice-12](practice-12/README.md) | Граф навигации и фоновый индекс изображений |

## Открытие в Android Studio

1. Скачайте или клонируйте весь репозиторий. Для Windows рекомендуется короткий путь латиницей, например `C:/dev/kotlin-practices`.
2. Откройте корень с `settings.gradle.kts`, а не отдельную practice-папку.
3. Укажите Gradle JDK 21. Установите Android SDK Platform 35 и Build Tools через SDK Manager. Gradle 9.1.0 загружается Wrapper автоматически; AGP 8.13.2, Kotlin 2.2.21. Для загрузки зависимостей нужен интернет.
4. Studio обычно создает `local.properties` с путем к SDK. Если этого не произошло, создайте его локально: `sdk.dir=C:/Users/your-name/AppData/Local/Android/Sdk`. Не загружайте этот файл в GitHub.
5. После Gradle Sync выберите конфигурацию Android App для модуля `pr01` ... `pr12` и устройство Android API 26+. Если конфигурация не появилась автоматически, создайте ее через Run > Edit Configurations > Android App.

## Сборка

В PowerShell из корня:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat :pr08:testDebugUnitTest
.\gradlew.bat :pr08:connectedDebugAndroidTest
```

Последняя команда требует работающего эмулятора или подключенного устройства. `verify.ps1` выполняет сборку и локальные тесты, используя ваше окружение Java/SDK. Для терминала задайте JAVA_HOME на JDK 21.

Linux/macOS: `chmod +x gradlew`, затем `./gradlew assembleDebug`. На этих ОС пакет отдельно не проверялся.

Результаты модулей вынесены в `${java.io.tmpdir}/KotlinPracticesGitHub/prXX`, чтобы избежать проблем Windows с кириллицей. APK находится в `prXX/outputs/apk/debug/`. Можно задать свой абсолютный каталог латиницей через переменную `KOTLIN_BUILD_DIR`. Для разных копий проекта используйте разные каталоги.

## Демонстрация

Инструкции и снимки экрана находятся в README каждой работы. Для 5–6 нужен доступ к DummyJSON; для 7–8 и 10–12 используется HTTPS PNG/JPEG (пример: https://httpbin.org/image/png). Доступность внешних сервисов может меняться. Сохраненные файлы и SQLite доступны без сети. В 4 разрешите камеру

## Как загрузить на GitHub

Создайте пустой репозиторий в своем аккаунте. Загрузите **все содержимое этой папки**, включая Gradle Wrapper, `gradle`, `settings.gradle.kts`, `.gitignore` и 12 папок работ. Не загружайте только отдельную practice-папку: ей нужны общие настройки. Удобнее использовать Git или GitHub Desktop, чтобы сохранить вложенные каталоги и скрытые файлы.

Пример команд (замените OWNER и REPOSITORY своими значениями):

```sh
git init
git add .
git commit -m "Add 12 Kotlin Android practices"
git branch -M main
git remote add origin https://github.com/OWNER/REPOSITORY.git
git push -u origin main
```
