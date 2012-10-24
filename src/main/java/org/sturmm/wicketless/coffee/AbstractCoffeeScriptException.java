/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sturmm.wicketless.coffee;

/**
 * This is the super class for all Exceptions of CoffeeScript parsing/js
 * creation.
 * 
 * @author Martin Sturm
 * 
 */
public abstract class AbstractCoffeeScriptException extends RuntimeException
{

	public AbstractCoffeeScriptException()
	{
		super();
	}

	public AbstractCoffeeScriptException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public AbstractCoffeeScriptException(String msg)
	{
		super(msg);
	}

	public AbstractCoffeeScriptException(Throwable cause)
	{
		super(cause);
	}

}
