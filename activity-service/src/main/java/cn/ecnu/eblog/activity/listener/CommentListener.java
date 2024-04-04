package cn.ecnu.eblog.activity.listener;
import cn.ecnu.eblog.activity.service.CommentService;
import cn.ecnu.eblog.common.constant.MQConstant;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.enumeration.OperationType;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.mq.Msg;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {MQConstant.COMMENT})
public class CommentListener {
    @Autowired
    private CommentService commentService;
    @RabbitHandler
    public void process(Msg<?> msg){
        OperationType operationType = msg.getOperationType();
        switch (operationType){
            case INSERT:
                commentService.insertComment((CommentDTO) msg.getData());
                break;
            // todo
//            case UPDATE:
//                commentService.updateComment(msg.getData());
//                break;
//            case DELETE:
//                commentService.deleteComment(msg.getData());
//                break;
            default:
                throw new RequestExcetption(MessageConstant.ILLEGAL_REQUEST);
        }
    }
}
