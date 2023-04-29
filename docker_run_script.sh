sudo docker build -t huythanh0x/udemy_coupon_ktor_server:v1 .
sudo docker run -p 8080:8080 --cap-add=NET_ADMIN --device=/dev/net/tun huythanh0x/udemy_coupon_ktor_server:v1

#sudo docker push huythanh0x/udemy_coupon_ktor_server:v1