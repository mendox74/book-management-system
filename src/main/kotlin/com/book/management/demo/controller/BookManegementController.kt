package com.book.management.demo.controller

import com.book.management.demo.model.Request
import com.book.management.demo.generated.jooq.tables.*
import com.book.management.demo.generated.jooq.tables.records.AuthorRecord
import com.book.management.demo.generated.jooq.tables.records.BooksRecord
import org.jooq.*;
import org.jooq.impl.*
import org.jooq.impl.DSL.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.http.ResponseEntity

@RestController
class HelloController {
    val url = "jdbc:mysql://localhost:3306/"
    val user = 
    val password = 

    // 書籍のタイトルと著者をすべて取得
    @GetMapping("/book/title/all")
    fun getBookAll(): String {
        val author = Author.AUTHOR
        val books = Books.BOOKS
        var string = "{"
        DSL.using(
            url,
            user,
            password
        ).select(books.TITLE, author.NAME)
        .from(books)
        .join(author)
        .on(books.AUTHOR_ID.eq(author.ID))
        .orderBy(1, 2).forEach {
            string += "{title: ${it[books.TITLE]}, name: ${it[author.NAME]}},";
        }
        string = string.dropLast(1) + "}"
        return string;
    }

    // 著者から書籍を検索
    @GetMapping("/book/author/{authorId}")
    @ResponseBody
    fun getBookByAuthorId(@PathVariable("authorId") authorId: Int): String {
        val author = Author.AUTHOR
        val books = Books.BOOKS
        var string = "{"
        DSL.using(
            url,
            user,
            password
        ).select(books.TITLE, author.NAME)
        .from(books)
        .join(author)
        .on(books.AUTHOR_ID.eq(author.ID))
        .where(books.AUTHOR_ID.eq(authorId))
        .orderBy(1, 2).forEach {
            string += "{title: ${it[books.TITLE]}, name: ${it[author.NAME]}},";
        }
        string = string.dropLast(1) + "}"
        return string;
    }

    // 書籍及び著者を登録
    @PostMapping("/book/create")
    fun createBook(@RequestBody request: Request): String {
        val author = Author.AUTHOR
        val books = Books.BOOKS
        var author_id: Int = request.author_id
        val create = DSL.using(
            url,
            user,
            password
        )
        if (0 == request.author_id) {
            val authorRecord: AuthorRecord = create.newRecord(author)
            authorRecord.set(author.NAME, request.name)
            author_id = authorRecord.get("ID") as Int
        }
        val booksRecord: BooksRecord = create.newRecord(books)
        booksRecord.set(books.TITLE, request.title)
        booksRecord.set(books.CONTENT, request.content)
        booksRecord.set(books.AUTHOR_ID, author_id)
        return "登録完了"
    }

    // 書籍を更新
    @PostMapping("/book/update")
    fun updateBook(@RequestBody request: Request): String {
        val books = Books.BOOKS
        val create = DSL.using(
            url,
            user,
            password
        )
        val booksRecord = create.fetchOne(books, books.ID.eq(request.id)) as BooksRecord
        booksRecord.set(books.TITLE, request.title)
        booksRecord.set(books.CONTENT, request.content)
        booksRecord.set(books.AUTHOR_ID, request.author_id)
        return "更新完了"
    }

    // 書籍を削除
    @DeleteMapping("/book/delete/{id}")
    fun deleteBook(@PathVariable("id") id: Int): String {
        val books = Books.BOOKS
        val create = DSL.using(
            url,
            user,
            password
        )
        val booksRecord = create.fetchOne(books, books.ID.eq(id)) as BooksRecord
        booksRecord.delete()
        return "削除完了"
    }
}