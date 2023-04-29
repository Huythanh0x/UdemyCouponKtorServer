FROM openjdk:8-jdk-alpine

WORKDIR /app
COPY . /app
EXPOSE 8080

WORKDIR /app
RUN ./gradlew build

CMD java -jar /app/build/libs/UdemyCouponKtorServer-all.jar