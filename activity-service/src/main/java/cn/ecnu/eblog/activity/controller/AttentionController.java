package cn.ecnu.eblog.activity.controller;

import cn.ecnu.eblog.activity.service.AttentionService;
import cn.ecnu.eblog.activity.utils.RedisUtil;
import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.AttentionDTO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import cn.ecnu.eblog.common.pojo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/activity/attention")
public class AttentionController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AttentionService attentionService;

    /**
     * 根据用户id关注
     * @param attentionDTO
     * @return
     */
    @PostMapping
    public Result<?> attentionByUserId(@RequestBody AttentionDTO attentionDTO){
        log.info("id: {} 用户关注", BaseContext.getCurrentId());
        attentionDTO.setFollowerId(BaseContext.getCurrentId());
        Msg<AttentionDTO> msg = new Msg<>();
        msg.setData(attentionDTO);
        // 修改redis中的数据
        redisUtil.attentionCheck(attentionDTO.getUserId(), attentionDTO.getFollowerId());
        // 发送消息
        rabbitTemplate.convertAndSend(MQConstant.TOPIC_EXCHANGE, MQConstant.LIKE, msg);
        return Result.success();
    }

    /**
     * 获取指定用户id的关注数
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Result<Integer> getAttentionCount(@PathVariable Long userId){
        log.info("id: {} 用户查看关注数", BaseContext.getCurrentId());
        Integer cnt = attentionService.getAttentionCount(userId);
        return Result.success(cnt);
    }

    /**
     * 查看是否已关注
     * @param attentionDTO
     * @return
     */
    @GetMapping
    public Result<Integer> hasAttention(AttentionDTO attentionDTO){
        log.info("id: {} 用户查看是否关注", BaseContext.getCurrentId());
        attentionDTO.setFollowerId(BaseContext.getCurrentId());
        Integer res = attentionService.hasAttention(attentionDTO);
        return Result.success(res);
    }
}
