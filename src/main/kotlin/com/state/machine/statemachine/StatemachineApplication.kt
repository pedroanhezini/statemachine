package com.state.machine.statemachine

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.statemachine.StateMachine
import org.springframework.stereotype.Component

@SpringBootApplication
class StatemachineApplication

fun main(args: Array<String>) {
	runApplication<StatemachineApplication>(*args)
}

@Component
class Runner(private val stateMachine: StateMachine<OrderStates, OrderEvents>) : CommandLineRunner {
	override fun run(vararg args: String?) {
		println("Iniciando máquina de estados...")
		stateMachine.sendEvent(OrderEvents.CONFIRMED_PAYMENT)
		stateMachine.sendEvent(OrderEvents.INVOICE_ISSUED)
		stateMachine.sendEvent(OrderEvents.SHIP)
		stateMachine.sendEvent(OrderEvents.DELIVER)
		println("Máquina de estados finalizada")
	}
}