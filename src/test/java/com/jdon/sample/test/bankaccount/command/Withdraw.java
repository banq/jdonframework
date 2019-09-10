package com.jdon.sample.test.bankaccount.command;

import com.jdon.sample.test.bankaccount.event.TransferEvent;
import com.jdon.sample.test.bankaccount.event.Withdrawed;

public class Withdraw extends TransferCommand {
    public Withdraw(String transactionId, String aggregateId, String commandId, int value) {
        super(transactionId, aggregateId, commandId, value);
    }

    public int getValue() {
        return Math.negateExact(super.getValue());
    }

    public TransferEvent creatTransferEvent(){
        return new Withdrawed(getTransactionId(), getAggregateId(), getCommandId(), super.getValue());
    }
}
