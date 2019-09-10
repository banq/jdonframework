package com.jdon.sample.test.bankaccount.infras;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.event.TransferEvent;

/**
 * this is the event Producer for Aggregate
 *
 */
@Introduce("message")
public class AggregatePub {

    @Send("next")
    public DomainMessage  next(TransferEvent transferEvent) {
        return new DomainMessage (transferEvent);
    }
}
