# Используем официальный образ с OpenJDK
FROM openjdk:21-jdk-slim AS build

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven

# Создаем рабочую директорию для проекта
WORKDIR /app

# Копируем pom.xml и зависимости для сборки проекта
COPY pom.xml .

# Скачиваем зависимости Maven (для кэширования слоев сборки)
RUN mvn dependency:go-offline

# Копируем все исходные файлы проекта в контейнер
COPY src /app/src

# Сборка JAR файла
RUN mvn clean package -DskipTests

# Устанавливаем образ для запуска приложения
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из предыдущего этапа
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт для приложения
EXPOSE 8888

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
