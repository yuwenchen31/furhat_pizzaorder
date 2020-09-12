package furhatos.app.gettingstarted.flow


import furhatos.flow.kotlin.*
import furhatos.nlu.common.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Number
import furhatos.nlu.common.Time
import furhatos.nlu.common.Yes
import furhatos.util.Language
import furhatos.app.gettingstarted.nlu.*
import furhatos.app.gettingstarted.*
import furhatos.gestures.Gestures
import furhatos.nlu.ListEntity


// Greet the users
val Start: State = state(Interaction) {
    onEntry {
        furhat.gesture(Gestures.Smile)
        furhat.say("Hi! Welcome to the pizza house!")
        goto(TakingOrder)
    }
}


val TakingOrder: State = state(Interaction){

    onEntry {
        random(
            { furhat.ask("How can I help?") },
            { furhat.ask("Which pizza do you want?") }
        )
    }

    onResponse<No> {
        furhat.say("That is fine. Hope you have a nice day!")
        goto(Idle)
    }

    onResponse<OrderPizza> {
        furhat.say("Ok, you want a pizza ${it.intent.topping}")
        users.current.order.adjoin(it.intent)
        goto(MoreOrder)

    }

    onResponse<RequestOptions> {
        furhat.say("We have ${Toppings().optionsToText()}")
        furhat.ask("Which one would you prefer?")
    }
}


val MoreOrder: State = state(Interaction){

    onEntry{
        furhat.ask("Do you want something else?")
    }

    onResponse<RequestOptions> {
        furhat.say("We have ${Toppings().optionsToText()}")
        furhat.ask("Which one would you prefer?")
    }

    onResponse<AddTopping>{
        furhat.say("Okay, adding ${it.intent.topping}")
        users.current.order.adjoin(it.intent)
        // users.current.order.topping = it.intent.topping
        reentry()
    }

    onResponse<RemoveTopping>{
        furhat.say("Okay, we remove ${it.intent.topping}.")
        users.current.order.topping?.removeFromList(it.intent?.topping!!)
        reentry()
    }

    onResponse<ChangeTopping>{

        users.current.order.topping?.removeFromList(it.intent?.topping!!)
        users.current.order.adjoin(it.intent)

        furhat.say("Okay, change to ${it.intent.topping}.")
        furhat.say("Current orders are ${users.current.order.topping}")

        furhat.say("Old topping is ${it.intent.oldtopping}")



        reentry()
    }


    onResponse<No>{
        goto(OrderInfo)
    }

}


val OrderInfo: State = state(parent= Interaction){

    onEntry {

        val order = users.current.order

        when {
            order.deliverTo == null -> goto(AskAddress)
            order.deliveryTime == null -> goto(AskTime)

            else -> {
                furhat.ask("Thank you for your order! We will deliver pizza with ${order.topping} ${order.deliverTo} at ${order.deliveryTime}, is that correct?")
            }
        }
    }

    onResponse<No> {
        goto(ChangeOrder)
    }

    onResponse<Yes> {
        furhat.say("Thank you! Your order will be delivered as soon as possible.")
        goto(Idle)
    }

}

val ChangeOrder: State = state(parent= Interaction){
    onEntry{
        furhat.ask("I am sorry! What would you like to change?")
    }

    onResponse<AddTopping> {
        // users.current.order.topping = it.intent.topping
        users.current.order.topping?.adjoin(it.intent)
        furhat.say("Okay, adding ${it.intent.topping}")
        goto(OrderInfo)

    }

    onResponse<No> {
        furhat.say("Great!")
        goto(OrderInfo)

    }

    onResponse<RemoveTopping>{
        users.current.order.topping?.removeFromList(it.intent?.topping!!)
        furhat.say("Okay, we remove ${it.intent?.topping}.")
        goto(OrderInfo)
    }


}


val AskAddress: State = state {
    onEntry{
        furhat.ask("May I know where you want to deliver?")
    }
    onResponse<TellPlace> {
        users.current.order.deliverTo = it.intent.place
        furhat.say("Okay! We will deliver it ${it.intent.place}")
        goto(OrderInfo)

    }

}

val AskTime: State = state {
    onEntry{
        furhat.ask("May I know when you want to deliver?")
    }
    onResponse<TellTime> {
        users.current.order.deliveryTime = it.intent.time
        furhat.say("Okay! We will deliver it at ${it.intent.time}")
        goto(OrderInfo)

    }

}