package furhatos.app.gettingstarted.nlu

import furhatos.nlu.*
import furhatos.nlu.TextGenerator
import furhatos.nlu.grammar.Grammar
import furhatos.nlu.kotlin.grammar
import furhatos.nlu.common.Number
import furhatos.nlu.common.Time
import furhatos.util.Language
import furhatos.records.GenericRecord
import java.time.LocalTime


// list of entities as one entity
class ListOfTopping : ListEntity<Toppings>()



// define topping entities
class Toppings : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("onion", "tomato", "mushroom", "bacon")
    }
}

//define place entities
class Place: EnumEntity(){
    override fun getEnum(lang: Language): List<String> {
        return listOf("home","office", "address")
    }

    override fun toText(lang: Language): String {
        return generate(lang,"to your $value")
    }

}

// user intent: ask what kind of toppings
class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I want to order a pizza",
            "I want a pizza",
            "What options do you have?",
            "What pizzas do you have?",
            "What are the alternatives?",
            "What do you have?",
            "What toppings you have?",
            "What kind of toppings you have?")
    }
}

// user intent: order pizza, open for inheritance
open class OrderPizza: Intent(), TextGenerator {
    var topping: ListOfTopping? = null
    var deliverTo: Place? = null
    var deliveryTime: Time? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I would like a pizza to my office at 3 pm",
            "I want to order a pizza with bacon and ham",
            "Deliver to my @deliverTo instead",
            "I want a pizza @topping",
            "I want it to @deliverTo",
            "Deliver it at @deliveryTime",
            "I want it at @deliveryTime",
            "I want to add @topping",
            "I also want @topping",
            "I want @topping",
            "I want it @deliveryTime @deliverTo",
            "I want a pizza with @topping",
            "I prefer @topping",
            "I would like @topping"

        )
    }

    override fun toText(lang: Language): String {
        return generate(lang, "[with $topping] [$deliverTo] [delivered $deliveryTime]")
    }

    override fun toString(): String {
        return toText()
    }
}


//user intent: add more order
class AddTopping: Intent(){
    var topping: ListOfTopping? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("@topping", "add @topping", "I also want @topping")
    }
}

//user intent: remove orders
class RemoveTopping: Intent() {
    var topping: ListOfTopping? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("I do not want @topping", "no @topping", "I want to remove @topping")
    }
}

//user intent: change topping
class ChangeTopping: Intent() {
    var topping: ListOfTopping? = null
    var oldtopping: Toppings? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to change @oldtopping to @topping", "change to @topping","change @oldtopping to @topping")
    }
}

//user intent: tell the place
class TellPlace: Intent(){
    var place: Place? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("@place", "I want it delivered to @place", "send it to @place", "to @place")
    }

}

//user intent: tell the time
class TellTime: Intent(){
    var time: Time? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf("@time", "at @time", "as soon as possible")
    }
}


