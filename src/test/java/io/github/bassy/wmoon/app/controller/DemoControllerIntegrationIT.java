package io.github.bassy.wmoon.app.controller;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class DemoControllerIntegrationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() {
        new DbSetup(new DataSourceDestination(dataSource), Operations.truncate("user")).launch();
    }

    @Test
    public void GETリクエストでhoge画面が表示されること() throws Exception {

        // テスト実行
        mockMvc.perform(get("/"))
                .andExpect(view().name("hoge"))
                .andExpect(status().isOk())

                // viewに文字列が含まれていることを検証
                .andExpect(content().string(containsString("hello world")));
    }

    @Test
    public void GETリクエストでdemo画面が表示されること() throws Exception {

        // テスト実行
        mockMvc.perform(get("/index"))
                .andExpect(view().name("demo"))
                .andExpect(status().isOk())

                // viewに要素と文字列が存在することを検証
                .andExpect(xpath("/html/head/title").string("demo dayo"))
                .andExpect(xpath("//h1").string("Demo page"));
    }

    @Test
    public void POSTリクエストでユーザ登録後demo画面にリダイレクトできること() throws Exception {

        // テスト実行
        mockMvc.perform(post("/save")
                .param("name", "Bob")
                .param("sex", "MALE"))
                .andExpect(view().name("redirect:demo"))
                .andExpect(status().is(302));

        // レコード件数の確認
        int count = JdbcTestUtils.countRowsInTableWhere(
                new JdbcTemplate(dataSource),
                "user",
                "name = 'Bob' and sex = 'MALE'"
        );
        assert count == 1;
    }

    @Test
    public void GETリクエストでdemoUsers画面にユーザ一覧が表示されること() throws Exception {

        // テストデータ投入
        Operation insert = sequenceOf(
                insertInto("user")
                        .columns("id", "name", "sex")
                        .values(10, "Bob", "MALE")
                        .values(20, "Marry", "FEMALE")
                        .build()
        );
        new DbSetup(new DataSourceDestination(dataSource), insert).launch();

        // テスト実行
        mockMvc.perform(get("/show"))
                .andExpect(view().name("demoUsers"))
                .andExpect(status().isOk())
                .andExpect(xpath("//h1").string("Demo Users"))
                .andExpect(xpath("//div[1]/span[2]").string("Bob"))
                .andExpect(xpath("//div[2]/span[2]").string("Marry"));
    }


    @Test
    public void ユーザID指定のGETリクエストでdemoUser画面にユーザが表示されること() throws Exception {

        // テストデータ投入
        Operation insert = sequenceOf(
                insertInto("user")
                        .columns("id", "name", "sex")
                        .values(100, "Mike", "MALE")
                        .build()
        );
        new DbSetup(new DataSourceDestination(dataSource), insert).launch();

        // テスト実行
        mockMvc.perform(get("/show/user/100"))
                .andExpect(view().name("demoUser"))
                .andExpect(xpath("//h1").string("Demo User"))
                .andExpect(xpath("//div/span[1]").string("100"))
                .andExpect(xpath("//div/span[2]").string("Mike"));
    }
}