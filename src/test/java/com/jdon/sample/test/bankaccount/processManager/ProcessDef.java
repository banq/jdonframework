package com.jdon.sample.test.bankaccount.processManager;

import com.jdon.annotation.Component;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;
import com.jdon.sample.test.bankaccount.command.TransferCommand;
import com.jdon.sample.test.bankaccount.infras.AccountRepositoryIF;
import com.jdon.sample.test.bankaccount.infras.BrokerProducerIF;

import java.util.*;

@Component
public class ProcessDef {
    //processDef order
    private List<TransferCommand> processDef;
    //cureent step
    private final Map<String, String> cStep =  new HashMap<>();
    private final AccountRepositoryIF accountRepository;
    private final BrokerProducerIF brokerProducer;

    public ProcessDef(AccountRepositoryIF accountRepository, BrokerProducerIF brokerProducer) {
        this.accountRepository = accountRepository;
        this.brokerProducer = brokerProducer;
    }

    public void start(List<TransferCommand> processDef){
        this.processDef = processDef;
        Iterator<TransferCommand> iterator = processDef.iterator();
        if (iterator.hasNext()){
            sendCommand(iterator.next());
        }
    }

    public void sendCommand(TransferCommand transferCommand){
        BankAccount bankAccount = accountRepository.getBankAccount(transferCommand.getAggregateId());
        brokerProducer.transfer(transferCommand.getAggregateId(), bankAccount, transferCommand);
        cStep.put(transferCommand.getTransactionId(),transferCommand.getCommandId());
    }


    public TransferCommand getNextTransferCommand(){
        Iterator<TransferCommand> iterator = processDef.iterator();
        while (iterator.hasNext()){
            TransferCommand pTransferCommand = iterator.next();
            String cTransferCommandId = cStep.get(pTransferCommand.getTransactionId());
            if (cTransferCommandId.equals(pTransferCommand.getCommandId())){
                if (iterator.hasNext())
                    return iterator.next();
                else
                    return null;
            }
        }
        return null;
    }


    public TransferCommand getPreTransferCommand(){
        Iterator<TransferCommand> iterator = processDef.listIterator();
        while (((ListIterator<TransferCommand>) iterator).hasPrevious()){
            TransferCommand pTransferCommand = ((ListIterator<TransferCommand>) iterator).previous();
            String cTransferCommandId = cStep.get(pTransferCommand.getTransactionId());
            if (cTransferCommandId.equals(pTransferCommand.getCommandId())){
                if (((ListIterator<TransferCommand>) iterator).hasPrevious())
                   return ((ListIterator<TransferCommand>) iterator).previous();
                else
                    return null;
            }
        }
        return null;
    }

    public void clear(){
        this.cStep.clear();
        this.processDef.clear();
    }

}
