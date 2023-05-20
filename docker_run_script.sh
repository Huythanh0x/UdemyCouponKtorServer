sudo docker network create  ktor-mysql-network
sudo docker network inspect ktor-mysql-network

sudo docker build -t huythanh0x/udemy_coupon_ktor_server:latest .

sudo docker run --name ktor-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d  mysql:latest
sudo docker  run --name udemy_coupon_ktor_server -p 8080:8080  --rm  -d huythanh0x/udemy_coupon_ktor_server:latest

sudo docker run ktor-mysql
sudo docker push huythanh0x/udemy_coupon_ktor_server:latest

docker network inspect ktor-mysql-network | grep -A3 '"Name": "ktor-mysql"' | awk -F'"' '/"IPv4Address":/ {print $4}' | cut -d'/' -f1

sudo docker network rm  ktor-mysql-network