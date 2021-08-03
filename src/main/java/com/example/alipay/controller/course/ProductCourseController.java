package com.example.alipay.controller.course;

import com.example.alipay.entity.KssCourses;
import com.example.alipay.service.KssCoursesService;
import com.example.alipay.vo.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxinabo
 * @date 2021/8/2
 */
@RestController
@RequestMapping("/api/course")
public class ProductCourseController {

    private final KssCoursesService kssCoursesService;

    public ProductCourseController(KssCoursesService kssCoursesService) {
        this.kssCoursesService = kssCoursesService;
    }

    @PostMapping("/load/course")
    public R loadCourse() {
        List<KssCourses> courseList = kssCoursesService.list();
        return R.ok().data("courseList", courseList);
    }
}
