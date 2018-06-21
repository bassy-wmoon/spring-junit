package io.github.bassy.wmoon.app.controller

import io.github.bassy.wmoon.app.model.User
import io.github.bassy.wmoon.app.service.DemoService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@Unroll
class DemoControllerSpec extends Specification {

    private MockMvc mockMvc

    private DemoService demoService

    def setup() {
        demoService = Mock()
        mockMvc = MockMvcBuilders.standaloneSetup(new DemoController(demoService)).build()
    }

    def 'GETリクエストでdemoUsers画面にユーザ一覧が表示されること'() {

        when: 'テスト実行'
        mockMvc.perform(get("/show"))
                .andExpect(view().name("demoUsers"))
                .andExpect(model().attributeExists("users"))

        then: 'selectUsersメソッドの呼び出し検証'
        1 * demoService.selectUsers() >> []
    }

    def 'ユーザID指定のGETリクエストでdemoUser画面にユーザが表示されること'() {

        when: 'テスト実行'
        mockMvc.perform(get("/show/user/100"))
                .andExpect(view().name("demoUser"))
                .andExpect(model().attributeExists("user"))

        then: 'selectUserメソッドの呼び出し検証'
        1 * demoService.selectUser(_) >> new User()
    }
}
