package com.zbw.fame.controller.admin;

import com.github.pagehelper.Page;
import com.zbw.fame.controller.BaseController;
import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.param.CommentParam;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台评论管理 Controller
 *
 * @author zbw
 * @since 2018/1/21 10:47
 */
@RestController
@RequestMapping("/api/admin/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取所有评论
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Pagination<Comment>}
     */
    @GetMapping
    public RestResponse index(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = FameConsts.PAGE_SIZE) Integer limit) {
        CommentParam param = CommentParam
                .builder()
                .summary(false)
                .html(false)
                .build();
        Page<Comment> comments = commentService.getComments(page, limit, param);
        return RestResponse.ok(new Pagination<Comment>(comments));
    }

    /**
     * 获取评论详情
     *
     * @param id 评论id
     * @return {@see CommentDto}
     */
    @GetMapping("{id}")
    public RestResponse detail(@PathVariable Integer id) {
        CommentDto comment = commentService.getCommentDetail(id);
        if (null == comment) {
            return this.error404();
        }
        if (null != comment.getPComment()) {
            comment.getPComment().setContent(FameUtil.mdToHtml(comment.getPComment().getContent()));
        }
        comment.setContent(FameUtil.mdToHtml(comment.getContent()));
        return RestResponse.ok(comment);
    }

    /**
     * 删除评论
     *
     * @param id 评论id
     * @return {@see RestResponse.ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse delete(@PathVariable Integer id) {
        if (commentService.deleteComment(id)) {

            return RestResponse.ok();
        } else {
            return RestResponse.fail("删除评论失败");
        }
    }

    /**
     * 获取评论数量
     *
     * @return {@see Integer}
     */
    @GetMapping("count")
    public RestResponse count() {
        return RestResponse.ok(commentService.count());
    }

}
