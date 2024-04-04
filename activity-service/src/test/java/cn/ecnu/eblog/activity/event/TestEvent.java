package cn.ecnu.eblog.activity.event;

import org.springframework.context.ApplicationEvent;

public class TestEvent extends ApplicationEvent {

    private final String msg;
    public TestEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
