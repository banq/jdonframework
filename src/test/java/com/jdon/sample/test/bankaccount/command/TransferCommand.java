package com.jdon.sample.test.bankaccount.command;

import com.jdon.sample.test.bankaccount.event.Canceled;
import com.jdon.sample.test.bankaccount.event.TransferEvent;

public class TransferCommand {

    private final String transactionId;
    private final String aggregateId;
    private final String commandId;
    private final int value;

    public TransferCommand(String transactionId, String aggregateId, String commandId, int value) {
        this.transactionId = transactionId;
        this.aggregateId = aggregateId;
        this.commandId = commandId;
        this.value = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getCommandId() {
        return commandId;
    }

    public int getValue() {
        return value;
    }

    public TransferEvent creatTransferEvent(){
        return new TransferEvent(transactionId, aggregateId, commandId, value);
    }

    public Canceled createCanceled(){
        return new Canceled(this);
    }

    public Cancel cancel(){
        return new Cancel(this);
    }
}
