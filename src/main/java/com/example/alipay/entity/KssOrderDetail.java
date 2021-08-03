package com.example.alipay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "kss_order_detail")
public class KssOrderDetail {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "courseid")
    private String courseid;

    @TableField(value = "coursetitle")
    private String coursetitle;

    @TableField(value = "courseimg")
    private String courseimg;

    @TableField(value = "userid")
    private String userid;

    @TableField(value = "ordernumber")
    private String ordernumber;

    @TableField(value = "tradeno")
    private String tradeno;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(value = "username")
    private String username;

    @TableField(value = "price")
    private String price;

    @TableField(value = "paymethod")
    private String paymethod;
}