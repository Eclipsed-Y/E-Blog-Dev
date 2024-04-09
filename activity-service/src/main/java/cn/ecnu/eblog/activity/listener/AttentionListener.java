package cn.ecnu.eblog.activity.listener;
import cn.ecnu.eblog.activity.service.AttentionService;
import cn.ecnu.eblog.activity.service.LikeService;
import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.pojo.dto.AttentionDTO;
import cn.ecnu.eblog.common.pojo.dto.LikeDTO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {MQConstant.ATTENTION_QUEUE})
public class AttentionListener {
    @Autowired
    private AttentionService attentionService;
    @RabbitHandler
    public void process(Msg<AttentionDTO> msg){
        attentionService.attentionByUserId(msg.getData());
    }
}
