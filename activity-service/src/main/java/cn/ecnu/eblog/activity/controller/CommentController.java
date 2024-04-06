package cn.ecnu.eblog.activity.controller;

import cn.ecnu.eblog.activity.service.CommentService;
import cn.ecnu.eblog.common.annotation.Inner;
import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.enumeration.OperationType;
import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.dto.CommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.SecondCommentPageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/activity/comment")
public class CommentController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CommentService commentService;

    /**
     * 新增评论
     * @param commentDTO
     * @return
     */
    @PostMapping
    public Result<?> insertComment(@RequestBody CommentDTO commentDTO){
        log.info("id: {} 用户新增评论", BaseContext.getCurrentId());
        commentDTO.setUserId(BaseContext.getCurrentId());
        Msg<CommentDTO> msg = new Msg<>(OperationType.INSERT, commentDTO);
        rabbitTemplate.convertAndSend(MQConstant.TOPIC_EXCHANGE, MQConstant.COMMENT, msg);
        return Result.success();
    }

    /**
     * 修改评论
     * @param commentDTO
     * @return
     */
    @PutMapping
    public Result<?> updateComment(@RequestBody CommentDTO commentDTO){
        log.info("id: {} 用户修改评论", BaseContext.getCurrentId());
        commentDTO.setUserId(BaseContext.getCurrentId());
        Msg<CommentDTO> msg = new Msg<>(OperationType.UPDATE, commentDTO);
        rabbitTemplate.convertAndSend(MQConstant.TOPIC_EXCHANGE, MQConstant.COMMENT, msg);
        return Result.success();
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteComment(@PathVariable("id") Long id){
        log.info("id: {} 用户删除评论", BaseContext.getCurrentId());
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUserId(BaseContext.getCurrentId());
        commentDTO.setId(id);
        Msg<CommentDTO> msg = new Msg<>(OperationType.DELETE, commentDTO);
        rabbitTemplate.convertAndSend(MQConstant.TOPIC_EXCHANGE, MQConstant.COMMENT, msg);
        return Result.success();
    }

    /**
     * 内部接口，根据文章id删除评论
     * @param articleId
     * @return
     */
    @DeleteMapping("/delete/{articleId}")
    @Inner
    public Result<?> deleteByArticleId(@PathVariable("articleId") Long articleId){
        log.info("id: {} 用户删除文章及其评论", BaseContext.getCurrentId());
        commentService.update(new UpdateWrapper<CommentDO>().eq("article_id", articleId).set("deleted", 1));
        return Result.success();
    }

    /**
     * 内部接口，更新用户信息
     * @param userInfoDTO
     * @return
     */
    @PutMapping("/userInfo")
    @Inner
    public Result<?> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO){
        log.info("id: {} 用户更新用户信息", BaseContext.getCurrentId());
        commentService.updateUserInfo(userInfoDTO);
        return Result.success();
    }

    /**
     * 根据文章id获取根评论
     * @param commentPageQueryDTO
     * @return
     */
    @GetMapping
    public Result<PageResult> getRootComment(CommentPageQueryDTO commentPageQueryDTO){
        log.info("id: {} 用户分页查询根评论", BaseContext.getCurrentId());
        PageResult pageResult = commentService.pageSelect(commentPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据根评论获取二级评论
     * @param secondCommentPageQueryDTO
     * @return
     */
    @GetMapping("/second")
    public Result<PageResult> getSecondComment(SecondCommentPageQueryDTO secondCommentPageQueryDTO){
        log.info("id: {} 用户分页查询二级评论", BaseContext.getCurrentId());
        PageResult pageResult = commentService.getSecondComment(secondCommentPageQueryDTO);
        return Result.success(pageResult);
    }
}
