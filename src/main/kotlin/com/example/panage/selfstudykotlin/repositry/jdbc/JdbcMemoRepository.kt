package com.example.panage.selfstudykotlin.repositry.jdbc

import com.example.panage.selfstudykotlin.model.Memo
import com.example.panage.selfstudykotlin.repositry.MemoRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JdbcMemoRepository(val jdbcTemplate: JdbcTemplate) : MemoRepository {

    override fun find(): List<Memo> =
            jdbcTemplate.query("SELECT MEMO, AUTHOR FROM MEMO ORDER BY CREATED ASC",
                    { resultSet, _ ->
                        Memo(
                                resultSet.getString("MEMO"),
                                resultSet.getString("AUTHOR"),
                                Date()
                        )
                    })


    override fun findByAuthor(author: String): List<Memo> =
            jdbcTemplate.query("SELECT MEMO, AUTHOR FROM MEMO WHERE AUTHOR = ? ORDER BY CREATED ASC",
                    RowMapper { resultSet, _ ->
                        Memo(
                                resultSet.getString("MEMO"),
                                resultSet.getString("AUTHOR"),
                                Date()
                        )
                    },
                    author)


    override fun save(item: Memo) {
        jdbcTemplate.update(
                """INSERT INTO MEMO (MEMO, AUTHOR, CREATED) VALUES (?, ?, CURRENT_TIMESTAMP)""",
                item.memo, item.author
        )
    }
}
