package com.jdon.sample.test.cqrs.a;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.cqrs.ParameterVO;

@Component
@Introduce("message")
public class DomainEventProducer implements DomainEventProduceIF {

	@Send("toEventB")
	public DomainMessage sendtoAnotherAggragate(ParameterVO parameterVO) {
		return new DomainMessage(parameterVO);
	}

}
