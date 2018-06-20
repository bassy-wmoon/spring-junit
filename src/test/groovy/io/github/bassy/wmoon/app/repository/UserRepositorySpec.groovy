package io.github.bassy.wmoon.app.repository

import com.ninja_squad.dbsetup.DbSetup
import com.ninja_squad.dbsetup.Operations
import com.ninja_squad.dbsetup.destination.Destination
import groovy.sql.Sql
import io.github.bassy.wmoon.app.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
@Unroll
class UserRepositorySpec extends Specification {

    @Autowired
    private Destination destination

    @Autowired
    private UserRepository userRepository

    def setup() {
        // テスト実行毎にUSERテーブルのデータをクリア
        new DbSetup(destination, Operations.deleteAllFrom('user')).launch()
    }

    def '名前= #name 、性別= #sex のユーザをinsetできること'() {
        given: '確認用SQL'
        def query = 'select count(1) as cnt from user where name = :name and sex = :sex'

        when: 'テスト実施'
        userRepository.insertUser(
                new User(name: name, sex: sex)
        )

        then: 'テスト結果確認'
        def actuals = new Sql(destination.getConnection()).rows([name: name, sex: sex.name()], query)
        actuals.size() == 1
        actuals.get(0).get('cnt') == 1

        where:
        name      | sex
        'Mike'    | User.Sex.MALE
        'Natalie' | User.Sex.FEMALE
    }

    def 'USERテーブルからidの昇順で全件取得できること'() { // DbUnitで実装の方がいいかも？
        given: 'データ投入'
            new DbSetup(destination, Operations.sequenceOf(
                    Operations.insertInto('user')
                    .columns('id', 'name', 'sex')
                    .values(102, 'Hanako', 'FEMALE')
                    .values(101, 'Jiro', 'MALE')
                    .values(100, 'Taro', 'MALE')
                    .build()
            )).launch()

        when: 'テスト実行'
        List<User> actuals = userRepository.selectUsers()

        then: 'テスト結果確認'
        actuals.size() == 3

        and: '1件目'
        actuals.get(0).getId() == 100
        actuals.get(0).getName() == 'Taro'
        actuals.get(0).getSex() == User.Sex.MALE

        and: '2件目'
        actuals.get(1).getId() == 101
        actuals.get(1).getName() == 'Jiro'
        actuals.get(1).getSex() == User.Sex.MALE

        and: '3件目'
        actuals.get(2).getId() == 102
        actuals.get(2).getName() == 'Hanako'
        actuals.get(2).getSex() == User.Sex.FEMALE
    }

    def 'id= #id の場合に、USERテーブルからユーザが取得できること'() {
        given: 'データ投入'
        new DbSetup(destination, Operations.sequenceOf(
                Operations.insertInto('user')
                        .columns('id', 'name', 'sex')
                        .values(200, 'Taro', 'MALE')
                        .values(201, 'Hanako', 'FEMALE')
                        .build()
        )).launch()

        when: 'テスト実行'
        User actual = userRepository.selectOne(id)

        then: 'テスト結果確認'
        actual != null
        actual.getId() == id
        actual.getName() == name
        actual.getSex() == sex

        where:
        id  || name     | sex
        200 || 'Taro'   | User.Sex.MALE
        201 || 'Hanako' | User.Sex.FEMALE
    }

    def 'USERテーブルに検索条件に該当するidが存在しない場合に、検索結果が取得できないこと'() {
        given: 'データ投入'
        new DbSetup(destination, Operations.sequenceOf(
                Operations.insertInto('user')
                    .row()
                        .column('id', 300)
                        .column('name', 'Taro')
                        .column('sex', 'MALE')
                    .end()
                    .row()
                        .column('id', 301)
                        .column('name', 'Hanako')
                        .column('sex', 'FEMALE')
                    .end()
                    .build()
        )).launch()

        and: '検索パラメータ'
        def id = 302

        when: 'テスト実行'
        User actual = userRepository.selectOne(id)

        then: 'テスト結果確認'
        actual == null
    }
}
