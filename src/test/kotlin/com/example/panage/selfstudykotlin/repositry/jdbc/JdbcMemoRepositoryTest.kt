package com.example.panage.selfstudykotlin.repositry.jdbc

import com.example.panage.selfstudykotlin.model.Memo
import com.example.panage.selfstudykotlin.repositry.MemoRepository
import com.example.panage.selfstudykotlin.util.createDataSource
import com.example.panage.selfstudykotlin.util.flywayMigrate
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.springframework.jdbc.core.JdbcTemplate
import java.util.*
import java.util.stream.Collectors
import javax.sql.DataSource

/**
 * @author fu-taku
 */
class JdbcMemoRepositoryTest : Spek({

    given("MemoRepository") {
        val dataSource: DataSource = createDataSource()
        flywayMigrate(dataSource)
        val memoRepository: MemoRepository = JdbcMemoRepository(JdbcTemplate(dataSource))

        context("レコード取得") {
            describe("すべてのレコードを取得") {
                val items: List<Memo> = memoRepository.find()

                it("すべて取得できている") {
                    assert(items.size == 4)
                    val memos: MutableList<String?>? = items.stream().map { (memo) -> memo }.collect(Collectors.toList())
                    assert(memos == listOf("Springを学ぶ", "Thymeleafを学ぶ", "Flywayを学ぶ", "AspectJを生麩"))
                }
            }

            given("authorを指定する") {
                val where = arrayOf(
                        data("金次郎", expected = listOf("Springを学ぶ", "Thymeleafを学ぶ", "Flywayを学ぶ")),
                        data("金字塔", expected = listOf("AspectJを生麩")),
                        data("金太郎", expected = emptyList())
                )

                on("Author = %s を指定してSELECT", with = *where) { author, expected ->
                    val items: List<Memo> = memoRepository.findByAuthor(author)

                    it("$expected と一致する") {
                        val memos: MutableList<String?>? = items.stream().map { (memo) -> memo }.collect(Collectors.toList())
                        assert(memos == expected)
                    }
                }
            }
        }

        context("レコード更新") {
            given("INSERTを実行") {
                val where = arrayOf(
                        data(Memo("hoge", "fuga", Date()), expected = 5),
                        data(Memo("piyo", "puyo", Date()), expected = 6)
                )

                on("%s を追加", with = *where) { memo, expected ->
                    memoRepository.save(memo)
                    val items: List<Memo> = memoRepository.find()

                    it("レコード数が $expected になっている") {
                        assert(items.size == expected)
                    }
                }
            }
        }
    }
})
