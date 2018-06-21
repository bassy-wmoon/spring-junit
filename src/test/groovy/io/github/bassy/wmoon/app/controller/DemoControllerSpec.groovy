package io.github.bassy.wmoon.app.controller

import io.github.bassy.wmoon.app.model.User
import io.github.bassy.wmoon.app.service.DemoService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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

    def 'view名hogeが返却されること'() {
        expect:
        mockMvc.perform(get('/'))
                .andExpect(view().name('hoge'))
    }
    
    def 'view名demoが返却されること'() {
        expect:
        mockMvc.perform(get('/index'))
                .andExpect(view().name('demo'))
    }
    
    def 'view名demoへのリダイレクトが返却されること_ユーザ登録が呼び出されること'() {

        when: 'テスト実行'
        mockMvc.perform(post('/save')
                .param('name', 'Bob')
                .param('sex', 'MALE'))
                .andExpect(view().name('redirect:demo'))

        then:
        1 * demoService.saveUser(_) >> { User user ->
            assert user.name == 'Bob'
            assert user.sex == User.Sex.MALE
        }
    }

    def 'view名demoUsers返却されること_モデルにusersが設定されていること'() {

        when: 'テスト実行'
        mockMvc.perform(get('/show'))
                .andExpect(view().name('demoUsers'))
                .andExpect(model().attributeExists('users'))

        then: 'selectUsersメソッドの呼び出し検証'
        1 * demoService.selectUsers() >> new ArrayList<User>()
    }

    def 'view名demoUserが返却されること_モデルにuserが設定されていること'() {

        when: 'テスト実行'
        mockMvc.perform(get('/show/user/100'))
                .andExpect(view().name('demoUser'))
                .andExpect(model().attributeExists('user'))

        then: 'selectUserメソッドの呼び出し検証'
        1 * demoService.selectUser(_) >> new User()
    }
}
