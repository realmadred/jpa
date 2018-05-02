package com.example.jpa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void contextLoads() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/sysUser/findById").param("id", "4")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":4,\"name\":\"OsvfUP\",\"age\":23,\"password\":\"LCwleq\"}"))
                .andReturn();
//        MockHttpServletResponse response = mvcResult.getResponse();
//        String contentAsString = response.getContentAsString();
//        System.out.println(contentAsString);
    }

}
