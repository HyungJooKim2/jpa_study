package study.jpa_study;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){   //Model : 데이터를 실어서 View 에 넘길 수 있다.
        model.addAttribute("data","hello!"); //"data"라는 키와 "hello"라는 값을 넘긴다.
        return "hello.html"; // hello : 화면 이름
    }
}
