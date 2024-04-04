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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/activity/comment")
public class CommentController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostMapping
    public Result<?> insertComment(@RequestBody CommentDTO commentDTO){
        log.info("id: {} 用户新增评论", BaseContext.getCurrentId());
        commentDTO.setUserId(BaseContext.getCurrentId());
        Msg<CommentDTO> msg = new Msg<>(OperationType.INSERT, commentDTO);
        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE, MQConstant.COMMENT, msg);
        return Result.success();
    }
}
