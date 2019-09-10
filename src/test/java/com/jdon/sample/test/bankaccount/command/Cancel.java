package com.jdon.sample.test.bankaccount.command;

public class Cancel extends TransferCommand {
    private final TransferCommand transferCommand;

    public Cancel(TransferCommand transferCommand) {
        super(transferCommand.getTransactionId(), transferCommand.getAggregateId(), transferCommand.getCommandId(), transferCommand.getValue());
        this.transferCommand = transferCommand;
    }

    public TransferCommand getTransferCommand() {
        return transferCommand;
    }
}
