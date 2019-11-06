FROM openjdk:8
COPY target/com.epam.spring.itsubbotnik_Telegram_bot_2019-1.0-SNAPSHOT.jar /tmp
COPY questions.xml /tmp
WORKDIR /tmp
CMD java -jar com.epam.spring.itsubbotnik_Telegram_bot_2019-1.0-SNAPSHOT.jar