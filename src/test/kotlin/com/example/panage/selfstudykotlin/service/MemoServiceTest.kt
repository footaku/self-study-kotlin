package com.example.panage.selfstudykotlin.service

import com.example.panage.selfstudykotlin.repositry.MemoRepository
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

/**
 * @author fu-taku
 */
class MemoServiceTest : Spek({

    val memoRepository: MemoRepository = mock()

    given("MemoServiceのパラメータテスト") {
        val where = arrayOf(
                data("hoge", "fuga"),
                data("1234", "5678")
        )

        val service: MemoService = MemoService(memoRepository)

        on("join(%s, %s) -> ", with = *where) { memo, author ->
            val actual = service.join(memo, author)

            it("actual == $actual") {
                assert(actual.memo == memo)
                assert(actual.author == author)
                assert(actual.created != null)
            }
        }
    }
})
