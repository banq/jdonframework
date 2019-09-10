package com.jdon.sample.test.bankaccount.infras;

import com.jdon.annotation.model.Owner;
import com.jdon.annotation.model.Receiver;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;
import com.jdon.sample.test.bankaccount.command.Cancel;
import com.jdon.sample.test.bankaccount.command.TransferCommand;

/**
 * this is the event Producer for Service/Component
 */
public interface BrokerProducerIF {

    @Send("transfer")
    DomainMessage transfer(@Owner String  id, @Receiver BankAccount bankAccount, TransferCommand transferCommand);

    @Send("cancel")
    DomainMessage cancel(@Owner String  id, @Receiver BankAccount bankAccount, Cancel transferCommand);
}
