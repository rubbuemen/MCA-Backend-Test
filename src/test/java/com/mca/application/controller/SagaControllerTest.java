package com.mca.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SagaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private static String URL = "/game/{gameId}/saga";

	@Test
	void testInput9() throws Exception {
		mockMvc.perform(get(URL, 9)).andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(jsonPath("$.[0].id", is("11"))).andExpect(jsonPath("$.[0].title", is("Relic Hunter: Curse of the Emerald Eye")))
				.andExpect(jsonPath("$.[0].price", is(39.99))).andExpect(jsonPath("$.[0].availability", is(false)))
				.andExpect(jsonPath("$.[1].id", is("15"))).andExpect(jsonPath("$.[1].title", is("Cosmic Empires: Twilight of the Overlords")))
				.andExpect(jsonPath("$.[1].price", is(49.99))).andExpect(jsonPath("$.[1].availability", is(true)))
				.andExpect(jsonPath("$.[2].id", is("19"))).andExpect(jsonPath("$.[2].title", is("Arcane Realms Online: Siege of the Sorcerer")))
				.andExpect(jsonPath("$.[2].price", is(29.99))).andExpect(jsonPath("$.[2].availability", is(true)))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testInput12() throws Exception {
		mockMvc.perform(get(URL, 12)).andExpect(status().is(HttpStatus.OK.value()))
				.andExpect(jsonPath("$.[0].id", is("20"))).andExpect(jsonPath("$.[0].title", is("Wasteland Chronicles: Edge of Survival")))
				.andExpect(jsonPath("$.[0].price", is(24.99))).andExpect(jsonPath("$.[0].availability", is(false)))
				.andExpect(jsonPath("$.[1].id", is("18"))).andExpect(jsonPath("$.[1].title", is("Eternal Battlefront: Rise of the Phoenix")))
				.andExpect(jsonPath("$.[1].price", is(59.99))).andExpect(jsonPath("$.[1].availability", is(true)))
				.andExpect(jsonPath("$.[2].id", is("19"))).andExpect(jsonPath("$.[2].title", is("Arcane Realms Online: Siege of the Sorcerer")))
				.andExpect(jsonPath("$.[2].price", is(29.99))).andExpect(jsonPath("$.[2].availability", is(true)))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testInput6() throws Exception {
		mockMvc.perform(get(URL, 6)).andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.code", is("Client exception")))
				.andExpect(jsonPath("$.description", is("There seems to be a problem connecting to the client providing the information, try again later or contact an administrator.")))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	void testInputBadPathVariable() throws Exception {
		mockMvc.perform(get(URL, "bad")).andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.code", is("Illegal argument exception")))
				.andExpect(jsonPath("$.description", is("The path variable is not a valid number ID.")))
				.andReturn().getResponse().getContentAsString();
	}

}