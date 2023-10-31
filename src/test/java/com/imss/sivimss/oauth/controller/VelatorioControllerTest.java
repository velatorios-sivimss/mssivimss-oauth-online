package com.imss.sivimss.oauth.controller;

import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.imss.sivimss.oauth.base.BaseTest;
import com.imss.sivimss.oauth.util.JsonUtil;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithMockUser(username="10796223", password="123456",roles = "ADMIN")
public class VelatorioControllerTest extends BaseTest {

	 //@Autowired
	 //private JwtTokenProvider jwtTokenProvider;
	
	@Test
    @DisplayName("buscar Panteon")
    @Order(1)
    public void buscarPanteonOK() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String myToken="";
        //MockModCatalogosClient.buscarPanteon(HttpStatusCode.OK_200, JsonUtil.readFromJson("json/request/buscar_panteon_mock.json"), JsonUtil.readFromJson("json/response/response_buscar_panteon_mock.json"), myToken, mockServer);
        this.mockMvc.perform(post("/panteones/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + myToken)
                        .content(JsonUtil.readFromJson("json/request/buscar_panteon_controller.json"))
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
        ;
    }

}
