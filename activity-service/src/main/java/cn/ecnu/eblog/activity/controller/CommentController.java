package cn.ecnu.eblog.activity.controller;

import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.enumeration.OperationType;
import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import cn.ecnu.eblog.common.pojo.result.Result;
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
}
