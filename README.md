# First actors project

Use this link to view the code example developed during the course in weeks 43 to 48.

If you want to download the example and stay up to date feel free to clone the project.

## Webservice API usage --> not working yet!
### curl (Terminal)
`curl -i -X GET http://localhost:8087/api/v1/cart`

`curl -i -X POST -H "Content-Type:application/json" http://localhost:8087/api/v1/cart -d '{"cartId":"lisa"}'`

`curl -i -X PUT -H "Content-Type:application/json" http://localhost:8087/api/v1/cart/lisa/item/socks -d '{"productId":"socks", "amount":12}'`

`curl -i -X DELETE http://localhost:8087/api/v1/cart/lisa/item/socks`
### http (Terminal)
see http://httpie.org
### RESTer (Browser Add-on)
Download and open add-on "RESTer" within Firefox or Chrome
### Postman (standalone tool)
see http://getpostman.com

## Week 49

- Webservice API
- Remoting
- Routing

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
