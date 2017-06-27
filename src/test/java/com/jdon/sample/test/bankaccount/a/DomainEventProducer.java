package com.jdon.sample.test.bankaccount.a;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.AccountParameterVO;


@Component
@Introduce("message")
public class DomainEventProducer implements DomainEventProduceIF {

	@Send("transferMoneyEventToB")
	public DomainMessage sendtoAnotherAggragate(AccountParameterVO parameterVO) {
		return new DomainMessage(parameterVO);
	}

}
