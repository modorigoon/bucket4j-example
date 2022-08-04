package me.modorigoon.bucket4jexample

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class Bucket4jExampleApplicationTests(private val mvc: MockMvc) {

	@Test
	fun `rate limiter test`() {
		for (i in 0..9) {
			mvc.perform(get("/greet")).andExpect(status().isOk)
		}
		mvc.perform(get("/greet")).andExpect(status().isTooManyRequests)
	}
}
