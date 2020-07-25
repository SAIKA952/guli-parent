package com.example.demo.eduservice.entity.comment;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value="课程详情评论", description="网站课程详情页评论相关字段")
@Data
public class CourseCommentVo {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "讲师ID")
    private String teacherId;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "用户ID")
    private String memberId;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}