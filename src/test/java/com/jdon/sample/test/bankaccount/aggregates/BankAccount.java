/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.sample.test.bankaccount.aggregates;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;
import com.jdon.annotation.model.OnCommand;
import com.jdon.sample.test.bankaccount.command.Cancel;
import com.jdon.sample.test.bankaccount.command.TransferCommand;
import com.jdon.sample.test.bankaccount.event.TransferEvent;
import com.jdon.sample.test.bankaccount.infras.AggregatePub;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@Model
public class BankAccount {
	private final String id;

	private final int balance ;

	private Collection<TransferEvent> eventsources  = new CopyOnWriteArrayList<>();

	@Inject
	private AggregatePub aggregatePub;

	public BankAccount(String id, int balance) {
		this.id = id;
		this.balance = balance;
	}

	@OnCommand("transfer")
	public void transfer(TransferCommand transferCommand) {
		int balance2 = getBalance() + transferCommand.getValue();
		if (balance2 > 1000 || balance2 < 0) {
			aggregatePub.next(transferCommand.createCanceled());
		}
		TransferEvent transferEvent = transferCommand.creatTransferEvent();
		eventsources.add(transferEvent);
		aggregatePub.next(transferEvent);
	}

	@OnCommand("cancel")
	public void cancel(Cancel cancel) {
		int balance2 = getBalance()  - cancel.getTransferCommand().getValue();
		if (balance2 > 1000 || balance2 < 0) {
			System.err.println("can not be canceled " + cancel.getTransferCommand().getTransactionId() + " "+cancel.getTransferCommand().getAggregateId());
		}
		eventsources.add(cancel.getTransferCommand().createCanceled());
	}


	private int project(){
		return eventsources.stream().map(e->e.getValue()).reduce(this.balance,(a,b)->a+b);
	}


	public String getId() {
		return id;
	}

	public int getBalance() {
		return project();
	}
}
