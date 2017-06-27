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

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;
import com.jdon.annotation.model.OnCommand;
import com.jdon.sample.test.bankaccount.AccountParameterVO;

@Model
public class BankAccount {
	private String id;

	private int amount = 0;

	@Inject
	private DomainEventProduceIF domainEventProducer;

	public BankAccount(String id) {
		super();
		this.id = id;
	}

	@OnCommand("depositCommand")
	public Object deposit(AccountParameterVO parameterVO) {
		amount = amount + parameterVO.getValue();
		System.out.print("\n AggregateRootA Action " + amount);
		AccountParameterVO parameterVONew = new AccountParameterVO(parameterVO.getId(),
				amount, parameterVO.getNextId());
		return domainEventProducer.sendtoAnotherAggragate(parameterVONew);
	}

	@OnCommand("withdrawCommand")
	public Object withdraw(AccountParameterVO parameterVO) {
		amount = amount - parameterVO.getValue();
		AccountParameterVO parameterVOnew = new AccountParameterVO(parameterVO.getId(),
				amount, parameterVO.getNextId());
		return parameterVOnew;

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
