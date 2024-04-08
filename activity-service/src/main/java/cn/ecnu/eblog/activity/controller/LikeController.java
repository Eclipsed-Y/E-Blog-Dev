package cn.ecnu.eblog.activity.controller;
import cn.ecnu.eblog.activity.service.LikeService;
import cn.ecnu.eblog.activity.utils.RedisUtil;
import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.LikeDTO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import cn.ecnu.eblog.common.pojo.result.Result;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/activity/like")
public class LikeController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LikeService likeService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 点赞请求
     * @param likeDTO
     * @return
     */
    @PostMapping
    public Result<?> like(@RequestBody LikeDTO likeDTO){
        log.info("id: {} 用户点赞", BaseContext.getCurrentId());
        likeDTO.setUserId(BaseContext.getCurrentId());
        Msg<LikeDTO> msg = new Msg<>();
        msg.setData(likeDTO);
        // 修改redis中的数据
        redisUtil.likeCheck(likeDTO.getArticleId(), likeDTO.getUserId());
        // 发送消息
        rabbitTemplate.convertAndSend(MQConstant.TOPIC_EXCHANGE, MQConstant.LIKE, msg);
        return Result.success();
    }

    /**
     * 查询文章点赞数
     * @param articleId
     * @return
     */
    @GetMapping("/{articleId}")
    public Result<Integer> getLikeCount(@PathVariable("articleId") Long articleId){
        log.info("id: {} 用户获取文章点赞数", BaseContext.getCurrentId());
        Integer cnt = likeService.getLikeCount(articleId, BaseContext.getCurrentId());
        return Result.success(cnt);
    }

    @GetMapping
    public Result<Integer> liked(LikeDTO likeDTO){
        log.info("id: {} 用户查询文章是否点赞", BaseContext.getCurrentId());
        likeDTO.setUserId(BaseContext.getCurrentId());
        Integer res = likeService.liked(likeDTO);
        return Result.success(res);
    }
}
