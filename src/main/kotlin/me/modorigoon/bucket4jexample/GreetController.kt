package me.modorigoon.bucket4jexample

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/greet")
class GreetController {

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun greet(): String {
        return "Welcome to the jungle!"
    }
}
