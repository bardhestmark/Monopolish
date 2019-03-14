package com.teamfour.monopolish.account;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;

public class AccountTest {
    private Account instance = null;

    @BeforeAll
    public void setInstance() {
        LocalDate date = LocalDate.of(2019, Month.DECEMBER, 24);
        instance = new Account("johhnyBoi23", "johhny@gmail.com", date);
    }

    @AfterAll
    public void tearDownAll() {
        instance = null;
    }

    @Test
    //@DisplayName("Username")
    public void getUsername() {
        String result = instance.getUsername();
        String expected = "johhnyBoi23";
        assertEquals(result, expected);
    }

    @Test
    @DisplayName("Email")
    public void getEmail() {
        String result = instance.getEmail();
        String expected = "johhny@gmail.com";
        assertEquals(result, expected);
    }

    @Test
    @DisplayName("Date")
    public void getDate() {
        LocalDate result = instance.getRegDate();
        LocalDate expected = LocalDate.of(2019, Month.DECEMBER, 24);
        assertEquals(result, expected);
    }
}
