package com.example.alipay.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * @author wanxinabo
 * @date 2021/8/2
 */
@Data
@TableName(value = "kss_courses")
public class KssCourses {
    /**
     * 课程唯一id
     */
    @TableId(value = "courseid", type = IdType.ASSIGN_ID)
    private String courseid;

    /**
     * 课程标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 课程简短介绍
     */
    @TableField(value = "intro")
    private String intro;

    /**
     * 课程封面地址
     */
    @TableField(value = "img")
    private String img;

    /**
     * 课程的活动价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 状态:已发布/未发布
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}