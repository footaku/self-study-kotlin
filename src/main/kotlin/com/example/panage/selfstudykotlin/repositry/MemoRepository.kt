package com.example.panage.selfstudykotlin.repositry

import com.example.panage.selfstudykotlin.model.Memo


/**
 * @author fu-taku
 */
interface MemoRepository {
    fun find(): List<Memo>
    fun findByAuthor(author: String): List<Memo>
    fun save(item: Memo)
}
