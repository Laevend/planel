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
import coffee.khyonieheart.lilac.api.StringType;

public class TomlString implements Commentable, TomlObject<String>, CharSequence
{
	private String string = null;
	private String comment;
	private StringType type = StringType.BASIC;
	private int newlines = 0;

	public TomlString(
		String string
	) {
		this.string = string.replace("\\\\", "\\")
			.replace("\\n", "\n");
	}

	public TomlString(
		String string,
		StringType type
	) {
		this(string);
		this.type = type;
	}

	public String get()
	{
		return this.string;
	}

	public StringType getStringType()
	{
		return this.type;
	}

	@Override
	public TomlObjectType getType() 
	{
		return TomlObjectType.STRING;
	}

	@Override
	public String serialize() 
	{
		return this.type.getStartAndEnd() + this.string + this.type.getStartAndEnd();
	}

	@Override
	public int length() 
	{
		return this.string.length();
	}

	@Override
	public char charAt(
		int index
	) {
		return this.string.charAt(index);
	}

	@Override
	public CharSequence subSequence(
		int start, 
		int end
	) {
		return this.string.subSequence(start, end);
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
	public TomlString clone()
	{
		return new TomlString(new String(this.string), this.type);
	}
}
