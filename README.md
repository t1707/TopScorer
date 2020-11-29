## Summary

This project designs a Score Ranking system where you can post scores, get scores using scoreId or get history of scores
with various filters. 
This Project is developed using the below technical stack:

1. Java 8
2. Gradle 6.3
3. Spring 2.3
4. Spring Boot
5. MySQL 8.0
6. JUnit 5

This project provides the following APIs explained in the next section

## Setup
Before we move onto the apis part, it is required to have the following setup.
Java version >= 8
Docker >= 2

1. Run the mysql image
`docker-compose up -d`
more details about docker-compose are [here](https://docs.docker.com/compose/)

2. Check if mysql image is up
`docker ps`

response should be similar to the following
```
~/TopScorer » docker ps
 CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                               NAMES
 4e5e5h3tb8d5        mysql               "docker-entrypoint.s…"   19 hours ago        Up 19 hours         0.0.0.0:3306->3306/tcp, 33060/tcp   topscorer_mysql_1
```

3. Start the `TopScore` app
` ./gradlew bootRun`
we are using gradle wrapper to run our app but if you have `gradle` installed into your system then run the following command.
`gradle bootRun`

4. Health Check
Request the `/health` api to see if application is running
`curl http://localhost:8080/demo/health`
The above request should reply as `OK`.

* Now our application is running, and we can move onto the next steps.

5. Running Tests
To run the unit and integration tests, run the following commands
`cd /src/test`
`./run.sh`

## APIs 

### Create Score
This POST api creates a new score corresponding to the requested payload consisting `player`, `score` and `time` at `/createScore` endpoint.

* player: name of the player
* score: score acquired by that player
* time: registered time of that score

e.g.
`curl -XPOST http://localhost:8080/demo/createScore -d '{"player":"player1","score":1, "time":"2020-01-01 10:00:00"}'`.

The above request will respond with a JSON representation of a Score object(if success), as the following listing shows:

```json
{"id":1,"player":"player1","score":1, "time":"2020-01-01 10:00:00"}
```

### Get Score
This GET api respond the score corresponding to the provided `scoreID` at `/getScore/{id}` endpoint.

e.g.
`curl -XGET http://localhost:8080/demo/getScore/1`.

* id: the unique id of the score entry

ResponseBody will be in the form
```json
{"id":1,"player":"player1","score":1, "time":"2020-01-01 10:00:00"}
```

### Delete Score
This GET api deletes the score corresponding to the provided `scoreID` at `/deleteScore/{id}` endpoint.

* id: the unique id of the score entry

e.g.
`curl http://localhost:8080/demo/deleteScore/1`.
If `OK` then api will respond a `Success` message otherwise will throw an error message.

### Get Scores
This GET api returns the pagination of scores corresponding to the provided filter payload at `/getScores` endpoint.

* page: page number of the result set
* size: number of entries on the page
* sortId: key to sort the result set on the resultant page
* player: name of the player
* before: entries after this time will get discarded
* after: entries before this time will get discarded

```json
{
    "page": 0,
    "size": "3",
    "sortId": "score",
    "players": "player1,player2",
    "before": "2025-01-01 10:00:00",
    "after": "2000-01-01 10:00:00"
}
```

e.g.
`curl -XPOST http://localhost:8080/demo/getScores -d '{above payload}'`

ResponseBody will be in the form
```json
{
    "scores": {
        "content": [
            {
                "id": 11,
                "player": "player2",
                "score": 5,
                "time": "2015-1-30 00:00:00"
            },
            {
                "id": 9,
                "player": "player2",
                "score": 20,
                "time": "2020-1-30 00:00:00"
            },
            {
                "id": 8,
                "player": "player1",
                "score": 23,
                "time": "2021-1-30 00:00:00"
            }
        ],
        "pageable": {
            "sort": {
                "unsorted": false,
                "sorted": true,
                "empty": false
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 3,
            "paged": true,
            "unpaged": false
        },
        "totalPages": 2,
        "totalElements": 4,
        "last": false,
        "first": true,
        "numberOfElements": 3,
        "sort": {
            "unsorted": false,
            "sorted": true,
            "empty": false
        },
        "size": 3,
        "number": 0,
        "empty": false
    }
}
```

### Get History
This GET api returns the history(statistics) of all the scores corresponding to the provided `player` at `/getHistory` endpoint.

* player: name of the player


e.g.
`curl -XGET http://localhost:8080/demo/getHistory/{player}'`

ResponseBody will be in the form
```json
{
    "avgScore": 61.5,
    "scores": [
        {
            "id": 8,
            "player": "player1",
            "score": 23,
            "time": "2021-1-30 00:00:00"
        },
        {
            "id": 10,
            "player": "player1",
            "score": 100,
            "time": "2010-1-30 00:00:00"
        }
    ],
    "topScore": {
        "id": 10,
        "player": "player1",
        "score": 100,
        "time": "2010-1-30 00:00:00"
    },
    "lowScore": {
        "id": 8,
        "player": "player1",
        "score": 23,
        "time": "2021-1-30 00:00:00"
    }
}
```

### TODO
Current project focuses on K.I.S.S and DRY by taking the minimalist approach.
But there are few things which could be added to make the system readable and robust. 
1. Prepared Statement for SQL requests
2. Use Redis as database for faster response
3. Generate separate response class for each API