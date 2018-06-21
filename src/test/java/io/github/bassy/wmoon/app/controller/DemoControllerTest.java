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

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DemoService demoService;

    @Test
    public void view名hogeが返却されること() throws Exception {

        // テスト実行
        mockMvc.perform(get("/"))
                .andExpect(view().name("hoge"));
    }

    @Test
    public void view名demoが返却されること() throws Exception {

        // テスト実行
        mockMvc.perform(get("/index"))
                .andExpect(view().name("demo"));
    }

    @Test
    public void view名demoへのリダイレクトが返却されること_ユーザ登録が呼び出されること() throws Exception {

        // saveUserメソッドをモック
        doNothing().when(demoService).saveUser(any());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // テスト実行
        mockMvc.perform(post("/save")
                .param("name", "Bob")
                .param("sex", "MALE"))
                .andExpect(view().name("redirect:demo"));

        // saveUserメソッドの呼び出しと引数を検証
        verify(demoService, times(1)).saveUser(any());
        verify(demoService).saveUser(userArgumentCaptor.capture());
        assert userArgumentCaptor.getValue().getName().equals("Bob");
        assert userArgumentCaptor.getValue().getSex().equals(User.Sex.MALE);
    }

    @Test
    public void view名demoUsers返却されること_モデルにusersが設定されていること() throws Exception{

        // サービスの戻り値のモック
        doReturn(new ArrayList<User>()).when(demoService).selectUsers();

        // テスト実行
        mockMvc.perform(get("/show"))
                .andExpect(view().name("demoUsers"))
                .andExpect(model().attributeExists("users"));

        // selectUsersメソッドの呼び出しと引数を検証
        verify(demoService, times(1)).selectUsers();
    }

    @Test
    public void view名demoUserが返却されること_モデルにuserが設定されていること() throws Exception {

        // サービスの戻り値のモック
        doReturn(new User()).when(demoService).selectUser(anyInt());

        // テスト実行
        mockMvc.perform(get("/show/user/100"))
                .andExpect(view().name("demoUser"))
                .andExpect(model().attributeExists("user"));

        // selectUserメソッドの呼び出しと引数を検証
        verify(demoService, times(1)).selectUser(anyInt());
    }
}