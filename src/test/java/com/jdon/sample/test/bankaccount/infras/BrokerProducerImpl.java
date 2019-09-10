package com.jdon.sample.test.bankaccount.infras;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;
import com.jdon.sample.test.bankaccount.command.Cancel;
import com.jdon.sample.test.bankaccount.command.TransferCommand;

/**
 * this is the event Producer for Service/Component
 */
@Component
@Introduce("componentmessage")
public class BrokerProducerImpl implements BrokerProducerIF {


	public DomainMessage transfer(String  Id,   BankAccount bankAccount, TransferCommand transferCommand){
		return new DomainMessage(transferCommand);
	}


	public DomainMessage cancel(String  Id,  BankAccount bankAccount, Cancel transferCommand){
		return new DomainMessage (transferCommand);
	}

}
