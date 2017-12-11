# First actors project

Use this link to view the code example developed during the course in weeks 43 to 48.

If you want to download the example and stay up to date feel free to clone the project.

## WebSocket API usage

Use a web socket test cient (e.g. "Simple WebSocket Client" browser add-on):

Open a connection to `ws://localhost:8087/ws/v1/hello`.

After that you can send text messages that get a standard reply. 

## Webservice API usage

* `GET` `http://localhost:8087/api/v1/hello`
  * returns: `hello world`
  
* `POST` `http://localhost:8087/api/v1/cart/{userName}` 
  * returns the provided username
  
* `POST` `http://localhost:8087/api/v1/cart/{userName}/item`
  * JSON body: `{ "toCart" : "userName", "item" : "item_to_by" }`
  * returns the provided JSON object

## Week 49 + 50

- Webservice API
- WebSocket API
- Remoting
- Routing
- Clustering

## Weeks 47 + 48

- Supervision
- Persistent actors

## Weeks 45 + 46

- Monads
- Futures are Monads
- Futures with Actors (when asking with ?)
- Exection contexts, dispatchers and timeouts
- for and for-yield with Monads and Actors

## Weeks 43 + 44

- Collections, Maps, and Tuples
- ? vs. !   ("ask" vs. "tell")
- Introduction of Futures
- Actor responding to messages (cf. SayHello message)
- First sketch of a shopping cart actor system
