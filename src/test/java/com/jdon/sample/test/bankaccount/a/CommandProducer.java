package com.jdon.sample.test.bankaccount.a;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.domain.message.DomainMessage;

@Component()
@Introduce("componentmessage")
public class CommandProducer implements CommandProducerIF{

	public DomainMessage withdrawCommand(BankAccount bModel, TransferEvent transferEvent){
		return new DomainMessage(transferEvent);
	}

	public DomainMessage depositCommand(BankAccount bModel, TransferEvent transferEvent){
		return new DomainMessage(transferEvent);
	}

	public DomainMessage failureCommand(BankAccount bModel, TransferEvent transferEvent){
		return new DomainMessage(transferEvent);
	}

	public DomainMessage finishCommand(BankAccount bModel, TransferEvent transferEvent){
		return new DomainMessage(transferEvent);
	}
	
	
}
