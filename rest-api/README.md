## Development Preparation
### Run database engine
$ docker run -d --name mysql -p 3306:3306 -e "MYSQL_ROOT_PASSWORD=123456" mysql:latest
#### Create database and user for application
$ docker exec -it mysql bash
$ mysql -u root -p
mysql> create database tedtalk;
mysql> create user 'tedtalk'@'%' identified by '123456';
mysql> grant all on tedtalk.* to 'tedtalk'@'%';


## Running Application
### Create Jar File
$./gradlew build
### Create docker image
$docker build -t rest-api:latest .
### Run docker-compose
$docker-compose up -d 
### Access to application
Then you can point application by url : **localhost:8080**

## APIs
###1.SignIn 
In order to call protected apis, you should first login and take JWT token and send it in header as Authorization
#### url -> /open/1.0/signin
#### Example of signin by admin credential
curl --location --request POST 'localhost:8080/open/1.0/signin' \
--header 'Content-Type: application/json' \
--data-raw '{
"username":"admin",
"password":"password"
}'

###2.Search for an exists talk by title|author|views|likes
#### url -> /api/1.0/talks[?title=&author=&views=&likes=]
#### Search for a talk with title contains "The gentle genius"
curl --location --request GET 'localhost:8080/api/1.0/talks?title=The gentle genius' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjY1OTcxODc1LCJleHAiOjE2NjU5NzI0NzQsImlzcyI6IlRlZFRhbGsiLCJuYmYiOjE2NjU5NzE4NzUsImp0aSI6InRlZHRhbGstMTY2NTk3MTg3NTAxMS05ZTcyMWQxZS05OTUyLTQ4YWQtYTcwMC01ZjQ4Mzc2Mjg1OWMiLCJyb2xlcyI6IlJPTEVfQURNSU4ifQ.Aobb3PhfGuUlr4VDjglMjNKVjhb-sHHLLWHzegAmNBU'

#### Search for a talk with author contains "The gentle genius"
curl --location --request GET 'localhost:8080/api/1.0/talks?author=Susan' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjY1OTcxODc1LCJleHAiOjE2NjU5NzI0NzQsImlzcyI6IlRlZFRhbGsiLCJuYmYiOjE2NjU5NzE4NzUsImp0aSI6InRlZHRhbGstMTY2NTk3MTg3NTAxMS05ZTcyMWQxZS05OTUyLTQ4YWQtYTcwMC01ZjQ4Mzc2Mjg1OWMiLCJyb2xlcyI6IlJPTEVfQURNSU4ifQ.Aobb3PhfGuUlr4VDjglMjNKVjhb-sHHLLWHzegAmNBU'

###3.Get details of specific talk by id
#### url -> /api/1.0/talks/{id}
#### Example of Getting talk detail of id = 5368
curl --location --request GET 'localhost:8080/api/1.0/talks/5368' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjY1OTcxODc1LCJleHAiOjE2NjU5NzI0NzQsImlzcyI6IlRlZFRhbGsiLCJuYmYiOjE2NjU5NzE4NzUsImp0aSI6InRlZHRhbGstMTY2NTk3MTg3NTAxMS05ZTcyMWQxZS05OTUyLTQ4YWQtYTcwMC01ZjQ4Mzc2Mjg1OWMiLCJyb2xlcyI6IlJPTEVfQURNSU4ifQ.Aobb3PhfGuUlr4VDjglMjNKVjhb-sHHLLWHzegAmNBU'

###4.Create new talk
#### url -> /api/1.0/talks
#### Example of creating new ted talk
curl --location --request POST 'localhost:8080/api/1.0/talks' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjY1OTcxODc1LCJleHAiOjE2NjU5NzI0NzQsImlzcyI6IlRlZFRhbGsiLCJuYmYiOjE2NjU5NzE4NzUsImp0aSI6InRlZHRhbGstMTY2NTk3MTg3NTAxMS05ZTcyMWQxZS05OTUyLTQ4YWQtYTcwMC01ZjQ4Mzc2Mjg1OWMiLCJyb2xlcyI6IlJPTEVfQURNSU4ifQ.Aobb3PhfGuUlr4VDjglMjNKVjhb-sHHLLWHzegAmNBU' \
--data-raw '{
"title":"Why people and AI make good business partners",
"author":"Shervin Khodabandeh",
"scheduleDate":"Oct 20",
"link":"https://www.ted.com/talks/shervin_khodabandeh_why_people_and_ai_make_good_business_partners"
}'

###5.Delete and exist talk
#### url -> /api/1.0/talks/{id}
#### Example of deleting talk with id = 5444
curl --location --request DELETE 'localhost:8080/api/1.0/talks/5444' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjY1OTcxODc1LCJleHAiOjE2NjU5NzI0NzQsImlzcyI6IlRlZFRhbGsiLCJuYmYiOjE2NjU5NzE4NzUsImp0aSI6InRlZHRhbGstMTY2NTk3MTg3NTAxMS05ZTcyMWQxZS05OTUyLTQ4YWQtYTcwMC01ZjQ4Mzc2Mjg1OWMiLCJyb2xlcyI6IlJPTEVfQURNSU4ifQ.Aobb3PhfGuUlr4VDjglMjNKVjhb-sHHLLWHzegAmNBU'

###5.Update and exist talk
#### url -> /api/1.0/talks/{id}
#### Example of updating title of talk with id = 5445 
curl --location --request PUT 'localhost:8080/api/1.0/talks/5445' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjY1OTcxODc1LCJleHAiOjE2NjU5NzI0NzQsImlzcyI6IlRlZFRhbGsiLCJuYmYiOjE2NjU5NzE4NzUsImp0aSI6InRlZHRhbGstMTY2NTk3MTg3NTAxMS05ZTcyMWQxZS05OTUyLTQ4YWQtYTcwMC01ZjQ4Mzc2Mjg1OWMiLCJyb2xlcyI6IlJPTEVfQURNSU4ifQ.Aobb3PhfGuUlr4VDjglMjNKVjhb-sHHLLWHzegAmNBU' \
--data-raw '{
"title":"Why people and AI make good business partners!"
}'