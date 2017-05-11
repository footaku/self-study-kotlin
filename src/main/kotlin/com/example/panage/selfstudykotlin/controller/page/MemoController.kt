package com.example.panage.selfstudykotlin.controller.page

import com.example.panage.selfstudykotlin.model.Memo
import com.example.panage.selfstudykotlin.service.MemoService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


/**
 * @author fu-taku
 */
@Controller
@RequestMapping("memo")
class MemoController(val memoService: MemoService) {

    @GetMapping
    fun get(model: Model): String {
        model.addAttribute("items", memoService.readAll())
        return "memo"
    }

    @GetMapping("{author:[a-zA-Z0-9]+}")
    fun get(@PathVariable author: String, model: Model): String {
        model.addAttribute("items", memoService.readByAuthor(author))
        return "memo"
    }

    @GetMapping("param/{memo:[a-zA-Z0-9]+}")
    fun get(@PathVariable memo: String,
            @RequestParam(required = false, defaultValue = "Default Author") author: String,
            model: Model): String {
        model.addAttribute("items", listOf(memoService.join(memo, author)))
        return "memo"
    }

    @PostMapping
    fun post(@ModelAttribute item: Memo, model: Model): String {
        memoService.write(item.memo, item.author)
        return "redirect:/memo"
    }
}
