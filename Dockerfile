FROM openjdk:8-jdk
EXPOSE 8080
RUN mkdir /app
COPY . /app
WORKDIR /app
RUN ./gradlew installDist
CMD ["java", "-jar", "/app/build/libs/UdemyCouponKtorServer-all.jar"]