package com.wecp.library;

import com.wecp.library.controller.exception.UserNotSubscribedException;
import com.wecp.library.domain.Book;
import com.wecp.library.domain.Issue;
import com.wecp.library.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LibraryApplicationTests {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";

    @Autowired
    private MockMvc mockMvc;

    User createUserEntity() {
        User user = new User();
        user.setUsername(DEFAULT_NAME);
        return user;
    }

    Issue createIssueEntity() {
        Issue issue = new Issue();
        issue.setIssueDate(LocalDate.now().minusDays(25));
        issue.setReturnDate(LocalDate.now());
        issue.setFine(30);
        issue.setPeriod(20);
        return issue;
    }

    @Test
    @WithMockUser
    void testIssueBookWithoutSubscription() throws Exception {
        // create user
        User user = createUserEntity();
        MvcResult resultActions = mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user))).andExpect(status().isOk()).andReturn();
        User createdUser = (User) TestUtil.convertJsonToObject(resultActions.getResponse().getContentAsString(), User.class);

        //issueBook withoutSubscription
        Issue issueEntity = createIssueEntity();
        issueEntity.setUser(createdUser);
        mockMvc.perform(post("/api/v1/issue-book").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(issueEntity)))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotSubscribedException));
    }

    @Test
    @WithMockUser
    void renewUserSubscriptionAndIssueBook() throws Exception {
        // create user
        User user = createUserEntity();
        MvcResult resultActions = mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user))).andExpect(status().isOk()).andReturn();
        User createdUser = (User) TestUtil.convertJsonToObject(resultActions.getResponse().getContentAsString(), User.class);

        // renew user subscription
        mockMvc.perform(get("/api/v1/renew-user-subscription/{id}", createdUser.getId()).with(csrf())).andExpect(status().isOk());

        //issueBook withoutSubscription
        Issue issueEntity = createIssueEntity();
        issueEntity.setUser(createdUser);
        mockMvc.perform(post("/api/v1/issue-book").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(issueEntity)))
                .andExpect(status().isOk());
    }

    @Test
    void createUserShouldFailWithoutSecurity() throws Exception {
        // create user
        User user = createUserEntity();
        mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user))).andExpect(status().isUnauthorized());
    }
}
