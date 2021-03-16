package com.bigid.web.controllers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class PostRESTFulControllerTest {

	@Autowired
    private TestRestTemplate restTemplate;

    /*@Test
    public void test() {
        this.restTemplate.getForEntity(
            "/{username}/vehicle", String.class, "Phil");
    }*/

}
