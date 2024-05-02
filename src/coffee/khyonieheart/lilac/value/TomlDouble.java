/*
 * This file is part of Lilac, a high performance TOML language library.
 * Copyright (C) 2024 Hailey-Jane "Khyonie" Garrett
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package coffee.khyonieheart.lilac.value;

import coffee.khyonieheart.lilac.api.Commentable;

public class TomlDouble implements Commentable, TomlObject<Double>
{
	private double value;
	private String comment;
	private int newlines = 0;

	public TomlDouble(
		double value
	) {
		this.value = value;
	}

	@Override
	public Double get() 
	{
		return this.value;
	}

	@Override
	public TomlObjectType getType() 
	{
		return TomlObjectType.DOUBLE;
	}

	@Override
	public String serialize() 
	{
		return Double.toString(this.value);
	}

	@Override
	public String getComment() 
	{
		return this.comment;
	}

	@Override
	public void setComment(
		String comment
	) {
		this.comment = comment;
	}

	@Override
	public int getNumberOfTrailingNewlines() 
	{
		return this.newlines;
	}

	@Override
	public void incrementTrailingNewlines() 
	{
		this.newlines++;
	}

	@Override
	public TomlDouble clone()
	{
		return new TomlDouble(this.value);
	}
}
