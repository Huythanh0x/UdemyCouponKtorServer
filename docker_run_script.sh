sudo docker build -t huythanh0x/udemy_coupon_ktor_server:latest .

#sudo docker run --name ktor-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d  mysql:latest
docker network create ktor-network
docker run --name ktor-network-mysql --network ktor-network -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d  mysql:latest
sudo docker run --name ktor-udemy-container --network ktor-network -p 8080:8080 -d huythanh0x/udemy_coupon_ktor_server:latest

#sudo docker push huythanh0x/udemy_coupon_ktor_server:latest