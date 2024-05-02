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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import coffee.khyonieheart.lilac.TomlParser;
import coffee.khyonieheart.lilac.api.Commentable;

/**
 * Represents a TOML array. 
 *
 * Arrays in TOML are different than in Java, whereas Java arrays are constrained to one type, TOML arrays behave much more similarly to <code>List</code>s of <code>Object</code>s.
 * As such, they are implemented using a List rather than directly using an array, and are mutable and growable. This implementation guarantees preservation of iteration order.
 */
public class TomlArray implements Commentable, TomlObject<List<TomlObject<?>>>
{
	private List<TomlObject<?>> data = new ArrayList<>();
	private String comment;
	private int newlines = 0;

	public TomlArray(
		TomlObject<?>... objects
	) {
		for (TomlObject<?> o : objects)
		{
			this.data.add(o);
		}
	}

	public TomlArray(
		Collection<TomlObject<?>> data
	) {
		this.data.addAll(data);
	}

	@Override
	public List<TomlObject<?>> get() 
	{
		return data;
	}

	@Override
	public TomlObjectType getType() 
	{
		return TomlObjectType.ARRAY;
	}

	public String serialize(
		TomlParser parser
	) {
		StringBuilder builder = new StringBuilder("[ ");

		if (parser.getSeparateArrayIntoLines())
		{
			builder.append('\n');
		}

		if (!data.isEmpty())
		{
			int index = 0;
			builder.append(this.data.get(index).serialize());

			for (; index < this.data.size(); index++)
			{
				builder.append(", ");
				if (parser.getSeparateArrayIntoLines())
				{
					builder.append('\n');
				}
				builder.append((parser.getSeparateArrayIntoLines() ? "\t" : "") + this.data.get(index).serialize());
			}
		}

		if (parser.getSeparateArrayIntoLines())
		{
			builder.append('\n');
		}

		builder.append(" ]");

		return builder.toString();
	}

	@Override
	public String serialize() 
	{
		StringBuilder builder = new StringBuilder("[ ");

		if (!data.isEmpty())
		{
			int index = 0;
			builder.append(this.data.get(index).serialize());

			for (; index < this.data.size(); index++)
			{
				builder.append(", ");
				builder.append(this.data.get(index).serialize());
			}
		}

		builder.append(" ]");

		return builder.toString();
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
	public TomlArray clone()
	{
		List<TomlObject<?>> clone = new ArrayList<>(this.data.size());

		for (TomlObject<?> obj : this.data)
		{
			clone.add(obj.clone());
		}

		return new TomlArray(clone);
	}
}
