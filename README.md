Ktor server for Udemy coupons
============

The project is a Ktor server that crawls coupons from various websites and validates coupons by sending data to the
official Udemy API. It provides several API endpoints, including fetching all or a specified number of data, searching
for coupons by keyword, and more. The project also includes a Docker Hub link for easy build and deployment.

### List websites

- <https://jobs.e-next.in/course/udemy>
- <https://www.real.discount/>

### Technologies Used

- [x] Kotlin
- [x] Ktor
- [x] Jsoup
- [ ] Database
- [x] Docker

### Build from the source

**Prerequisites:** * Java 11 or higher

1. Clone the project repository using `git clone https://github.com/Huythanh0x/UdemyCouponKtorServer/tree/master`
2. Run the project using `./gradlew run`

Then all the data will be available at <http://0.0.0.0:8080/>

### Deployment

The project is Dockerized and can be deployed to any platform that supports Docker. You can use the Docker Hub link to
build and deploy the project easily. Check at [Docker hub](https://hub.docker.com/repository/docker/huythanh0x/udemy_coupon_ktor_server/general)

You can also check the live server [http://huythanh0x.ddns.net](http://huythanh0x.ddns.net:8080)

### API Endpoints

#### Sample response

<details>
    <summary> Click to show hidden info</summary>
    <pre style="background-color: #f0f0f0;">
    {
      "localTime": "2023-05-13T04:07:21.789",
      "coupons": [
        {
          "subCategory": "Unknown",
          "level": "Beginner",
          "heading": "Learn how to use the powerful Python pandas library to analyze and manipulate data.",
          "author": "Hassan Shoayb",
          "expiredDate": "2023-05-17 05:23:00+00:00",
          "rating": 4.72846,
          "students": 10023,
          "couponUrl": "https://www.udemy.com/course/complete-pandas-for-absolute-beginners/?couponCode=DA906AA9D2D1A450AB14",
          "description": "<p> A really long description in HTML code</p> ",
          "language": "English",
          "title": "Complete Pandas for Absolute Beginners 2023",
          "previewVideo": "/course/5097114/preview/?startPreviewId=46190992",
          "reviews": 34,
          "usesRemaining": 97,
          "contentLength": 59,
          "category": "Development",
          "courseId": 5097114,
          "couponCode": "DA906AA9D2D1A450AB14",
          "previewImage": "https://img-b.udemycdn.com/course/750x422/5097114_5ee1_2.jpg"
        }
      ],
      "ipAddress": "115.72.83.11"
    }
    </pre>
</details>

#### Fetch all data

`GET http://0.0.0.0:8080/fetch/all`

Fetches all the coupon data from the websites.

#### Fetch n data

`GET http://0.0.0.0:8080/fetch/{count}`

Fetches a specified number of coupon data from the websites.

#### Search by keyword

`GET http://0.0.0.0:8080/search/python`

Searches for coupon data containing the specified keyword in the title.


Contributing
------------

Contributions to the project are welcome. You can submit bug reports, feature requests, and pull requests on the
project's GitHub repository.

License
-------

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
