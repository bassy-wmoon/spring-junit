package io.github.bassy.wmoon.app.controller;

import io.github.bassy.wmoon.app.model.User;
import io.github.bassy.wmoon.app.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DemoService demoService;

    @Test
    public void GETリクエストでhoge画面が表示されること() throws Exception {

        // テスト実行
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(view().name("hoge"))
                .andExpect(status().isOk())

                // viewに文字列が含まれていることを検証
                .andExpect(content().string(containsString("hello world")));
    }

    @Test
    public void GETリクエストでdemo画面が表示されること() throws Exception {

        // テスト実行
        mockMvc.perform(MockMvcRequestBuilders.get("/demo"))
                .andExpect(view().name("demo"))
                .andExpect(status().isOk())

                // viewに要素と文字列が存在することを検証
                .andExpect(xpath("/html/head/title").string("demo dayo"))
                .andExpect(xpath("//h1").string("Demo page"));
    }

    @Test
    public void POSTリクエストでユーザ登録後demo画面にリダイレクトできること() throws Exception {

        // saveUserメソッドをモック
        doNothing().when(demoService).saveUser(any());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // テスト実行
        mockMvc.perform(MockMvcRequestBuilders
                .post("/save")
                .param("name", "Bob")
                .param("sex", "MALE"))
                .andExpect(view().name("redirect:demo"))
                .andExpect(status().is(302));

        // saveUserメソッドの呼び出しと引数を検証
        verify(demoService, times(1)).saveUser(any());
        verify(demoService).saveUser(userArgumentCaptor.capture());
        assert userArgumentCaptor.getValue().getName().equals("Bob");
        assert userArgumentCaptor.getValue().getSex().equals(User.Sex.MALE);
    }

    @Test
    public void GETリクエストでdemoUsers画面にユーザ一覧が表示されること() throws Exception{

        // サービスの戻り値を作成
        User bob = new User();
        bob.setId(1);
        bob.setName("Bob");
        bob.setSex(User.Sex.MALE);
        User marry = new User();
        marry.setId(2);
        marry.setName("Marry");
        marry.setSex(User.Sex.FEMALE);

        List<User> users = new ArrayList<>();
        users.add(bob);
        users.add(marry);

        // メソッドをモック
        doReturn(users).when(demoService).selectUsers();

        // テスト実行
        mockMvc.perform(MockMvcRequestBuilders
                .get("/show"))
                .andExpect(view().name("demoUsers"))
                .andExpect(model().attributeExists("users"))
                .andExpect(xpath("//h1").string("Demo Users"))
                .andExpect(xpath("//div[1]/span[2]").string("Bob"))
                .andExpect(xpath("//div[2]/span[2]").string("Marry"));

        // selectUsersメソッドの呼び出しと引数を検証
        verify(demoService, times(1)).selectUsers();
    }

    @Test
    public void ユーザID指定のGETリクエストでdemoUser画面にユーザが表示されること() throws Exception {

        // サービスの戻り値を作成
        User mike = new User();
        mike.setId(100);
        mike.setName("Mike");
        mike.setSex(User.Sex.MALE);

        // メソッドをモック
        doReturn(mike).when(demoService).selectUser(anyInt());


        // テスト実行
        mockMvc.perform(MockMvcRequestBuilders
                .get("/show/user/100"))
                .andExpect(view().name("demoUser"))
                .andExpect(model().attributeExists("user"))
                .andExpect(xpath("//h1").string("Demo User"))
                .andExpect(xpath("//div/span[1]").string("100"))
                .andExpect(xpath("//div/span[2]").string("Mike"));

        // selectUserメソッドの呼び出しと引数を検証
        verify(demoService, times(1)).selectUser(anyInt());
    }
}