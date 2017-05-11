package com.example.panage.selfstudykotlin.service

import com.example.panage.selfstudykotlin.model.Memo
import com.example.panage.selfstudykotlin.repositry.MemoRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author fu-taku
 */
@Service
class MemoService(val memoRepository: MemoRepository) {
    fun join(memo: String, author: String): Memo = Memo(memo, author, Date())

    fun readAll() = memoRepository.find()

    fun readByAuthor(author: String) = memoRepository.findByAuthor(author)

    fun write(memo: String?, author: String?) = memoRepository.save(Memo(memo, author, Date()))

}
