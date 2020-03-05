package com.state.machine.statemachine

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.listener.StateMachineListener
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State
import java.util.*


enum class OrderStates {
    CREATED, APPROVED, INVOICED, CANCELLED, SHIPPED, DELIVERED
}

enum class OrderEvents {
    CONFIRMED_PAYMENT, INVOICE_ISSUED, CANCEL, SHIP, DELIVER
}

@Configuration
@EnableStateMachine
class OrderStateMachineTransitionByEventConfig : EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents>() {

    override fun configure(config: StateMachineConfigurationConfigurer<OrderStates, OrderEvents>) {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener())
    }

    override fun configure(states: StateMachineStateConfigurer<OrderStates, OrderEvents>) {
        states
                .withStates()
                .initial(OrderStates.CREATED)
                .states(EnumSet.allOf(OrderStates::class.java))
    }

    override fun configure(transitions: StateMachineTransitionConfigurer<OrderStates, OrderEvents>) {
        transitions
                .withExternal()
                .source(OrderStates.CREATED).target(OrderStates.APPROVED)
                .event(OrderEvents.CONFIRMED_PAYMENT).event(OrderEvents.CANCEL)
                .and().withExternal()
                .source(OrderStates.APPROVED).target(OrderStates.INVOICED)
                .event(OrderEvents.INVOICE_ISSUED)
                .and().withExternal()
                .source(OrderStates.APPROVED).target(OrderStates.CANCELLED)
                .event(OrderEvents.CANCEL)
                .and().withExternal()
                .source(OrderStates.INVOICED).target(OrderStates.SHIPPED)
                .event(OrderEvents.SHIP)
                .and().withExternal()
                .source(OrderStates.SHIPPED).target(OrderStates.DELIVERED)
                .event(OrderEvents.DELIVER)
    }

    @Bean
    fun listener(): StateMachineListener<OrderStates, OrderEvents> {
        return OrderMachineListenerAdapter()
    }
}

class OrderMachineListenerAdapter : StateMachineListenerAdapter<OrderStates, OrderEvents>() {
    override fun stateChanged(from: State<OrderStates, OrderEvents>, to: State<OrderStates, OrderEvents>) {
        println("OrderState change from " + from.getId() + " to " + to.getId())
    }

}
