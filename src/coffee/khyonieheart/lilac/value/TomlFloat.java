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

public class TomlFloat implements Commentable, TomlObject<Float>
{
	private float value;
	private String comment;
	private int newlines = 0;

	public TomlFloat(
		float value
	) {
		this.value = value;
	}

	@Override
	public Float get() 
	{
		return this.value;
	}

	@Override
	public TomlObjectType getType() 
	{
		return TomlObjectType.FLOAT;
	}

	@Override
	public String serialize() 
	{
		if (this.value == Float.POSITIVE_INFINITY)
		{
			return "inf";
		}

		if (this.value == Float.NEGATIVE_INFINITY)
		{
			return "-inf";
		}

		if (this.value == Float.NaN)
		{
			return "nan";
		}

		return Float.toString(this.value);
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
	public TomlFloat clone()
	{
		return new TomlFloat(this.value);
	}
}
