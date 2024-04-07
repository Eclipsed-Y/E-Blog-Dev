package cn.ecnu.eblog.activity.controller;
import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.LikeDTO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import cn.ecnu.eblog.common.pojo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/activity/like")
public class LikeController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostMapping
    public Result<?> like(@RequestBody LikeDTO likeDTO){
        log.info("id: {} 用户点赞", BaseContext.getCurrentId());
        likeDTO.setUserId(BaseContext.getCurrentId());
        Msg<LikeDTO> msg = new Msg<>();
        msg.setData(likeDTO);
        rabbitTemplate.convertAndSend(MQConstant.TOPIC_EXCHANGE, MQConstant.LIKE, msg);
        return Result.success();
    }
}
