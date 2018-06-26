package light.sundq.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import light.sundq.web.websocket.WSRequest;
import light.sundq.web.websocket.WSResponse;

@Controller
public class WSController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@MessageMapping("/sendTest")
    @SendTo("/topic/subscribeTest")
    public WSResponse sendDemo(WSRequest message) {
        logger.info("接收到了信息" + message.getName());
        return new WSResponse("你发送的消息为:" + message.getName());
    }

    @SubscribeMapping("/subscribeTest")
    public WSResponse sub() {
        logger.info("XXX用户订阅了我。。。");
        return new WSResponse("感谢你订阅了我。。。");
    }
}