# lbg-alert-service

## Build and Run 
mvn clean install
java -jar lbg-alert-service-0.0.1-SNAPSHOT.jar

## h2 console
http://localhost:8081/h2-console
JDBS URL: jdbc:h2:mem:alert_db
sa/password

## Swagger
http://localhost:8081/swagger-ui/index.html#/

## Actuator
http://localhost:8081/actuator/health


## curl cmds
curl -X 'GET' \
'http://localhost:8081/api/alerts?status=NEW&riskBand=HIGH&alertType=STRUCTURING&amountMin=1000&amountMax=1000000&page=0&size=20&sortBy=triggeredAt&sortDir=desc' \
-H 'accept: */*'


curl -X 'GET' \
'http://localhost:8081/api/alerts/ALT-00001' \
-H 'accept: */*'


curl -X 'GET' \
'http://localhost:8081/api/alerts/summary?groupBy=status' \
-H 'accept: */*'

curl -X 'PATCH' \
'http://localhost:8081/api/alerts/ALT-00001/status' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"status": "UNDER_REVIEW"
}'

curl -X 'POST' \
'http://localhost:8081/api/alerts' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"customerId": "CUST-1042",
"alertType": "STRUCTURING",
"riskBand": "HIGH",
"amount": 10000.01,
"currency": "GBP",
"triggeredAt": "2026-04-18T11:35:06.843Z",
"flaggedRules": [
"CASH_THRESHOLD"
],
"assignedAnalyst": "ravi"
}'

  