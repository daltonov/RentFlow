package com.rentflow.property.controller;

import com.rentflow.property.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.rentflow.common.exception.NotFoundException;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {
    @Test
    void returnsNotFoundWhenPropertyDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        when(propertyService.getById(id))
                .thenThrow(new NotFoundException("Property", id));

        mockMvc.perform(get("/api/properties/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(
                        "Property with id " + id + " was not found"
                ));
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropertyService propertyService;

    @Test
    void rejectsInvalidPropertyBeforeCallingService() throws Exception {
        mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": " ",
                                  "address": "Москва, Тверская 1",
                                  "maxGuests": 0,
                                  "defaultPrice": -100
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.maxGuests").exists())
                .andExpect(jsonPath("$.errors.defaultPrice").exists());

        verifyNoInteractions(propertyService);
    }
}