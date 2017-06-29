package com.jdon.sample.test.bankaccount.a;

import com.jdon.annotation.model.Receiver;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;

public interface CommandProducerIF {

	@Send("withdrawCommand")
	public DomainMessage withdrawCommand(@Receiver BankAccount bModel, TransferEvent transferEvent);
	
	@Send("depositCommand")
	public DomainMessage depositCommand(@Receiver BankAccount bModel, TransferEvent transferEvent);

	@Send("failureCommand")
	public DomainMessage failureCommand(@Receiver BankAccount bModel, TransferEvent transferEvent);

	@Send("finishCommand")
	public DomainMessage finishCommand(@Receiver BankAccount bModel, TransferEvent transferEvent);

}
