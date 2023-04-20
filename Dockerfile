FROM openjdk:8-jdk-alpine
RUN apk update && apk add --no-cache openvpn
RUN apk --no-cache add curl

WORKDIR /app
COPY . /app
EXPOSE 8080

COPY configure.ovpn /etc/openvpn/
COPY ./mycredentials.txt /etc/openvpn/

WORKDIR /app
CMD ./gradlew build

CMD apt-get install -y openvpn-systemd-resolved
CMD modprobe tun


CMD openvpn --config /etc/openvpn/configure.ovpn --auth-user-pass /etc/openvpn/mycredentials.txt & sleep 10; java -jar /app/build/libs/UdemyCouponKtorServer-all.jar