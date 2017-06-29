/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package com.jdon.sample.test.bankaccount.a;

import java.util.HashMap;
import java.util.Map;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;
import com.jdon.annotation.model.OnCommand;

@Model
public class BankAccount {
	private String id;

	private int amount = 0;

	private Map<Integer, WithdrawEvent> eventsourcesW = new HashMap<Integer, WithdrawEvent>();
	private Map<Integer, DepositEvent> eventsourcesD = new HashMap<Integer, DepositEvent>();

	@Inject
	private DomainEventProducer domainEventProducer;

	public BankAccount(String id) {
		super();
		this.id = id;
	}

	public BankAccount(String id, int amount) {
		super();
		this.id = id;
		this.amount = amount;
	}

	@OnCommand("depositCommand")
	public Object deposit(TransferEvent transferEvent) {
		int amount2 = amount + transferEvent.getValue();
		if (amount2 > 1000) {
			TransferEvent transferEventNew = new ResultEvent(
					transferEvent.getId(), transferEvent.getValue(),
					this.getId());
			return domainEventProducer.failure(transferEventNew);
		}

		if (!(transferEvent instanceof WithdrawEvent)) {// first step
			DepositEvent transferEventNew = new DepositEvent(
					transferEvent.getId(), transferEvent.getValue(),
					transferEvent.getNextId(), this.getId());
			eventsourcesD.put(transferEventNew.getId(), transferEventNew);
			return domainEventProducer.nextStep(transferEventNew);
		}

		WithdrawEvent de = (WithdrawEvent) transferEvent;
		amount = amount + transferEvent.getValue();
		TransferEvent transferEventNew = new ResultEvent(transferEvent.getId(),
				transferEvent.getValue(), de.getPreId());
		return domainEventProducer.finish(transferEventNew);

	}

	@OnCommand("withdrawCommand")
	public Object withdraw(TransferEvent transferEvent) {
		int amount2 = amount - transferEvent.getValue();
		if (amount2 < 0) {
			String rootId = (transferEvent instanceof DepositEvent) ? ((DepositEvent) transferEvent)
					.getPreId() : this.getId();
			TransferEvent transferEventNew = new ResultEvent(
					transferEvent.getId(), transferEvent.getValue(), rootId);
			return domainEventProducer.failure(transferEventNew);
		}

		if (!(transferEvent instanceof DepositEvent)) {// first step
			WithdrawEvent transferEventNew = new WithdrawEvent(
					transferEvent.getId(), transferEvent.getValue(),
					transferEvent.getNextId(), this.getId());
			eventsourcesW.put(transferEventNew.getId(), transferEventNew);
			return domainEventProducer.nextStep(transferEventNew);

		}

		DepositEvent de = (DepositEvent) transferEvent;
		amount = amount - transferEvent.getValue();
		TransferEvent transferEventNew = new ResultEvent(transferEvent.getId(),
				transferEvent.getValue(), de.getPreId());
		return domainEventProducer.finish(transferEventNew);

	}

	@OnCommand("finishCommand")
	public Object finish(TransferEvent transferEvent) {
		if (eventsourcesW.containsKey(transferEvent.getId())){
			eventsourcesW.remove(transferEvent.getId());
			amount = amount - transferEvent.getValue();
		}else if (eventsourcesD.containsKey(transferEvent.getId())){
			eventsourcesD.remove(transferEvent.getId());
			amount = amount + transferEvent.getValue();		
		}
		return transferEvent;
	}

	@OnCommand("failureCommand")
	public Object fail(TransferEvent transferEvent) {		
		eventsourcesD.remove(transferEvent.getId());
		return transferEvent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

}
