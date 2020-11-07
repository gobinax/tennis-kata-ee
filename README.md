Tennis kata is fun. Counting tennis point is an interesting problem with tricky corner case.

But how would you feel counting tennis point for Enterprise with **Spring boot**, a **database**, a **3 layer architecture**, **integration testing** and all the awesome stuff that your enterprise deserves because it is so serious !!

ENTERPRISE YEAH !!

User story 1 : create match
---------------------------
##### Query
`POST /tennis/match`
```json
{
  "player1": "Nadal",
  "player2": "Djokovic"
}
```

##### Result
```json
{
  "id": 1,
  "player1": "Nadal",
  "player2": "Djokovic"
}
```

User story 2 : show match score
-------------------------------
##### Query
`GET /tennis/match/{id}/score`

##### Result
```json
{
  "score":"Nadal-Djokovic\n5-3\nDeuce\n"
}
```

User story 3 : score points
--------------------------
##### Query
`PUT /tennis/match/{id}/score`
```json
["Nadal", "Djokovic", "Djokovic"]
```

##### Result
```json
{
  "score":"Nadal-Djokovic\n0-0\n15-30"
}
```