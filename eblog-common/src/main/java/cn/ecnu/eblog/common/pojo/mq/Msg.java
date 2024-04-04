package cn.ecnu.eblog.common.pojo.mq;

import cn.ecnu.eblog.common.enumeration.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg <T> implements Serializable {
    private OperationType operationType;
    private T data;
}
