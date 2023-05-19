FROM openjdk:8-jdk-alpine

WORKDIR /app
COPY . /app
EXPOSE 8080

WORKDIR /app
RUN apk update && apk add --no-cache curl
RUN ./gradlew build --daemon

CMD java -jar /app/build/libs/UdemyCouponKtorServer-all.jar