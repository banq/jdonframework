package com.jdon.sample.test.bankaccount.a;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;



@Introduce("message")
public class DomainEventProducer {

	@Send("transfernext")
	public DomainMessage  nextStep(TransferEvent event) {
		return new DomainMessage (event);
	}

	@Send("failureEvent")
	public DomainMessage failure(TransferEvent transferEvent){
		return new DomainMessage(transferEvent);
	}

	@Send("finishEvent")
	public DomainMessage finish(TransferEvent transferEvent){
		return new DomainMessage(transferEvent);
	}
	
}
