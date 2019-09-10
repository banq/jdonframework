package com.jdon.sample.test.bankaccount.command;

import com.jdon.sample.test.bankaccount.event.Deposited;
import com.jdon.sample.test.bankaccount.event.TransferEvent;

public class Deposit extends TransferCommand {

    public Deposit(String transactionId, String aggregateId, String commandId, int value) {
        super(transactionId, aggregateId, commandId, value);
    }

    public TransferEvent creatTransferEvent(){
        return new Deposited(getTransactionId(), getAggregateId(), getCommandId(), getValue());
    }
}
