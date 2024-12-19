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
package coffee.khyonieheart.lilac;

public class TomlSyntaxException extends Exception
{
	private int line;
	private int position;
	private int length;
	private String document;

	public TomlSyntaxException(
		String message,
		int line,
		int position,
		int length,
		String document
	) {
		super(message);
		this.line = line;
		this.position = position;
		this.length = length;
		this.document = document;
	}

	@Override
	public void printStackTrace()
	{
		String lineString = document.split("\n")[line];
		if (line < document.split("\n").length - 1)
		{
			// Encoded return arrow as int
			lineString += ((char) 9166);
		}

		System.out.println("TOML syntax error: " + this.getMessage() + " at line " + (line + 1) + ", position " + (this.position + 1));
		System.out.println(lineString);
		System.out.println(" ".repeat(position) + "^".repeat(this.length) + " (Here)");

		super.printStackTrace();
	}
}
