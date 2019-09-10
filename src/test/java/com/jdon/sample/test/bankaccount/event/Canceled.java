package com.jdon.sample.test.bankaccount.event;

import com.jdon.sample.test.bankaccount.command.Deposit;
import com.jdon.sample.test.bankaccount.command.TransferCommand;

public class Canceled extends TransferEvent {

    private final TransferCommand transferCommand;

    public Canceled(TransferCommand transferCommand) {
        super(transferCommand.getTransactionId(), transferCommand.getAggregateId(), transferCommand.getCommandId(), transferCommand.getValue());
        this.transferCommand = transferCommand;
    }

    public int getValue() {
        if(transferCommand instanceof Deposit)
           return Math.negateExact(super.getValue());
        else
            return super.getValue();
    }

}
