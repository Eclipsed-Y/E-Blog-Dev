package cn.ecnu.eblog.activity.listener;
import cn.ecnu.eblog.activity.service.CommentService;
import cn.ecnu.eblog.activity.service.LikeService;
import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.enumeration.OperationType;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.dto.LikeDTO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {MQConstant.LIKE_QUEUE})
public class LikeListener {
    @Autowired
    private LikeService likeService;
    @RabbitHandler
    public void process(Msg<LikeDTO> msg){
        likeService.like(msg.getData());
    }
}
