package furhatos.app.gettingstarted.flow

import furhatos.app.gettingstarted.nlu.OrderPizza
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User

// Associate an order to a user
val User.order by NullSafeUserDataDelegate { OrderPizza() }