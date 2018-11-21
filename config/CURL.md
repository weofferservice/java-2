#### Включаем в консоли Windows кодировку UTF-8
`chcp 65001`

#### getAll
`curl -v -X GET http://localhost:8080/java-2/rest/profile/meals --user user@yandex.ru:password`

#### getBetween
`curl -v -X GET http://localhost:8080/java-2/rest/profile/meals/filter -G -d startDate=2015-05-30 -d endDate=2015-05-31 -d startTime=12:00 -d endTime=14:00 --user user@yandex.ru:password`

#### update (description=Обновленный завтрак)
`curl -v -H "Content-Type: application/json" -X PUT http://localhost:8080/java-2/rest/profile/meals/100002 -d "{ \"description\": \"\u041E\u0431\u043D\u043E\u0432\u043B\u0435\u043D\u043D\u044B\u0439 \u0437\u0430\u0432\u0442\u0440\u0430\u043A\", \"calories\": 1985, \"dateTime\": \"1985-11-01T19:56\" }" --user user@yandex.ru:password`

#### get
`curl -v -X GET http://localhost:8080/java-2/rest/profile/meals/100002 --user user@yandex.ru:password`

#### delete
`curl -v -X DELETE http://localhost:8080/java-2/rest/profile/meals/100002 --user user@yandex.ru:password`
###### Проверка
`curl -v -X GET http://localhost:8080/java-2/rest/profile/meals --user user@yandex.ru:password`

#### createWithLocation (description=Новый ужин)
`curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/java-2/rest/profile/meals -d "{ \"description\": \"\u041d\u043e\u0432\u044b\u0439 \u0443\u0436\u0438\u043d\", \"calories\": 1986, \"dateTime\": \"1986-10-01T20:18\" }" --user user@yandex.ru:password`
###### Проверка
`curl -v -X GET http://localhost:8080/java-2/rest/profile/meals/100010 --user user@yandex.ru:password`
`curl -v -X GET http://localhost:8080/java-2/rest/profile/meals --user user@yandex.ru:password`

#### validate with errors
`curl -v -H "Content-Type: application/json" -X PUT http://localhost:8080/java-2/rest/profile/meals/100003 -d "{ \"dateTime\": \"2015-05-30T07:00\" }" --user user@yandex.ru:password`
`curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/java-2/rest/admin/users -d "{}" --user admin@gmail.com:admin`
