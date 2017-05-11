package com.example.panage.selfstudykotlin.controller.page

import com.example.panage.selfstudykotlin.model.Memo
import com.example.panage.selfstudykotlin.service.MemoService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

/**
 * @author fu-taku
 */
class MemoControllerTest : Spek({

    val memoService: MemoService = mock {
        on { join(any(), any()) } doAnswer { invocationOnMock ->
            val memo = invocationOnMock.arguments[0] as String?
            val author = invocationOnMock.arguments[1] as String?
            Memo(memo, author, Date())
        }
    }
    val mockMvc = MockMvcBuilders.standaloneSetup(MemoController(memoService)).build()

    given("MemoController") {

        context("/memo") {
            describe("Getアクセス") {
                val response = mockMvc.perform(MockMvcRequestBuilders.get("/memo/"))

                it("HttpStatus:200になる") {
                    response.andExpect(MockMvcResultMatchers.status().isOk)
                }
            }

            describe("Postアクセス") {
                val memo: String = "aaa"
                val author: String = "bbb"
                val response = mockMvc.perform(MockMvcRequestBuilders.post("/memo/")
                        .param("memo", memo)
                        .param("author", author))

                it("HttpStatus:200になる") {
                    response.andExpect(MockMvcResultMatchers.status().isOk)

                    it("responseにMemoがふくまれている") {
                        val items = response.andReturn().modelAndView.modelMap["items"] as List<*>
                        assertNotNull(items)
                        assert(items.size == 1)
                        assertTrue(items is List<*>)

                        val item = items[0] as Memo
                        assert(item.memo == memo)
                        assert(item.author == author)
                    }
                }
            }
        }

        context("/memo/param/{memo}?author={author}のパターンテスト") {
            given("パラメーターの準備") {
                val where = arrayOf(
                        data("hoge", "piyo"),
                        data("1234", "5678"),
                        data("HOGE", "PIYO"),
                        data("12ho", "34pi"),
                        data("ho12", "pi34"),
                        data("ho12", "")
                )

                on("/memo/%s/author/%sにGetアクセス", with = *where) { memo, author ->
                    val response = mockMvc.perform(MockMvcRequestBuilders.get("/memo/param/$memo?author=$author"))

                    it("HttpStatus:200になる") {
                        response.andExpect(MockMvcResultMatchers.status().isOk)

                        it("responseにMemoがふくまれている") {
                            val items = response.andReturn().modelAndView.modelMap["items"] as List<*>
                            assertNotNull(items)
                            assert(items.size == 1)
                            assertTrue(items is List<*>)

                            val item = items[0] as Memo
                            assert(item.memo == memo)
                            assert(item.author == author)
                        }
                    }
                }
            }
        }
    }
})
