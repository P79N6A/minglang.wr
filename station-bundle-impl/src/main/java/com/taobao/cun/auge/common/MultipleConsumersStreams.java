package com.taobao.cun.auge.common;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MultipleConsumersStreams {
	 @Input("c2bquali")
	 SubscribableChannel inputA();
}
