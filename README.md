# Simple implementation of the furhat robot - taking the order from users

It can handle pizza order from users. When the robot is approached by a user,
it will greet the user and start the interaction. It is designed to offer topping options,
take user’s order, change user’s order, ask delivery time, ask delivery place, and final
summary.

## Conversation Flow
It consists of 6 states, 3 entities, and 6 intents.
![alt text](https://github.com/yuwenchen31/furhat_pizzaorder/blob/master/conversation_flow.jpg?raw=true)

## Scenarios
User performs the following sequence of intents: 1) ask what options they have, 2) order a pizza with mushroom, 3) add topping of tomato, 4) remove mushroom, 5)
tell place, 6) tell time, 7) confirm order.

